package com.homemadebazar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homemadebazar.R;
import com.homemadebazar.activity.FoodieHomeActivity;
import com.homemadebazar.activity.HomeActivity;
import com.homemadebazar.activity.MarketPlaceHomeActivity;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.SharedPreference;

/**
 * Created by HP on 7/29/2017.
 */

public class NavigationDrawerFragment extends BaseFragment implements View.OnClickListener {
    private static String TAG = ">>>>>NavigationFragment";
    private TextView tvName, tvMobileNumber;
    private ImageView ivProfilePic;
    private UserModel userModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }


    public void initUI() {
        Log.e(TAG, "===== initUI() =====");
        userModel = SharedPreference.getUserModel(getActivity());
        ivProfilePic = getView().findViewById(R.id.iv_profile);
        tvName = getView().findViewById(R.id.tv_name);
        tvMobileNumber = getView().findViewById(R.id.tv_mobile_number);

//        if (userModel.getAccountType().equals(Constants.AccountType.FOODIE)) {
//            getView().findViewById(R.id.ll_sales_report).setVisibility(View.GONE);
//            getView().findViewById(R.id.view_sales_report).setVisibility(View.GONE);
//        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println(">>>>> onActivityCreated.........");
    }

    public void initialiseListener() {
        if (getView() != null) {
            getView().findViewById(R.id.rl_header).setOnClickListener(this);
            getView().findViewById(R.id.tv_my_orders).setOnClickListener(this);
            getView().findViewById(R.id.tv_marketplace_orders).setOnClickListener(this);
            getView().findViewById(R.id.tv_my_wallet).setOnClickListener(this);
            getView().findViewById(R.id.tv_sales_report).setOnClickListener(this);
            getView().findViewById(R.id.tv_transaction_history).setOnClickListener(this);
            getView().findViewById(R.id.tv_change_password).setOnClickListener(this);
            getView().findViewById(R.id.tv_terms_of_use).setOnClickListener(this);
            getView().findViewById(R.id.tv_privacy_policy).setOnClickListener(this);
            getView().findViewById(R.id.tv_about).setOnClickListener(this);
            getView().findViewById(R.id.tv_logout).setOnClickListener(this);
        }

    }

    public void updateUI(String profilePath, String name, String mobileNumber) {
        tvName.setText(name);
        tvMobileNumber.setText(mobileNumber);
    }

    public void setData() {

        if (userModel.getAccountType().equals(Constants.Role.FOODIE.getStringRole())) {
            getView().findViewById(R.id.ll_market_place_ordres).setVisibility(View.GONE);
            getView().findViewById(R.id.view_marketplace).setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(userModel.getProfilePic())) {
            Glide.with(getActivity()).load(userModel.getProfilePic()).into(ivProfilePic);
        }
        tvName.setText(userModel.getFirstName() + " " + userModel.getLastName());
        tvMobileNumber.setText(userModel.getCountryCode() + userModel.getMobile());
    }

    @Override
    public void onClick(View view) {
        if (getActivity() instanceof HomeActivity)
            ((HomeActivity) getActivity()).onNavItemClick(view);
        else if (getActivity() instanceof FoodieHomeActivity)
            ((FoodieHomeActivity) getActivity()).onNavItemClick(view);
        else if (getActivity() instanceof MarketPlaceHomeActivity)
            ((MarketPlaceHomeActivity) getActivity()).onNavItemClick(view);
    }
}
