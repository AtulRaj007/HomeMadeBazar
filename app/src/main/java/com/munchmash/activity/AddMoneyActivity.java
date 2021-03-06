package com.munchmash.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.munchmash.R;
import com.munchmash.model.BaseModel;
import com.munchmash.model.UserModel;
import com.munchmash.network.GetRequest;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.AddMoneyApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

import org.json.JSONObject;

public class AddMoneyActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvWalletBalance;
    private EditText etAmount;
    private UserModel userModel;
    private String transactionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        setupToolbar();
    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText(getResources().getString(R.string.add_money));
        ((TextView) findViewById(R.id.et_amount)).setHint("Enter Amount " + Utils.getCurrencySymbol(this, Utils.Currency.SIGN));
    }

    @Override
    protected void initUI() {
        tvWalletBalance = findViewById(R.id.tv_wallet_balance);
        etAmount = findViewById(R.id.et_amount);
        userModel = SharedPreference.getUserModel(AddMoneyActivity.this);
    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.btn_add_money).setOnClickListener(this);
    }

    @Override
    protected void setData() {
        tvWalletBalance.setText(userModel.getWalletBalance() + Utils.getCurrencySymbol(this, Utils.Currency.SIGN));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void addMoneyToWallet() {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(this, null);
            progressDialog.show();

            final AddMoneyApiCall apiCall = new AddMoneyApiCall(userModel.getAccountId(), etAmount.getText().toString());
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                transactionId = apiCall.getTransactionId();
                                String token = apiCall.getToken();
                                launchDropInRequest(token, etAmount.getText().toString().trim());

                            } else {
                                DialogUtils.showAlert(AddMoneyActivity.this, baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), AddMoneyActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), AddMoneyActivity.this, null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_money:
                addMoneyToWallet();
                break;
        }
    }

    private void launchDropInRequest(String clientToken, String amount) {

        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(clientToken)
                .amount(amount)
                .requestThreeDSecureVerification(true)
                .collectDeviceData(true)
                .disablePayPal();

        startActivityForResult(dropInRequest.getIntent(AddMoneyActivity.this), Constants.Keys.REQUEST_ADD_MONEY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.Keys.REQUEST_ADD_MONEY) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                String paymentMethodNonce = result.getPaymentMethodNonce().getNonce();
                System.out.println("Result:-" + result.toString() + "Nonce:-" + paymentMethodNonce);
                sendPaymentDetailsToServer(userModel.getAccountId(), paymentMethodNonce, etAmount.getText().toString().trim(), transactionId);
            } else if (resultCode == RESULT_CANCELED) {

            } else {
//                Exception error = (Exception) data.getSerializableExtra(MainActivity.EXTRA_ERROR);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getServiceUrl(String accountId, String paymentMethodNonce, String amount, String transactionId) {
        String url = Constants.ServerURL.CREATE_PAYMENT + "ACCOUNT_ID=" + accountId + "&PAYMENT_METHOD_NONCE=" + paymentMethodNonce + "&AMOUNT=" + amount + "&TXN_NO=" + transactionId;
        System.out.println("Service Url:-" + url);
        return url;
    }

    private void sendPaymentDetailsToServer(String accountId, String paymentMethodNonce, String amount, String transactionId) {
        if (Constants.AppDebug.isPaymentDebug) {
            System.out.println(Constants.ServiceTAG.URL + getServiceUrl(accountId, paymentMethodNonce, amount, transactionId));
            return;
        }

        final ProgressDialog progressDialog = DialogUtils.getProgressDialog(this, null);
        progressDialog.show();
        new GetRequest(AddMoneyActivity.this, new GetRequest.ApiCompleteListener() {
            @Override
            public void onApiCompleteListener(String response) {
                DialogUtils.hideProgressDialog(progressDialog);
                System.out.println("====== Response ======" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    int statusCode = object.optInt("StatusCode");
                    String statusMessage = object.optString("StatusMessage");
                    if (statusCode == Constants.ServerResponseCode.SUCCESS) {
                        Double walletBalance = object.optDouble("NewWalletAmount");
                        userModel.setWalletBalance(walletBalance);
                        SharedPreference.saveUserModel(AddMoneyActivity.this, userModel);

                        DialogUtils.showAlert(AddMoneyActivity.this, "Money Added Successfully", new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        });
                    } else {
                        DialogUtils.showAlert(AddMoneyActivity.this, statusMessage, new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, "Payment Failed");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).execute(getServiceUrl(accountId, paymentMethodNonce, amount, transactionId));
    }
}
