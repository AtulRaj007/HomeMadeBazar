package com.homemadebazar.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.GetWalletBalanceApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

public class WalletActivity extends BaseActivity implements View.OnClickListener {
    private UserModel userModel;
    private TextView tvWalletMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        setupToolbar();
    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("Wallet");
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(WalletActivity.this);
        tvWalletMoney = findViewById(R.id.tv_wallet_money);
        tvWalletMoney.setText(userModel.getWalletBalance() + "");
    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.ll_add_money).setOnClickListener(this);
        findViewById(R.id.ll_pay_money).setOnClickListener(this);
        findViewById(R.id.ll_accept_money).setOnClickListener(this);
        findViewById(R.id.tv_sent_to_bank).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userModel = SharedPreference.getUserModel(WalletActivity.this);
        try {
            tvWalletMoney.setText(userModel.getWalletBalance() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Constants.isBalanceRefresh) {
            Constants.isBalanceRefresh = false;
            getWalletBalance();
        } else {
            updateWalletUI();
        }
    }

    @Override
    protected void setData() {
        getWalletBalance();
    }


    private void updateWalletUI() {
        try {
            if (userModel.getWalletBalance() > 0) {
                findViewById(R.id.ll_total_balance).setBackgroundColor(getResources().getColor(R.color.green));
                findViewById(R.id.tv_sent_to_bank).setBackgroundColor(getResources().getColor(R.color.green));
            } else {
                findViewById(R.id.ll_total_balance).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                findViewById(R.id.tv_sent_to_bank).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getWalletBalance() {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(this, null);
            progressDialog.show();

            final GetWalletBalanceApiCall apiCall = new GetWalletBalanceApiCall(userModel.getUserId());
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                Double walletBalance = apiCall.getWalletBalance();
                                String accountId = apiCall.getAccountId();
                                userModel.setAccountId(accountId);
                                userModel.setWalletBalance(walletBalance);
                                tvWalletMoney.setText(walletBalance + "");
                                SharedPreference.saveUserModel(WalletActivity.this, userModel);
                                updateWalletUI();

                            } else {
                                DialogUtils.showAlert(WalletActivity.this, baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), WalletActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), WalletActivity.this, null);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_add_money:
                startActivity(new Intent(WalletActivity.this, AddMoneyActivity.class));
                break;
            case R.id.ll_pay_money:
                startActivity(new Intent(WalletActivity.this, PayMoneyMobileInputActivity.class));
                break;
            case R.id.ll_accept_money:
                startActivity(new Intent(WalletActivity.this, AcceptMoneyActivity.class));
                break;
            case R.id.tv_sent_to_bank:
                startActivity(new Intent(WalletActivity.this, SendMoneyToBankActivity.class));
                break;
        }
    }
}
