package com.homemadebazar.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.adapter.ViewPagerAdapter;
import com.homemadebazar.fragment.MarketPlaceManageBrandFragment;
import com.homemadebazar.fragment.MarketPlaceManageCategoryFragment;

public class MarketPlaceManageActivity extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place_manage);
        setupToolbar();
    }

    @Override
    protected void initUI() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

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
        viewPagerAdapter.addFragment(new MarketPlaceManageCategoryFragment(), "Category");
        viewPagerAdapter.addFragment(new MarketPlaceManageBrandFragment(), "Brand");

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

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("Manage");

    }

    private void setUpTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
    }
}
