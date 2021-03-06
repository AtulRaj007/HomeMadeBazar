package com.munchmash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.munchmash.R;
import com.munchmash.model.BaseModel;
import com.munchmash.model.UserModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.GetWalletBalanceApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

public class WalletActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private UserModel userModel;
    private TextView tvWalletMoney;
    private SwipeRefreshLayout swipeRefreshLayout;

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
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.ll_add_money).setOnClickListener(this);
        findViewById(R.id.ll_pay_money).setOnClickListener(this);
        findViewById(R.id.ll_accept_money).setOnClickListener(this);
        findViewById(R.id.tv_sent_to_bank).setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userModel = SharedPreference.getUserModel(WalletActivity.this);
        try {
            tvWalletMoney.setText(userModel.getWalletBalance() + Utils.getCurrencySymbol(this, Utils.Currency.SIGN));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Constants.isBalanceRefresh) {
            Constants.isBalanceRefresh = false;
            swipeRefreshLayout.setRefreshing(true);
            getWalletBalance();
        } else {
            updateWalletUI();
        }
    }

    @Override
    protected void setData() {
        tvWalletMoney.setText(userModel.getWalletBalance() + Utils.getCurrencySymbol(this, Utils.Currency.SIGN));
        swipeRefreshLayout.setRefreshing(true);
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
            final GetWalletBalanceApiCall apiCall = new GetWalletBalanceApiCall(userModel.getUserId());
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                Double walletBalance = apiCall.getWalletBalance();
                                String accountId = apiCall.getAccountId();
                                userModel.setAccountId(accountId);
                                userModel.setWalletBalance(walletBalance);
                                tvWalletMoney.setText(walletBalance + Utils.getCurrencySymbol(WalletActivity.this, Utils.Currency.SIGN));
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
                if (userModel.getWalletBalance() > 0)
                    startActivity(new Intent(WalletActivity.this, SendMoneyToBankActivity.class));
                else
                    DialogUtils.showAlert(WalletActivity.this, "Add money to wallet");
                break;
        }
    }

    @Override
    public void onRefresh() {
        getWalletBalance();
    }
}
