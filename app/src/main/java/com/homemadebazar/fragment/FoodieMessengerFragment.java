package com.homemadebazar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homemadebazar.R;
import com.homemadebazar.adapter.ViewPagerAdapter;

/**
 * Created by atulraj on 22/11/17.
 */

public class FoodieMessengerFragment extends BaseFragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_foodie_messenger,container,false);
        return view;
    }

    @Override
    protected void initUI() {
        viewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager());
        tabLayout=getView().findViewById(R.id.tab_layout);
        viewPager=getView().findViewById(R.id.view_pager);
    }

    @Override
    protected void initialiseListener() {

    }

    @Override
    protected void setData() {
        setUpViewPager();
        setUpTabLayout();

    }

    private void setUpViewPager(){
        viewPagerAdapter.addFragment(new FoodieMessengerFriendsFragment(),"Friends");
        viewPagerAdapter.addFragment(new FoodieMessengerRequestFragment(),"Request");
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

    private void setUpTabLayout(){
        tabLayout.setupWithViewPager(viewPager);
    }
}
