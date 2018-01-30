package com.homemadebazar.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homemadebazar.R;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.OtherUserProfileDetailsModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.GetUserProfileDetailsApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

/**
 * Created by Atul on 1/29/18.
 */

public class UserProfileFragment extends DialogFragment {
    private UserModel userModel;
    private ImageView ivProfilePic;
    private TextView tvName, tvEmailId, tvMobileNumber, tvAboutYourSelf;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        return view;
    }

    private void initUI() {
        userModel = SharedPreference.getUserModel(getActivity());
        tvName = getView().findViewById(R.id.tv_name);
        tvEmailId = getView().findViewById(R.id.tv_emailId);
        tvMobileNumber = getView().findViewById(R.id.tv_mobile_number);
        tvAboutYourSelf = getView().findViewById(R.id.tv_about_your_self);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();
        getUserProfileDetails();
    }

    public void getUserProfileDetails() {
        try {
            final GetUserProfileDetailsApiCall apiCall = new GetUserProfileDetailsApiCall(userModel.getUserId(), userModel.getUserId());
            HttpRequestHandler.getInstance(getActivity()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                OtherUserProfileDetailsModel userModel = apiCall.getResult();
                                updateDetails(userModel);
                            } else {
                                DialogUtils.showAlert(getActivity(), baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), getActivity(), null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), getActivity(), null);
        }
    }

    private void updateDetails(OtherUserProfileDetailsModel userModel) {
        if (TextUtils.isEmpty(userModel.getProfilePic())) {
            Glide.with(getActivity()).load(userModel.getProfilePic()).into(ivProfilePic);
        }
        tvName.setText(userModel.getFirstName() + " " + userModel.getLastName());
        tvEmailId.setText(userModel.getEmailId());
        tvMobileNumber.setText(userModel.getMobile());
        tvAboutYourSelf.setText(userModel.getDpStatus());
    }
}
