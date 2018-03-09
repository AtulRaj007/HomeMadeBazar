package com.homemadebazar.fragment;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.homemadebazar.R;
import com.homemadebazar.adapter.ImageLocalPagerAdapter;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.SharedPreference;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Atul on 2/19/18.
 */

public class AppWalkthroughFragment extends DialogFragment {
    private ViewPager viewPager;
    private int homeChefImageArrayList[] = {R.drawable.homechef_order, R.drawable.homechef_myshop, R.drawable.homechef_marketplace, R.drawable.homechef_skillhub};
    private int foodieImageArrayList[] = {R.drawable.foodie_home, R.drawable.foodie_discover, R.drawable.foodie_flash, R.drawable.foodie_messenger};
    private int marketplaceImageArrayList[] = {R.drawable.marketplace_orders, R.drawable.marketplace_myshop, R.drawable.marketplace_myproducts};
    private CircleIndicator circleIndicator;
    private UserModel userModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        userModel = SharedPreference.getUserModel(getActivity());
        View view = inflater.inflate(R.layout.fragment_appwalkthrough, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        try {
            circleIndicator = getView().findViewById(R.id.indicator);
            viewPager = getView().findViewById(R.id.view_pager);
            getView().findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            ImageLocalPagerAdapter imagePagerAdapter = null;
            if (userModel.getAccountType().equals(Constants.AccountType.HOME_CHEF))
                imagePagerAdapter = new ImageLocalPagerAdapter(getActivity(), homeChefImageArrayList);
            else if (userModel.getAccountType().equals(Constants.AccountType.FOODIE))
                imagePagerAdapter = new ImageLocalPagerAdapter(getActivity(), foodieImageArrayList);
            else if (userModel.getAccountType().equals(Constants.AccountType.MARKET_PLACE))
                imagePagerAdapter = new ImageLocalPagerAdapter(getActivity(), marketplaceImageArrayList);
            viewPager.setAdapter(imagePagerAdapter);
            circleIndicator.setViewPager(viewPager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
