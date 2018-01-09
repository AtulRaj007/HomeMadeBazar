package com.homemadebazar.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.homemadebazar.R;
import com.homemadebazar.adapter.ViewPagerAdapter;
import com.homemadebazar.model.HomeChefSkillHubVideoModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.HomeChefIncomingOrderApiCall;
import com.homemadebazar.network.apicall.HomeChefSkillVideoApiCall;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

public class MyOrdersFragment extends BaseFragment {
    private static final String TAG = ">>>>>MyOrdersFragment";
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_orders, container, false);
    }

    public void initUI() {
        tabLayout = (TabLayout) getView().findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) getView().findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
    }

    @Override
    protected void initialiseListener() {

    }

    @Override
    protected void setData() {
        setUpViewPager();
        setHasOptionsMenu(true);
    }

    private void setUpViewPager() {
        viewPagerAdapter.addFragment(new NewMyOrdersFragment(), "Now");
        viewPagerAdapter.addFragment(new ScheduledMyOrdersFragment(), "Scheduled");
        mViewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e(TAG, "===== onCreateOptionsMenu =====");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e(TAG, "Id:-" + item.getItemId());
        return super.onOptionsItemSelected(item);

    }

//    http://35.183.8.236/api/CreateOrder/ShowOrderedList
//{"UserId":"17112218","TabSection":"NOW"}

}