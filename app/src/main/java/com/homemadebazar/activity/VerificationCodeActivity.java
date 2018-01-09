package com.homemadebazar.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.homemadebazar.R;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.IsAccountExistModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.IsAccountExistApiCall;
import com.homemadebazar.network.apicall.SendOtpApiCall;
import com.homemadebazar.network.apicall.VerifyOtpApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.Utils;

public class VerificationCodeActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = ">>>>>Verification";
    private static String KEY_USER_ID="KEY_USER_ID";
    private EditText etFirstDigitPswd, etSecondDigitPswd, etThirdDigitPswd, etFourthDigitPswd;
    private IsAccountExistModel isAccountExistModel;
    private String userId;
    private static String KEY_IS_SOCIAL_LOGIN="KEY_IS_SOCIAL_LOGIN";
//    private static String KEY_USER_MODEL="KEY_USER_MODEL";
    private boolean isSocialLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        setupToolbar();
        initialise();

    }

    public static Intent getIntent(Context context,String userId,boolean isSocialLogin){
        Intent intent=new Intent(context,VerificationCodeActivity.class);
        intent.putExtra(KEY_USER_ID,userId);
        intent.putExtra(KEY_IS_SOCIAL_LOGIN,isSocialLogin);
        return intent;
    }

//    public static Intent getIntent(Context context, UserModel userModel, boolean isSocialLogin)
//    {
//        Intent intent=new Intent(context,VerificationCodeActivity.class);
//        intent.putExtra(KEY_USER_MODEL,userModel);
//        intent.putExtra(KEY_IS_SOCIAL_LOGIN,isSocialLogin);
//        return intent;
//    }

    public void getDataFromBundle(){
        userId=getIntent().getStringExtra(KEY_USER_ID);
        isSocialLogin=getIntent().getBooleanExtra(KEY_IS_SOCIAL_LOGIN,false);
    }


    @Override
    protected void initUI() {
        getDataFromBundle();
        etFirstDigitPswd = (EditText) findViewById(R.id.et_first_digit_password);
        etSecondDigitPswd = (EditText) findViewById(R.id.et_second_digit_password);
        etThirdDigitPswd = (EditText) findViewById(R.id.et_third_digit_password);
        etFourthDigitPswd = (EditText) findViewById(R.id.et_fourth_digit_password);
    }

    @Override
    protected void initialiseListener() {
        etFirstDigitPswd.addTextChangedListener(new GenericTextWatcher(etFirstDigitPswd));
        etSecondDigitPswd.addTextChangedListener(new GenericTextWatcher(etSecondDigitPswd));
        etThirdDigitPswd.addTextChangedListener(new GenericTextWatcher(etThirdDigitPswd));
        etFourthDigitPswd.addTextChangedListener(new GenericTextWatcher(etFourthDigitPswd));

        etFirstDigitPswd.setOnKeyListener(onKeyListener);
        etSecondDigitPswd.setOnKeyListener(onKeyListener);
        etThirdDigitPswd.setOnKeyListener(onKeyListener);
        etFourthDigitPswd.setOnKeyListener(onKeyListener);

        findViewById(R.id.btn_continue).setOnClickListener(this);
        findViewById(R.id.tv_resend_code).setOnClickListener(this);
    }

    @Override
    protected void setData() {
        sendOtp();
    }

    private void initialise() {
        etFirstDigitPswd.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etFirstDigitPswd, InputMethodManager.SHOW_IMPLICIT);
    }

    private void setupToolbar(){
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_continue:
                if(isValid())
                verifyOtp();
                break;
            case R.id.tv_resend_code:
                sendOtp();
                break;
        }

    }

    private boolean isValid(){
        if(etFirstDigitPswd.length()==1 && etSecondDigitPswd.length()==1 && etThirdDigitPswd.length()==1 && etFourthDigitPswd.length()==1)
        {
            return true;
        }else {
            DialogUtils.showAlert(VerificationCodeActivity.this,"Please enter OTP");
            return false;
        }
    }

    private class GenericTextWatcher implements TextWatcher {
        private View view;

        GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();

            switch (view.getId()) {
                case R.id.et_first_digit_password:
                    if (text.length() > 0) {
                        etSecondDigitPswd.requestFocus();
                    }

                    break;
                case R.id.et_second_digit_password:
                    if (text.length() > 0) {
                        etThirdDigitPswd.requestFocus();
                    }

                    break;
                case R.id.et_third_digit_password:
                    if (text.length() > 0) {
                        etFourthDigitPswd.requestFocus();
                    }

                    break;
                case R.id.et_fourth_digit_password:
                    if (text.length() > 0) {

                    }

                    break;
            }

        }
    }


    View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

            if (keyCode == keyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                switch (view.getId()) {
                    case R.id.et_first_digit_password:
                        return true;
                    case R.id.et_second_digit_password:
                        etFirstDigitPswd.requestFocus();
                        return true;
                    case R.id.et_third_digit_password:
                        etSecondDigitPswd.requestFocus();
                        return true;
                    case R.id.et_fourth_digit_password:
                        etThirdDigitPswd.requestFocus();
                        return true;
                }
            }
            return false;
        }

    };

    public void sendOtp() {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(this,null);
            progressDialog.show();

            final SendOtpApiCall apiCall = new SendOtpApiCall(userId);
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel=apiCall.getResult();
                            if(baseModel.getStatusCode()== Constants.ServerResponseCode.SUCCESS){
                                DialogUtils.showAlert(VerificationCodeActivity.this,baseModel.getStatusMessage());
                            }else{
                                DialogUtils.showAlert(VerificationCodeActivity.this,baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), VerificationCodeActivity.this,null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), VerificationCodeActivity.this,null);
        }
    }

    public void verifyOtp() {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(this,null);
            progressDialog.show();

            String inputOtp=etFirstDigitPswd.getText().toString()+etSecondDigitPswd.getText().toString()
                    +etThirdDigitPswd.getText().toString()+etFourthDigitPswd.getText().toString();
            Log.d(TAG,"Input Otp :-"+inputOtp);
            final VerifyOtpApiCall apiCall = new VerifyOtpApiCall(userId,inputOtp);
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel=apiCall.getResult();
                            if(baseModel.getStatusCode()== Constants.ServerResponseCode.SUCCESS){
                                Toast.makeText(VerificationCodeActivity.this,baseModel.getStatusMessage(),Toast.LENGTH_SHORT).show();
                                startActivity(SignUpActivity.getIntent(VerificationCodeActivity.this,userId,isSocialLogin));
                            }else{
                                DialogUtils.showAlert(VerificationCodeActivity.this,baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), VerificationCodeActivity.this,null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), VerificationCodeActivity.this,null);
        }
    }
}
