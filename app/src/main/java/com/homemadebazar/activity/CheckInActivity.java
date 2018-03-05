package com.homemadebazar.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.adapter.FoodieCheckInAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.FoodieCheckInModel;
import com.homemadebazar.model.UserLocation;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.FoodieCheckInApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

public class CheckInActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private UserModel userModel;
    private UserLocation location;
    private ArrayList<FoodieCheckInModel> foodieCheckInModelArrayList = new ArrayList<>();
    private FoodieCheckInAdapter foodieCheckInAdapter;

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
    }

    @Override
    protected void initialiseListener() {

    }

    @Override
    protected void setData() {
        recyclerView.setLayoutManager(linearLayoutManager);
        foodieCheckInAdapter = new FoodieCheckInAdapter(CheckInActivity.this, foodieCheckInModelArrayList);
        recyclerView.setAdapter(foodieCheckInAdapter);
        getCheckInDetails();
    }


    private void getCheckInDetails() {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(CheckInActivity.this, null);
            progressDialog.show();

            final FoodieCheckInApiCall apiCall = new FoodieCheckInApiCall(userModel.getUserId(), location.getLatitude(), location.getLongitude());
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    if (e == null) { // Success
                        DialogUtils.hideProgressDialog(progressDialog);
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
}
