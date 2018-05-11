package com.homemadebazar.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.homemadebazar.R;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.PasswordLoginApiCall;
import com.homemadebazar.network.apicall.ResetPasswordApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

public class EnterPasswordActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ">>>>>SignInPassword";
    private static String KEY_USER_ID = "KEY_USER_ID";
    private static String KEY_MOBILE = "KEY_MOBILE";
    private String userId, mobile;
    private String password;

    public static Intent getIntent(Context context, String userId, String mobile) {
        Intent intent = new Intent(context, EnterPasswordActivity.class);
        intent.putExtra(KEY_USER_ID, userId);
        intent.putExtra(KEY_MOBILE, mobile);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);
        setupToolbar();
    }

    private void getDataFromBundle() {
        userId = getIntent().getStringExtra(KEY_USER_ID);
        mobile = getIntent().getStringExtra(KEY_MOBILE);
    }

    @Override
    protected void initUI() {
        getDataFromBundle();
    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.btn_continue).setOnClickListener(this);
        findViewById(R.id.tv_reset_password).setOnClickListener(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_continue:
                if (isValid()) {
                    doSignIn();
                }
                break;
            case R.id.tv_reset_password:
                DialogUtils.showAlert(EnterPasswordActivity.this, "Do you want to reset your password.\nPassword will be sent to your mobile number", new Runnable() {
                    @Override
                    public void run() {
                        resetPassword();
                    }
                }, new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                break;
        }
    }

    private boolean isValid() {
        password = ((EditText) findViewById(R.id.et_password)).getText().toString();
        if (password.length() < 6) {
            DialogUtils.showAlert(EnterPasswordActivity.this, "Password must contain at least 6 digits");
            return false;
        }
        return true;
    }

    public void doSignIn() {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(this, null);
            progressDialog.show();

            final PasswordLoginApiCall apiCall = new PasswordLoginApiCall(userId, mobile, password);
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            UserModel userModel = apiCall.getResult();
                            if (userModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                Log.d(TAG, userModel.toString());
                                Utils.hideSoftKeyboard(EnterPasswordActivity.this);
                                SharedPreference.saveUserModel(EnterPasswordActivity.this, userModel);
                                Utils.openAccountTypeHomeScreen(EnterPasswordActivity.this, userModel.getAccountType());
                            } else {
                                DialogUtils.showAlert(EnterPasswordActivity.this, userModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), EnterPasswordActivity.this, null);
                    }
                }
            }, false);
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), EnterPasswordActivity.this, null);
        }
    }

    private void resetPassword() {
        try {
            final ResetPasswordApiCall apiCall = new ResetPasswordApiCall(userId);
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                DialogUtils.showAlert(EnterPasswordActivity.this, baseModel.getStatusMessage());
                            } else {
                                DialogUtils.showAlert(EnterPasswordActivity.this, baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), EnterPasswordActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), EnterPasswordActivity.this, null);
        }
    }
}
