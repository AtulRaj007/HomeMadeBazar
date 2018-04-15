package com.homemadebazar.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homemadebazar.R;
import com.homemadebazar.adapter.ProfileRecyclerAdapter;
import com.homemadebazar.adapter.ViewPagerAdapter;
import com.homemadebazar.fragment.UserRatingFragment;
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

import me.relex.circleindicator.CircleIndicator;

public class ProfileViewActivity extends BaseActivity implements View.OnClickListener {
    private static String KEY_USER_ID = "KEY_USER_ID";
    private TextView tvCommonInterest, tvName, tvEmailId, tvMobileNumber, tvCountry, tvProfessionType, tvProfessionName, tvAbout;
    private Button btnSendFriendRequest;
    private ImageView ivProfilePic;
    private UserModel userModel;
    private String profileUserId;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ProfileRecyclerAdapter profileRecyclerAdapter;
    private ArrayList<ProfileInterestsModel> profileInterestsModelArrayList = new ArrayList<>();
    private OtherUserProfileDetailsModel otherUserProfileDetailsModel;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private CircleIndicator circleIndicator;

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
        tvCommonInterest = findViewById(R.id.tv_common_interests);
        tvName = findViewById(R.id.tv_name);
        tvEmailId = findViewById(R.id.tv_emailId);
        tvMobileNumber = findViewById(R.id.tv_mobile_number);
        tvCountry = findViewById(R.id.tv_country);
        tvProfessionType = findViewById(R.id.tv_profession_type);
        tvProfessionName = findViewById(R.id.tv_profession_name);
        tvAbout = findViewById(R.id.tv_about);
        recyclerView = findViewById(R.id.recycler_view);
        btnSendFriendRequest = findViewById(R.id.btn_friend_request);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        viewPager = findViewById(R.id.view_pager);
        circleIndicator = findViewById(R.id.indicator);
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

    private void initialiseProfileInterests(String selectedInterests) {
        try {
            profileInterestsModelArrayList.clear();
            String[] interestsArray = {};
            if (!TextUtils.isEmpty(selectedInterests))
                interestsArray = selectedInterests.split(";");
            for (int i = 0; i < Constants.profileInterests.length; i++) {
                ProfileInterestsModel profileInterestsModel = new ProfileInterestsModel();
                try {
                    profileInterestsModel.setIconId(Integer.parseInt(Constants.profileInterests[i][0]));
                    profileInterestsModel.setInterestName(Constants.profileInterests[i][1]);
                    try {
                        if (interestsArray != null && interestsArray[i].equals("1"))
                            profileInterestsModel.setSelected(true);
                        else
                            profileInterestsModel.setSelected(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        profileInterestsModel.setSelected(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                profileInterestsModelArrayList.add(profileInterestsModel);
            }
            profileRecyclerAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getSimilarInterests(String otherUserInterests) {
        String commonInterestText = "";
        try {
            String ownInterest = userModel.getInterest();
            if (!TextUtils.isEmpty(ownInterest)) {
                String ownInterestArray[] = ownInterest.split(";");
                String otherInterestArray[] = otherUserInterests.split(";");
                for (int i = 0; i < ownInterestArray.length; i++) {
                    if (ownInterestArray[i].equals("1") && otherInterestArray[i].equals("1")) {
                        if (TextUtils.isEmpty(commonInterestText)) {
                            commonInterestText = Constants.profileInterests[i][1];
                        } else {
                            commonInterestText = commonInterestText + ", " + Constants.profileInterests[i][1];
                        }
                    }
                }

                if (!TextUtils.isEmpty(commonInterestText)) {
                    commonInterestText = "<font color='#D3D3D3'>You both enjoy </font>" + "<font color='#000000'>" + commonInterestText + "." + "</font>";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return commonInterestText;
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

    private void updateDetails(OtherUserProfileDetailsModel otherUserProfileDetailsModel) {
        if (!TextUtils.isEmpty(otherUserProfileDetailsModel.getProfilePic())) {
            Glide.with(ProfileViewActivity.this).load(otherUserProfileDetailsModel.getProfilePic()).into(ivProfilePic);
        }
        if (!TextUtils.isEmpty(getSimilarInterests(otherUserProfileDetailsModel.getInterest())))
            tvCommonInterest.setText(Html.fromHtml(getSimilarInterests(otherUserProfileDetailsModel.getInterest())));
        else
            tvCommonInterest.setVisibility(View.GONE);

        try {
            if (otherUserProfileDetailsModel.getFriendRequestNumeric() == Constants.RequestType.FRIEND) {
                findViewById(R.id.ll_mobile_number).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.ll_mobile_number).setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        tvName.setText(otherUserProfileDetailsModel.getFirstName() + " " + otherUserProfileDetailsModel.getLastName());
        tvEmailId.setText(otherUserProfileDetailsModel.getEmailId());
        tvMobileNumber.setText(otherUserProfileDetailsModel.getCountryCode() + otherUserProfileDetailsModel.getMobile());
        tvCountry.setText(otherUserProfileDetailsModel.getCountryName());
        try {
            tvProfessionType.setText(getResources().getStringArray(R.array.profession)[Integer.parseInt(otherUserProfileDetailsModel.getProfessionType())]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvProfessionName.setText(otherUserProfileDetailsModel.getProfessionName());
        tvAbout.setText(otherUserProfileDetailsModel.getDpStatus());
        initialiseProfileInterests(otherUserProfileDetailsModel.getInterest());
        if (otherUserProfileDetailsModel.getAccountType().equals(Constants.Role.FOODIE.getStringRole()) && !userModel.getUserId().trim().equals(profileUserId.trim())) {
            btnSendFriendRequest.setVisibility(View.VISIBLE);
        } else {
            btnSendFriendRequest.setVisibility(View.GONE);
        }

        if (otherUserProfileDetailsModel.getRatingModelArrayList().size() > 0) {
            findViewById(R.id.tv_top_reviews).setVisibility(View.VISIBLE);
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            for (int i = 0; i < otherUserProfileDetailsModel.getRatingModelArrayList().size(); i++) {
                viewPagerAdapter.addFragment(UserRatingFragment.instantiateFragment(otherUserProfileDetailsModel.getRatingModelArrayList().get(i)), "");
            }
            viewPager.setAdapter(viewPagerAdapter);
            circleIndicator.setViewPager(viewPager);
        } else {
            findViewById(R.id.rl_view_pager).setVisibility(View.GONE);
        }

        btnSendFriendRequest.setText(Constants.RequestString[otherUserProfileDetailsModel.getFriendRequestNumeric()]);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_friend_request:
                try {
                    if (otherUserProfileDetailsModel != null) {
                        int numericStatus = otherUserProfileDetailsModel.getFriendRequestNumeric();
                        if (numericStatus == Constants.RequestType.REQUEST_NOT_SENT || numericStatus == Constants.RequestType.UNFRIEND)
                            joinParticipateApiCall(userModel.getUserId(), profileUserId);
                    }
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
