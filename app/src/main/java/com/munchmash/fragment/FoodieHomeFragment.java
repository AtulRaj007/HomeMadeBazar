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
import com.munchmash.model.UserModel;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

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
        viewPagerAdapter.addFragment(new FoodieNearMeFragment(), "Near Me");
        viewPagerAdapter.addFragment(new FoodieMyCityFragment(), "My City");
        viewPagerAdapter.addFragment(new FoodieFavouritesFragment(), "Favourites");

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
