package com.munchmash.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.munchmash.R;
import com.munchmash.adapter.FoodieDiscoverAdapter;
import com.munchmash.adapter.ViewPagerAdapter;
import com.munchmash.model.HomeChefOrderModel;
import com.munchmash.model.UserModel;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

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
        String strMealsUnder = "Meals Under" + "\n";
        if (userModel.getCountryCode().equals("91")) {
            strMealsUnder += "100 " + Utils.getRupeesSymbol();
        } else if (userModel.getCountryCode().equals("44")) {
            strMealsUnder += "5 " + Utils.getPoundSymbol();
        } else {
            strMealsUnder += "5 " + Utils.getDollarSymbol();
        }

        viewPagerAdapter.addFragment(new FoodieTopDealsFragment(), "Top Deals");
        viewPagerAdapter.addFragment(new FoodieMealsUnderPriceFragment(), strMealsUnder);
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
