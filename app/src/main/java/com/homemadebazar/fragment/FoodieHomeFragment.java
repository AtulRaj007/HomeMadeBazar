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
import com.homemadebazar.model.UserModel;
import com.homemadebazar.util.SharedPreference;

/**
 * Created by atulraj on 22/11/17.
 */

public class FoodieHomeFragment extends BaseFragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private UserModel userModel;
//    private FloatingActionButton fabSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foodie_home, container, false);
    }

    @Override
    protected void initUI() {
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        tabLayout = getView().findViewById(R.id.tab_layout);
        viewPager = getView().findViewById(R.id.view_pager);
//        fabSearch = getView().findViewById(R.id.fab_search);

    }

    @Override
    protected void initialiseListener() {
        userModel = SharedPreference.getUserModel(getActivity());
    }

    @Override
    protected void setData() {
        setUpViewPager();
        setUpTabLayout();
    }

    private void setUpViewPager() {
        viewPagerAdapter.addFragment(new FoodieHomeChefSearchFragment(), "Home");
        viewPagerAdapter.addFragment(new FoodieHomeListFragment(), "List");
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void setUpTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
    }

}
