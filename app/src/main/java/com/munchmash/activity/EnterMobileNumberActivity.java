package com.munchmash.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;
import com.munchmash.R;
import com.munchmash.model.IsAccountExistModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.IsAccountExistApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.Utils;


public class EnterMobileNumberActivity extends BaseActivity implements View.OnClickListener {
    private CountryCodePicker countryCodePicker;
    private String countryCode, countryName;
    private EditText etMobileNumber;
    private static int MIN_DIGIT_NUMBER = 10;
    private String userId = "";
    //    private UserModel userModel;
    private boolean isSocialLogin;
    private static String KEY_USER_ID = "KEY_USER_ID";
    private static String KEY_IS_SOCIAL_LOGIN = "KEY_IS_SOCIAL_LOGIN";
//    private static String KEY_USER_MODEL="KEY_USER_MODEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mobile_number);
        setupToolbar();
    }

    @Override
    protected void initUI() {
        getBundleData();
        if (!TextUtils.isEmpty(userId))
            findViewById(R.id.tv_skip).setVisibility(View.VISIBLE);
        else
            userId = "";
        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp_country);
        etMobileNumber = (EditText) findViewById(R.id.et_mobile_number);
        countryCode = countryCodePicker.getDefaultCountryCode();
        countryName = countryCodePicker.getDefaultCountryName();
    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.btn_continue).setOnClickListener(this);
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = countryCodePicker.getSelectedCountryCode();
                countryName = countryCodePicker.getSelectedCountryName();
            }
        });
        findViewById(R.id.tv_skip).setOnClickListener(this);
    }

    public static Intent getIntent(Context context, String userId, boolean isSocialLogin) {
        Intent intent = new Intent(context, EnterMobileNumberActivity.class);
        intent.putExtra(KEY_USER_ID, userId);
        intent.putExtra(KEY_IS_SOCIAL_LOGIN, isSocialLogin);
        return intent;
    }

    private void getBundleData() {
        try {
            userId = getIntent().getStringExtra(KEY_USER_ID);
            isSocialLogin = getIntent().getBooleanExtra(KEY_IS_SOCIAL_LOGIN, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            case R.id.btn_continue:
                if (isValid())
                    checkEmailExists();
                break;
            case R.id.tv_skip:
                startActivity(SignUpActivity.getIntent(EnterMobileNumberActivity.this, userId, true));
                break;
        }
    }


    private boolean isValid() {
        if (TextUtils.isEmpty(etMobileNumber.getText().toString())) {
            DialogUtils.showAlert(EnterMobileNumberActivity.this, "Please enter Mobile Number");
            return false;
        } else if (etMobileNumber.getText().toString().length() < MIN_DIGIT_NUMBER) {
            DialogUtils.showAlert(EnterMobileNumberActivity.this, "Please enter valid Mobile Number");
            return false;
        }
        return true;
    }

    public void checkEmailExists() {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(this, null);
            progressDialog.show();


            final IsAccountExistApiCall apiCall = new IsAccountExistApiCall(countryCode, countryName, etMobileNumber.getText().toString(), userId);
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            IsAccountExistModel isAccountExistModel = apiCall.getResult();
                            if (isAccountExistModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                if (isAccountExistModel.isSignUpRequired()) {
                                    startActivity(VerificationCodeActivity.getIntent(EnterMobileNumberActivity.this, isAccountExistModel.getUserId(), isSocialLogin, countryCode + etMobileNumber.getText().toString().trim()));
                                } else {
                                    startActivity(EnterPasswordActivity.getIntent(EnterMobileNumberActivity.this, isAccountExistModel.getUserId(), isAccountExistModel.getMobile()));
                                }
                            } else {
                                DialogUtils.showAlert(EnterMobileNumberActivity.this, isAccountExistModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), EnterMobileNumberActivity.this, null);
                    }
                }
            }, false);
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), EnterMobileNumberActivity.this, null);
        }
    }
}
