package com.homemadebazar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homemadebazar.R;
import com.homemadebazar.adapter.FoodieDiscoverAdapter;
import com.homemadebazar.adapter.ViewPagerAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChefOrderModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.GetListOfHotDealsApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

/**
 * Created by atulraj on 22/11/17.
 */

public class FoodieDiscoverFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FoodieDiscoverAdapter foodieDiscoverAdapter;
    private ArrayList<HomeChefOrderModel> homeChefOrderModelArrayList = new ArrayList<>();
    private UserModel userModel;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
//    private HomeChefFoodTimingAdapter homeChefLunchAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foodie_discover, container, false);
        return view;
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(getActivity());
        viewPager = getView().findViewById(R.id.view_pager);
        tabLayout = getView().findViewById(R.id.tab_layout);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
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
        viewPagerAdapter.addFragment(new FoodieTopDealsFragment(), "Top Deals");
        viewPagerAdapter.addFragment(new FoodieMealsUnderPriceFragment(), "Meals Under 100");
        viewPagerAdapter.addFragment(new FoodieTopChefFragment(), "Top Chefs");

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Utils.hideSoftKeyboard(getActivity());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setUpTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
    }


}
