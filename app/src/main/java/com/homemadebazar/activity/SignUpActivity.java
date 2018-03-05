package com.homemadebazar.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.homemadebazar.model.CustomAddress;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.SignupApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
    private String firstName, lastName, emailId, password, confirmPassword, appartmentNumber, streetNumber, area, city, state;
    private String userId;
    private String deviceToken = "", pinCode = "";
    private double latitude, longitude;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastLocation;
    private boolean isSocialLogin = false;
    private EditText etApartmentNo, etStreetNo, etArea, etCity, etState, etPincode;
    private Handler handler = new Handler();

    public static Intent getIntent(Context context, String userId, boolean isSocialLogin) {
        Intent intent = new Intent(context, SignUpActivity.class);
        intent.putExtra(KEY_USER_ID, userId);
        intent.putExtra(KEY_IS_SOCIAL_LOGIN, isSocialLogin);
        return intent;
    }

    public void getAddressFromLocation(final double latitude, final double longitude,
                                       final Context context, final Handler handler) {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("\n");
                        }
                        sb.append(address.getLocality()).append("\n");
                        sb.append(address.getPostalCode()).append("\n");
                        sb.append(address.getCountryName());
                        result = sb.toString();
                        final String locality = address.getLocality();
                        final String subLocality = address.getSubLocality();
                        final String postalCode = address.getPostalCode();
                        final String state = address.getSubThoroughfare();
                        System.out.println("Locality:-" + locality);//City
                        System.out.println("SubLocality:-" + subLocality);//Area
                        System.out.println("Postal Code:-" + postalCode);//PinCode
                        System.out.println("SubThrough Fair" + state);
                        System.out.println("Result:-" + result);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((EditText) findViewById(R.id.et_city)).setText(locality);
                                ((EditText) findViewById(R.id.et_area)).setText(subLocality);
                                ((EditText) findViewById(R.id.et_pincode)).setText(postalCode);
                                ((EditText) findViewById(R.id.et_state)).setText(state);
                            }
                        });

                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Latitude: " + latitude + " Longitude: " + longitude +
                                "\n\nAddress:\n" + result;
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Latitude: " + latitude + " Longitude: " + longitude +
                                "\n Unable to get address for this lat-long.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
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
        ivHomeChef = findViewById(R.id.iv_home_chef);
        ivFoodie = findViewById(R.id.iv_foodie);
        ivMarketPlace = findViewById(R.id.iv_market_place);
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
                            getAddressFromLocation(latitude, longitude, SignUpActivity.this, handler);

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
        appartmentNumber = ((EditText) findViewById(R.id.et_apartment_no)).getText().toString().trim();
        streetNumber = ((EditText) findViewById(R.id.et_street_no)).getText().toString().trim();
        area = ((EditText) findViewById(R.id.et_area)).getText().toString().trim();
        city = ((EditText) findViewById(R.id.et_city)).getText().toString().trim();
        state = ((EditText) findViewById(R.id.et_state)).getText().toString().trim();
        pinCode = ((EditText) findViewById(R.id.et_pincode)).getText().toString().trim();


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
        } else if (TextUtils.isEmpty(appartmentNumber)) {
            DialogUtils.showAlert(SignUpActivity.this, "Please enter apartment number");
            return false;
        } else if (TextUtils.isEmpty(streetNumber)) {
            DialogUtils.showAlert(SignUpActivity.this, "Please enter street number");
            return false;
        } else if (TextUtils.isEmpty(area)) {
            DialogUtils.showAlert(SignUpActivity.this, "Please enter area");
            return false;
        } else if (TextUtils.isEmpty(city)) {
            DialogUtils.showAlert(SignUpActivity.this, "Please enter city");
            return false;
        } else if (TextUtils.isEmpty(state)) {
            DialogUtils.showAlert(SignUpActivity.this, "Please enter state");
            return false;
        } else if (TextUtils.isEmpty(pinCode)) {
            DialogUtils.showAlert(SignUpActivity.this, "Please enter pincode");
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

            String completeAddress = CustomAddress.getCompleteAddress(appartmentNumber, streetNumber, area, city, state);
            final SignupApiCall apiCall = new SignupApiCall(userId, firstName, lastName, emailId, password,
                    String.valueOf(userRole.getRole()), deviceToken, latitude + "", longitude + "", completeAddress, pinCode);
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
}
