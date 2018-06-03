package com.munchmash.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.munchmash.R;
import com.munchmash.adapter.MarketPlaceMyOrdersAdapter;
import com.munchmash.model.BaseModel;
import com.munchmash.model.MarketPlaceMyOrdersModel;
import com.munchmash.model.UserModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.MarketPlaceOrdersApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

import java.util.ArrayList;

public class MarketPlaceMyOrdersActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private UserModel userModel;
    private MarketPlaceMyOrdersAdapter marketPlaceMyOrdersAdapter;
    private ArrayList<MarketPlaceMyOrdersModel> marketPlaceOrderModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place_my_orders);
        setupToolbar();
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(this);
        recyclerView = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        linearLayoutManager = new LinearLayoutManager(this);
        marketPlaceMyOrdersAdapter = new MarketPlaceMyOrdersAdapter(this, marketPlaceOrderModelArrayList);
    }

    @Override
    protected void initialiseListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void setData() {
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(marketPlaceMyOrdersAdapter);
        swipeRefreshLayout.setRefreshing(true);
        getMarketPlaceMyOrders();
    }

    @Override
    public void onRefresh() {
        getMarketPlaceMyOrders();
    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("MarketPlace Orders");

    }

    private void getMarketPlaceMyOrders() {
        try {
            String viewOrdersFor = ViewOrderFor.MARKETPLACE;
            if (userModel.getAccountType().equals(Constants.Role.HOME_CHEF.getStringRole()))
                viewOrdersFor = ViewOrderFor.HOMECHEF;
            final MarketPlaceOrdersApiCall apiCall = new MarketPlaceOrdersApiCall(userModel.getUserId(), viewOrdersFor);
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                findViewById(R.id.tv_no_record_found).setVisibility(View.GONE);
                                marketPlaceOrderModelArrayList.clear();
                                marketPlaceOrderModelArrayList.addAll(apiCall.getResult());
                                marketPlaceMyOrdersAdapter.notifyDataSetChanged();
                            } else if (baseModel.getStatusCode() == Constants.ServerResponseCode.NO_RECORD_FOUND) {
                                marketPlaceOrderModelArrayList.clear();
                                marketPlaceMyOrdersAdapter.notifyDataSetChanged();
                                findViewById(R.id.tv_no_record_found).setVisibility(View.VISIBLE);
                            } else {
                                DialogUtils.showAlert(MarketPlaceMyOrdersActivity.this, baseModel.getStatusMessage());
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), MarketPlaceMyOrdersActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), this, null);
        }
    }

    interface ViewOrderFor {
        String HOMECHEF = "1";
        String MARKETPLACE = "0";
    }
}
