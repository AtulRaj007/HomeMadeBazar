package com.homemadebazar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.activity.FoodieHomeActivity;
import com.homemadebazar.activity.HomeActivity;
import com.homemadebazar.activity.MarketPlaceHomeActivity;

/**
 * Created by HP on 7/29/2017.
 */

public class NavigationDrawerFragment extends BaseFragment implements View.OnClickListener {
    private static String TAG = ">>>>>NavigationFragment";
    private ImageView ivProfile;
    private TextView tvName, tvAddress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }


    public void initUI() {
        Log.e(TAG, "===== initUI() =====");
        ivProfile = getView().findViewById(R.id.iv_profile);
        tvName = getView().findViewById(R.id.tv_name);
        tvAddress = getView().findViewById(R.id.tv_address);
    }

    public void initialiseListener() {
        if (getView() != null) {
            getView().findViewById(R.id.iv_edit_profile).setOnClickListener(this);
            getView().findViewById(R.id.tv_my_orders).setOnClickListener(this);
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

    public void updateUI(String profilePath, String name, String address) {
        tvName.setText(name);
        tvAddress.setText(address);
    }

    public void setData() {

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
