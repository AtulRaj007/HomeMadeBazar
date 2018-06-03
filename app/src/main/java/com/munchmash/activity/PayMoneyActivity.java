package com.munchmash.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.munchmash.R;
import com.munchmash.model.BaseModel;
import com.munchmash.model.UserModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.PayMoneyApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

public class PayMoneyActivity extends BaseActivity implements View.OnClickListener {
    private static String KEY_MOBILE_NUMBER = "KEY_MOBILE_NUMBER";
    private String mobileNumber;
    private EditText etMobileNumber, etAmount, etDescription;
    private UserModel userModel;

    public static Intent getPayIntent(Context context, String mobileNumber) {
        Intent intent = new Intent(context, PayMoneyActivity.class);
        intent.putExtra(KEY_MOBILE_NUMBER, mobileNumber);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_money);
        setupToolbar();
    }

    @Override
    protected void initUI() {
        mobileNumber = getIntent().getStringExtra(KEY_MOBILE_NUMBER);
        userModel = SharedPreference.getUserModel(PayMoneyActivity.this);
        etMobileNumber = findViewById(R.id.et_mobile_number);
        etAmount = findViewById(R.id.et_amount);
        etDescription = findViewById(R.id.et_description);
    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.btn_pay_money).setOnClickListener(this);
    }

    @Override
    protected void setData() {
        if (mobileNumber != null)
            etMobileNumber.setText(mobileNumber);

    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("Pay Money");

    }

    private boolean isValid() {
        return true;
    }

    private void payMoney(String accountId, String mobileNumber, String amount, String description) {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(this, null);
            progressDialog.show();

            final PayMoneyApiCall apiCall = new PayMoneyApiCall(accountId, mobileNumber, amount, description);
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                Double walletBalance = apiCall.getWalletBalance();
                                System.out.println("WalletBalance:-" + walletBalance.toString());
                                userModel.setWalletBalance(walletBalance);
                                SharedPreference.saveUserModel(PayMoneyActivity.this, userModel);
                                DialogUtils.showAlert(PayMoneyActivity.this, "Money Paid Successfully", new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                });
                            } else {
                                DialogUtils.showAlert(PayMoneyActivity.this, baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), PayMoneyActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), PayMoneyActivity.this, null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay_money:
                if (isValid()) {
                    payMoney(userModel.getAccountId(), etMobileNumber.getText().toString().trim(), etAmount.getText().toString().trim(), etDescription.getText().toString().trim());
                }
                break;
        }
    }
}
