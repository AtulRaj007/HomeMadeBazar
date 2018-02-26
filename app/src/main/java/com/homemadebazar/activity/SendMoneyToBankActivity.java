package com.homemadebazar.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.util.DialogUtils;

public class SendMoneyToBankActivity extends BaseActivity implements View.OnClickListener {
    private EditText etAccountNumber, etAccountHolderName, etIfscCode, etAmount;
    private Button btnTransfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money_to_bank);
        setUpToolbar();
    }

    @Override
    protected void initUI() {
        etAccountNumber = findViewById(R.id.et_account_number);
        etAccountHolderName = findViewById(R.id.et_account_holder_name);
        etIfscCode = findViewById(R.id.et_ifsc_code);
        etAmount = findViewById(R.id.et_amount);
        btnTransfer = findViewById(R.id.btn_transfer);
    }

    @Override
    protected void initialiseListener() {
        btnTransfer.setOnClickListener(this);
    }

    @Override
    protected void setData() {

    }

    private void setUpToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("Send Money");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_transfer:
                if (isValid()) {
                    sendMoneyToBank();
                }
                break;
        }
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(etAccountNumber.getText().toString().trim())) {
            DialogUtils.showAlert(this, "Please enter account number.");
            return false;
        } else if (TextUtils.isEmpty(etAccountHolderName.getText().toString().trim())) {
            DialogUtils.showAlert(this, "Please enter account's holder name.");
            return false;
        } else if (TextUtils.isEmpty(etIfscCode.getText().toString().trim())) {
            DialogUtils.showAlert(this, "Please enter IFSC code.");
            return false;
        } else if (TextUtils.isEmpty(etAmount.getText().toString().trim())) {
            DialogUtils.showAlert(this, "Please enter amount.");
            return false;
        }
        return true;
    }

    private void sendMoneyToBank() {
//        try {
//            final Dialog progressDialog = DialogUtils.getProgressDialog(SendMoneyToBankActivity.this, null);
//            progressDialog.show();
//            final ChangePasswordApiCall apiCall = new ChangePasswordApiCall(userModel.getUserId(), etCurrentPassword.getText().toString().trim(), etNewPassword.getText().toString().trim());
//            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {
//
//                @Override
//                public void onComplete(Exception e) {
//                    if (e == null) { // Success
//                        DialogUtils.hideProgressDialog(progressDialog);
//                        try {
//                            BaseModel baseModel = apiCall.getBaseModel();
//                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
//                                DialogUtils.showAlert(ChangePasswordActivity.this, baseModel.getStatusMessage(), new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        finish();
//                                    }
//                                });
//                            } else {
//                                DialogUtils.showAlert(ChangePasswordActivity.this, baseModel.getStatusMessage());
//                            }
//
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                        }
//                    } else { // Failure
//                        Utils.handleError(e.getMessage(), ChangePasswordActivity.this, null);
//                    }
//                }
//            });
//        } catch (Exception e) {
//            Utils.handleError(e.getMessage(), ChangePasswordActivity.this, null);
//        }
    }
}
