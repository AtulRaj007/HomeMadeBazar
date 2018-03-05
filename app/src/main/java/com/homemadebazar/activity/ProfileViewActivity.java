package com.homemadebazar.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homemadebazar.R;
import com.homemadebazar.adapter.ProfileRecyclerAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.OtherUserProfileDetailsModel;
import com.homemadebazar.model.ProfileInterestsModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.GetUserProfileDetailsApiCall;
import com.homemadebazar.network.apicall.MessengerJoinSendRequestApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

public class ProfileViewActivity extends BaseActivity implements View.OnClickListener {
    private static String KEY_USER_ID = "KEY_USER_ID";
    private TextView tvName, tvEmailId, tvMobileNumber, tvCountry, tvAbout;
    private Button btnSendFriendRequest;
    private ImageView ivProfilePic;
    private UserModel userModel;
    private String profileUserId;
    private Spinner sprProfession;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ProfileRecyclerAdapter profileRecyclerAdapter;
    private ArrayList<ProfileInterestsModel> profileInterestsModelArrayList = new ArrayList<>();
    private OtherUserProfileDetailsModel otherUserProfileDetailsModel;

    public static Intent getProfileIntent(Context context, String userId) {
        Intent intent = new Intent(context, ProfileViewActivity.class);
        intent.putExtra(KEY_USER_ID, userId);
        return intent;
    }

    private void getDataFromBundle() {
        profileUserId = getIntent().getStringExtra(KEY_USER_ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
    }

    @Override
    protected void initUI() {
        getDataFromBundle();
        userModel = SharedPreference.getUserModel(ProfileViewActivity.this);
        ivProfilePic = findViewById(R.id.iv_profile_pic);
        tvName = findViewById(R.id.tv_name);
        tvEmailId = findViewById(R.id.tv_emailId);
        tvMobileNumber = findViewById(R.id.tv_mobile_number);
        tvCountry = findViewById(R.id.tv_country);
        tvAbout = findViewById(R.id.tv_about);
        sprProfession = findViewById(R.id.spr_profession);
        recyclerView = findViewById(R.id.recycler_view);
        btnSendFriendRequest = findViewById(R.id.btn_friend_request);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSendFriendRequest.setOnClickListener(this);
    }

    @Override
    protected void setData() {
        initialiseProfileInterests();
        recyclerView.setLayoutManager(linearLayoutManager);
        profileRecyclerAdapter = new ProfileRecyclerAdapter(this, profileInterestsModelArrayList, false);
        recyclerView.setAdapter(profileRecyclerAdapter);
        getUserProfileDetails();
    }

    private void initialiseProfileInterests() {
        profileInterestsModelArrayList.clear();
        for (int i = 0; i < Constants.profileInterests.length; i++) {
            ProfileInterestsModel profileInterestsModel = new ProfileInterestsModel();
            try {
                profileInterestsModel.setIconId(Integer.parseInt(Constants.profileInterests[i][0]));
                profileInterestsModel.setInterestName(Constants.profileInterests[i][1]);
                profileInterestsModel.setSelected(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            profileInterestsModelArrayList.add(profileInterestsModel);
        }
    }

    public void getUserProfileDetails() {
        try {
            final GetUserProfileDetailsApiCall apiCall = new GetUserProfileDetailsApiCall(userModel.getUserId(), profileUserId);
            HttpRequestHandler.getInstance(this).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                otherUserProfileDetailsModel = apiCall.getResult();
                                updateDetails(otherUserProfileDetailsModel);
                            } else {
                                DialogUtils.showAlert(ProfileViewActivity.this, baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), ProfileViewActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), ProfileViewActivity.this, null);
        }
    }

    private void updateDetails(OtherUserProfileDetailsModel userModel) {
        if (!TextUtils.isEmpty(userModel.getProfilePic())) {
            Glide.with(ProfileViewActivity.this).load(userModel.getProfilePic()).into(ivProfilePic);
        }
        tvName.setText(userModel.getFirstName() + " " + userModel.getLastName());
        tvEmailId.setText(userModel.getEmailId());
        tvMobileNumber.setText(userModel.getMobile());
        tvCountry.setText(userModel.getCountryName());
        tvAbout.setText(userModel.getDpStatus());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_friend_request:
                try {
                    if (otherUserProfileDetailsModel != null)
                        joinParticipateApiCall(userModel.getUserId(), profileUserId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void joinParticipateApiCall(String fromUserId, String toReqUserId) {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(ProfileViewActivity.this, null);
            progressDialog.show();

            final MessengerJoinSendRequestApiCall apiCall = new MessengerJoinSendRequestApiCall(fromUserId, toReqUserId);
            HttpRequestHandler.getInstance(ProfileViewActivity.this).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    progressDialog.hide();
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                DialogUtils.showAlert(ProfileViewActivity.this, "Friend Request has been sent.");

                            } else {
                                DialogUtils.showAlert(ProfileViewActivity.this, baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), ProfileViewActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), ProfileViewActivity.this, null);
        }
    }
}
