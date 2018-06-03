package com.munchmash.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.munchmash.R;
import com.munchmash.model.UserModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.SocialLoginApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class SocialLoginActivity extends BaseActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = ">>>>>SocialActivity";
    private static final int REQUEST_ACCOUNTS_PERMISSION = 101;
    private GoogleApiClient mGoogleApiClient;
    private final static int RC_SIGN_IN = 100;
    private CallbackManager callbackManager;
    private static final String KEY_IS_IMPORT_DATA="KEY_IS_IMPORT_DATA";
    private boolean isImportData=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login);
        setupToolbar();
    }

    @Override
    protected void initUI() {
        getDatafromBundle();
        initialiseGoogle();
        initialiseFacebook();
    }

    private void initialiseGoogle() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void initialiseFacebook() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.e(TAG, loginResult.toString());


                        // Facebook Email address
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        Log.v("Facebook Response ", response.toString());

                                        try {
                                            String id = object.getString("id");
                                            String name = object.getString("name");
                                            String email = object.getString("email");
                                            String gender = object.getString("gender");
                                            String nameArray[]=name.split(" ");
                                            String firstName="",lastName="";
                                            try {
                                                firstName = nameArray[0];
                                                lastName = nameArray[nameArray.length - 1];
                                            }catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }
                                            if(isImportData)
                                            {
                                                UserModel userModel=new UserModel();
                                                userModel.setEmailId(email);
                                                userModel.setFirstName(name);
                                                Constants.socialUserModel=userModel;
                                                setResult(RESULT_OK);
                                                finish();
                                            }else {
                                                doSocialLogin(id, email, firstName, lastName, "", "", "", "", Constants.SignInType.FACEBOOK, "");
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();


                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(SocialLoginActivity.this, "Facebook Login Failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.e(TAG, exception.toString());
                    }
                });
    }

    public static Intent getIntent(Context context, boolean isImportData){
        Intent intent=new Intent(context,SocialLoginActivity.class);
        intent.putExtra(KEY_IS_IMPORT_DATA,isImportData);
        return intent;
    }

    private void getDatafromBundle(){
        isImportData=getIntent().getBooleanExtra(KEY_IS_IMPORT_DATA,false);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.iv_google).setOnClickListener(this);
        findViewById(R.id.iv_facebook).setOnClickListener(this);
    }

    @Override
    protected void setData() {

    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_google:
                if (ContextCompat.checkSelfPermission(SocialLoginActivity.this, android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SocialLoginActivity.this, new String[]{android.Manifest.permission.GET_ACCOUNTS}, REQUEST_ACCOUNTS_PERMISSION);
                } else {
                    signIn();
                }
                break;
            case R.id.iv_facebook:
                signInFacebook();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("===== onConnectionFailed =====");
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String name = acct.getDisplayName();
            String email = acct.getEmail();
            String id = acct.getId();
            String nameArray[]=name.split(" ");
            String firstName="",lastName="";
            try {
                firstName = nameArray[0];
                lastName = nameArray[nameArray.length - 1];
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            System.out.println("Name:-" + name);
            System.out.println("Email:-" + email);

            if(isImportData)
            {
                UserModel userModel=new UserModel();
                userModel.setEmailId(email);
                userModel.setFirstName(name);
                Constants.socialUserModel=userModel;
                setResult(RESULT_OK);
                finish();
            }else {
                doSocialLogin(id, email, firstName, lastName, "", "", "", "", Constants.SignInType.GOOGLE, "");
            }
        } else {
            System.out.println("...Google Login Failed...");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                int statusCode = result.getStatus().getStatusCode();
                handleSignInResult(result);
//                }else{
//                    Toast.makeText(SocialLoginActivity.this,R.string.str_google_login_failed,Toast.LENGTH_SHORT).show();
//                }

                break;

        }
    }


    private void doSocialLogin( String uniqueId, String emailId,String firstName, String lastName,
                                String deviceToken, String latitude, String longitude,
                                String pinCode,String signInType,String profilePic){
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(this,null);
            progressDialog.show();

            final SocialLoginApiCall apiCall = new SocialLoginApiCall(uniqueId,emailId,firstName,lastName,deviceToken,
                    latitude,longitude,pinCode,signInType,profilePic);
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            UserModel userModel=apiCall.getResult();
                            Log.e("Social UserModel:-",userModel.toString());
                            if(userModel.getStatusCode()== Constants.ServerResponseCode.SUCCESS){
                                Log.d(TAG,userModel.toString());
                                if(apiCall.isSignUpRequired()) {
                                    startActivity(EnterMobileNumberActivity.getIntent(SocialLoginActivity.this,userModel.getUserId(),true));
                                    Constants.socialUserModel=userModel;
//                                    startActivity(EnterMobileNumberActivity.getIntent(SocialLoginActivity.this,userModel,true));
                                    finish();
                                }else{
                                    SharedPreference.saveUserModel(SocialLoginActivity.this,userModel);
                                    Intent intent = new Intent(SocialLoginActivity.this, HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }else{
                                DialogUtils.showAlert(SocialLoginActivity.this,userModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), SocialLoginActivity.this,null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), SocialLoginActivity.this,null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCOUNTS_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    signIn();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
