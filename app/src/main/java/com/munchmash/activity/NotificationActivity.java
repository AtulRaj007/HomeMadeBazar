package com.munchmash.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.munchmash.R;
import com.munchmash.adapter.NotificationAdapter;
import com.munchmash.model.BaseModel;
import com.munchmash.model.NotificationModel;
import com.munchmash.model.UserModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.GetNotificationApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

import java.util.ArrayList;

public class NotificationActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
    private UserModel userModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setupToolbar();
        SharedPreference.setIntegerPreference(this, SharedPreference.NOTIFICATION_COUNT, 0);
        Utils.sendLocalNotificationCount(NotificationActivity.this);
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(NotificationActivity.this);
        recyclerView = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    protected void initialiseListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void setData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        notificationAdapter = new NotificationAdapter(this, notificationModelArrayList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(notificationAdapter);
        swipeRefreshLayout.setRefreshing(true);
        getNotificationsApiCall();
    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("NOTIFICATION");

    }

    private void getNotificationsApiCall() {
        try {
            final GetNotificationApiCall apiCall = new GetNotificationApiCall(userModel.getUserId());
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                findViewById(R.id.tv_no_record_found).setVisibility(View.GONE);
                                notificationModelArrayList.clear();
                                notificationModelArrayList.addAll(apiCall.getResult());
                                notificationAdapter.notifyDataSetChanged();
                            } else if (baseModel.getStatusCode() == Constants.ServerResponseCode.NO_RECORD_FOUND) {
                                notificationModelArrayList.clear();
                                notificationAdapter.notifyDataSetChanged();
                                findViewById(R.id.tv_no_record_found).setVisibility(View.VISIBLE);
                            } else {
                                DialogUtils.showAlert(NotificationActivity.this, baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), NotificationActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), NotificationActivity.this, null);
        }
    }

    @Override
    public void onRefresh() {
        getNotificationsApiCall();
    }
}
