package com.munchmash.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.munchmash.R;
import com.munchmash.adapter.ViewPagerAdapter;


/**
 * Created by atulraj on 23/11/17.
 */

public class MarketPlaceOrdersFragment extends BaseFragment {
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market_place_orders, container, false);
        return view;
    }

    @Override
    protected void initUI() {
        viewPager = getView().findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        tabLayout = getView().findViewById(R.id.tab_layout);


    }

    @Override
    protected void initialiseListener() {

    }

    @Override
    protected void setData() {
        setUpViewPager();
        setUpTabLayout();

    }

    private void setUpViewPager() {
        viewPagerAdapter.addFragment(new MarketPlaceIncomingOrdersFragment(), "Incoming Orders");
        viewPagerAdapter.addFragment(new MarketPlaceOutgoingOrdersFragment(), "Outgoing Orders");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(viewPagerAdapter);

    }

    private void setUpTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
    }
}
