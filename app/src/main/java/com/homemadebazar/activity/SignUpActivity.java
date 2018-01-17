package com.homemadebazar.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.homemadebazar.R;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.SignupApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ">>>>>Signup";
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private static final int CODE_IMPORT_SOCIAL_DATA = 101;
    private static String KEY_USER_ID = "KEY_USER_ID";
    private static String address = "";
    private static String KEY_IS_SOCIAL_LOGIN = "KEY_IS_SOCIAL_LOGIN";
    private Constants.Role userRole = Constants.Role.HOME_CHEF;
    private int[] selectedRole = {R.drawable.home_chef_selected, R.drawable.foodie_selected, R.drawable.market_place_selected};
    private int[] unSelectedRole = {R.drawable.home_chef_unselected, R.drawable.foodie_unselected, R.drawable.market_place_unselected};
    private ImageView ivHomeChef, ivFoodie, ivMarketPlace;
    private String firstName, lastName, emailId, password, confirmPassword;
    private String userId;
    private String deviceToken = "", pinCode = "";
    private double latitude, longitude;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastLocation;
    private boolean isSocialLogin = false;

    public static Intent getIntent(Context context, String userId, boolean isSocialLogin) {
        Intent intent = new Intent(context, SignUpActivity.class);
        intent.putExtra(KEY_USER_ID, userId);
        intent.putExtra(KEY_IS_SOCIAL_LOGIN, isSocialLogin);
        return intent;
    }

    protected static JSONObject getLocationFormGoogle(String placesName) {

        String apiRequest = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + placesName;
        HttpGet httpGet = new HttpGet(apiRequest);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    protected static String getCityAddress(JSONObject result) {
        if (result.has("results")) {
            try {
                JSONArray array = result.getJSONArray("results");
                address = array.getJSONObject(0).optString("formatted_address");
                System.out.println("Address:-" + address);
                if (array.length() > 0) {
                    JSONObject place = array.getJSONObject(0);
                    JSONArray components = place.getJSONArray("address_components");
                    for (int i = 0; i < components.length(); i++) {
                        JSONObject component = components.getJSONObject(i);
                        JSONArray types = component.getJSONArray("types");
                        for (int j = 0; j < types.length(); j++) {
                            if (types.getString(j).equals("postal_code")) {
                                return component.getString("long_name");
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        deviceToken = FirebaseInstanceId.getInstance().getToken();
        if (TextUtils.isEmpty(deviceToken))
            System.out.println("DeviceToken:-" + deviceToken);
        setupToolbar();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            getLocation();
        }
    }

    @Override
    protected void initUI() {
        getDatafromBundle();
        ivHomeChef = (ImageView) findViewById(R.id.iv_home_chef);
        ivFoodie = (ImageView) findViewById(R.id.iv_foodie);
        ivMarketPlace = (ImageView) findViewById(R.id.iv_market_place);

    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.btn_continue).setOnClickListener(this);
        ivHomeChef.setOnClickListener(this);
        ivFoodie.setOnClickListener(this);
        ivMarketPlace.setOnClickListener(this);
    }

    @Override
    protected void setData() {
        if (isSocialLogin && Constants.socialUserModel != null) {
            ((EditText) findViewById(R.id.et_email_id)).setFocusable(false);
            ((EditText) findViewById(R.id.et_email_id)).setText(Constants.socialUserModel.getEmailId());
            ((EditText) findViewById(R.id.et_first_name)).setText(Constants.socialUserModel.getFirstName());
            ((EditText) findViewById(R.id.et_last_name)).setText(Constants.socialUserModel.getLastName());
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            latitude = mLastLocation.getLatitude();
                            longitude = mLastLocation.getLongitude();
                            Log.e("Latitude", mLastLocation.getLatitude() + "");
                            Log.e("Longitude", mLastLocation.getLongitude() + "");
                            new Address().execute();

                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());

                        }
                    }
                });
    }

    private void getDatafromBundle() {
        userId = getIntent().getStringExtra(KEY_USER_ID);
        isSocialLogin = getIntent().getBooleanExtra(KEY_IS_SOCIAL_LOGIN, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:
                if (isValid()) {
                    doSignUp();
                }
                break;
            case R.id.iv_home_chef:
                userRole = Constants.Role.HOME_CHEF;
                selectImageRole(userRole.getRole());
                break;
            case R.id.iv_foodie:
                userRole = Constants.Role.FOODIE;
                selectImageRole(userRole.getRole());
                break;
            case R.id.iv_market_place:
                userRole = Constants.Role.MARKET_PLACE;
                selectImageRole(userRole.getRole());
                break;
        }
    }

    private boolean isValid() {
        firstName = ((EditText) findViewById(R.id.et_first_name)).getText().toString().trim();
        lastName = ((EditText) findViewById(R.id.et_last_name)).getText().toString().trim();
        emailId = ((EditText) findViewById(R.id.et_email_id)).getText().toString().trim();
        password = ((EditText) findViewById(R.id.et_password)).getText().toString().trim();
        confirmPassword = ((EditText) findViewById(R.id.et_confirm_password)).getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            DialogUtils.showAlert(SignUpActivity.this, "Please enter first name");
            return false;
        } else if (TextUtils.isEmpty(lastName)) {
            DialogUtils.showAlert(SignUpActivity.this, "Please enter last name");
            return false;
        } else if (TextUtils.isEmpty(emailId)) {
            DialogUtils.showAlert(SignUpActivity.this, "Please enter emailId");
            return false;
        } else if (!Utils.isValid(emailId, Utils.TYPE_EMAIL)) {
            DialogUtils.showAlert(SignUpActivity.this, "Please enter valid emailId");
            return false;
        } else if (!Utils.checkPassword(password)) {
            DialogUtils.showAlert(SignUpActivity.this, "Password must contain combination of text,digit and special character and minium 8 digits.");
            return false;
        } else if (!password.equals(confirmPassword)) {
            DialogUtils.showAlert(SignUpActivity.this, "Password and confirm password doesnot match");
            return false;
        }
        return true;

    }

    private void selectImageRole(int selectedPosition) {
        if (selectedPosition == Constants.Role.HOME_CHEF.getRole()) {
            ivHomeChef.setImageResource(selectedRole[0]);
        } else {
            ivHomeChef.setImageResource(unSelectedRole[0]);
        }

        if (selectedPosition == Constants.Role.FOODIE.getRole()) {
            ivFoodie.setImageResource(selectedRole[1]);
        } else {
            ivFoodie.setImageResource(unSelectedRole[1]);
        }

        if (selectedPosition == Constants.Role.MARKET_PLACE.getRole()) {
            ivMarketPlace.setImageResource(selectedRole[2]);
        } else {
            ivMarketPlace.setImageResource(unSelectedRole[2]);
        }
    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (!isSocialLogin)
            findViewById(R.id.tv_sync_social_data).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_sync_social_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(SocialLoginActivity.getIntent(SignUpActivity.this, true), CODE_IMPORT_SOCIAL_DATA);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void doSignUp() {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(this, null);
            progressDialog.show();

            final SignupApiCall apiCall = new SignupApiCall(userId, firstName, lastName, emailId, password,
                    String.valueOf(userRole.getRole()), deviceToken, latitude + "", longitude + "", pinCode);
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            UserModel userModel = apiCall.getResult();
                            if (userModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                Log.d(TAG, userModel.toString());
                                Utils.hideSoftKeyboard(SignUpActivity.this);
                                SharedPreference.saveUserModel(SignUpActivity.this, userModel);
                                Utils.openAccountTypeHomeScreen(SignUpActivity.this, userModel.getAccountType());
                            } else {
                                DialogUtils.showAlert(SignUpActivity.this, userModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), SignUpActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), SignUpActivity.this, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_IMPORT_SOCIAL_DATA && resultCode == RESULT_OK) {
            if (Constants.socialUserModel != null) {
                ((EditText) findViewById(R.id.et_email_id)).setText(Constants.socialUserModel.getEmailId());
                ((EditText) findViewById(R.id.et_first_name)).setText(Constants.socialUserModel.getFirstName());
                ((EditText) findViewById(R.id.et_last_name)).setText(Constants.socialUserModel.getLastName());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    class Address extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            JSONObject result = getLocationFormGoogle(latitude + "," + longitude);
            return getCityAddress(result);
        }

        @Override
        protected void onPostExecute(String postalAddress) {
            System.out.println("Address:-" + address);
            System.out.println("Postal Code:-" + postalAddress);
            ((EditText) findViewById(R.id.et_address)).setText(address);
            ((EditText) findViewById(R.id.et_pincode)).setText(postalAddress);

        }
    }
}
