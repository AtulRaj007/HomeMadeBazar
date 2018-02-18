package com.homemadebazar.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.adapter.MessengerInviteParticipatesRecyclerAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.MessegeInviteParticipateModel;
import com.homemadebazar.model.UserLocation;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.MessengerInviteParticipateApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

/**
 * Created by sonu on 12/9/2017.
 */

public class FoodieMessengerInviteParticipateActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private UserModel userModel;
    private ArrayList<MessegeInviteParticipateModel> dataList;
    private TextView toolbarTitle;
    private UserLocation userLocation;
    private MessengerInviteParticipatesRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_invite_participates);
        getInviteParticipateListApiCall();
    }

    @Override
    protected void initUI() {
        userLocation = SharedPreference.getUserLocation(this);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.participates_recycler_view);
    }

    @Override
    protected void initialiseListener() {
        dataList = new ArrayList<>();
        userModel = SharedPreference.getUserModel(this);
    }

    @Override
    protected void setData() {
        setUpToolbar();
        setRecyclerAdapter();
    }

    private void setRecyclerAdapter() {
        adapter = new MessengerInviteParticipatesRecyclerAdapter(this, dataList, userModel.getUserId());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle = findViewById(R.id.tv_title);
        toolbarTitle.setText("Invite");
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void getInviteParticipateListApiCall() {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(this, null);
            progressDialog.show();

            final MessengerInviteParticipateApiCall apiCall = new MessengerInviteParticipateApiCall(userModel.getUserId(), userLocation.getLatitude(), userLocation.getLongitude());
            HttpRequestHandler.getInstance(this).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    progressDialog.hide();
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                // DialogUtils.showAlert(getActivity(), "HomeChiefDetailList size:-" + apiCall.getHomeChiefDetailList().size());
                                dataList.clear();
                                dataList.addAll(apiCall.getData());
                                setRecyclerAdapter();
                            } else {
                                DialogUtils.showAlert(FoodieMessengerInviteParticipateActivity.this, baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), FoodieMessengerInviteParticipateActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), FoodieMessengerInviteParticipateActivity.this, null);
        }
    }
}
