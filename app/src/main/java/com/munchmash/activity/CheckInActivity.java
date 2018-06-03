package com.munchmash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.munchmash.R;
import com.munchmash.adapter.FoodieCheckInAdapter;
import com.munchmash.model.BaseModel;
import com.munchmash.model.FoodieCheckInModel;
import com.munchmash.model.UserLocation;
import com.munchmash.model.UserModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.FoodieCheckInApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

import java.util.ArrayList;

public class CheckInActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private UserModel userModel;
    private UserLocation location;
    private ArrayList<FoodieCheckInModel> foodieCheckInModelArrayList = new ArrayList<>();
    private FoodieCheckInAdapter foodieCheckInAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        setUpToolbar();
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(CheckInActivity.this);
        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        location = SharedPreference.getUserLocation(CheckInActivity.this);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    protected void initialiseListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void setData() {
        recyclerView.setLayoutManager(linearLayoutManager);
        foodieCheckInAdapter = new FoodieCheckInAdapter(CheckInActivity.this, foodieCheckInModelArrayList);
        recyclerView.setAdapter(foodieCheckInAdapter);
        swipeRefreshLayout.setRefreshing(true);
        getCheckInDetails();
    }


    private void getCheckInDetails() {
        try {

            final FoodieCheckInApiCall apiCall = new FoodieCheckInApiCall(userModel.getUserId(), location.getLatitude(), location.getLongitude());
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    if (e == null) { // Success
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                ArrayList<FoodieCheckInModel> checkInModelArrayList = apiCall.getResult();
                                foodieCheckInModelArrayList.clear();
                                foodieCheckInModelArrayList.addAll(checkInModelArrayList);
                                foodieCheckInAdapter.notifyDataSetChanged();
                            } else if (baseModel.getStatusCode() == Constants.ServerResponseCode.NO_RECORD_FOUND) {
                                findViewById(R.id.tv_no_record_found).setVisibility(View.VISIBLE);
                            } else {
                                DialogUtils.showAlert(CheckInActivity.this, baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), CheckInActivity.this, null);
                    }
                }
            }, false);
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), CheckInActivity.this, null);
        }
    }

    public void onCheckInSelected(int position) {
        Intent intent = new Intent();
        intent.putExtra(Constants.BundleKeys.CHECK_IN_MODEL, foodieCheckInModelArrayList.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setUpToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("Check In");
    }

    @Override
    public void onRefresh() {
        getCheckInDetails();
    }
}
