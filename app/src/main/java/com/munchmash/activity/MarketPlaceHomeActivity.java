package com.munchmash.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.munchmash.R;
import com.munchmash.adapter.ViewPagerAdapter;
import com.munchmash.fragment.MarketPlaceMyProductsFragment;
import com.munchmash.fragment.MarketPlaceMyShopFragment;
import com.munchmash.fragment.MarketPlaceOrdersFragment;
import com.munchmash.model.UserModel;
import com.munchmash.util.Constants;
import com.munchmash.util.ServiceUtils;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

public class MarketPlaceHomeActivity extends BaseActivity {
    private static final String TAG = ">>>>> MarketPlace >>";
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private int tabIcons[] = {R.drawable.ic_marketplace_order_unselected, R.drawable.ic_marketplace_myshop_unselect, R.drawable.ic_marketplace_myproduct_unselect};
    private int tabIconsSelected[] = {R.drawable.ic_marketplace_order, R.drawable.ic_marketplace_myshop, R.drawable.ic_marketplace_myproduct};
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private UserModel userModel;
    private Toolbar mToolbar;
    private String[] titles = {"Orders", "My Shop", "My Products"};
    private NotificationReceiver notificationReceiver;
    private TextView tvNotificationCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place_home);
        setUpToolbar();
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        ServiceUtils.deviceLoginLogoutApiCall(MarketPlaceHomeActivity.this, userModel.getUserId(), deviceToken, Constants.LoginHistory.LOGIN);
        Utils.runAppWalkthrough(this, getFragmentManager(), userModel.getAccountType());
        Utils.setupUserToCrashAnalytics(userModel);

        notificationReceiver = new NotificationReceiver();
        Utils.registerNotificationReceiver(this, notificationReceiver);
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(MarketPlaceHomeActivity.this);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        tvNotificationCount=findViewById(R.id.tv_notification_count);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.rl_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MarketPlaceHomeActivity.this, NotificationActivity.class));
            }
        });
    }

    @Override
    protected void setData() {
        setUpViewPager();
        setUpTabLayout();
        setUpIcons(0);
    }

    private void setUpToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        findViewById(R.id.menu_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.menu_notification:
                        mDrawerLayout.openDrawer(Gravity.LEFT);
                        break;
                }
            }
        });
    }


    private void setUpViewPager() {
        viewPagerAdapter.addFragment(new MarketPlaceOrdersFragment(), "Orders");
        viewPagerAdapter.addFragment(new MarketPlaceMyShopFragment(), "My Shop");
        viewPagerAdapter.addFragment(new MarketPlaceMyProductsFragment(), "My Products");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setUpIcons(position);
                Utils.hideSoftKeyboard(MarketPlaceHomeActivity.this);
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

    private void setUpIcons(int selectedPosition) {
        ((TextView) findViewById(R.id.tv_toolbar_title)).setText(titles[selectedPosition]);
        for (int i = 0; i < viewPagerAdapter.fragmentArrayList.size(); i++) {
            if (selectedPosition == i) {
                tabLayout.getTabAt(i).setIcon(tabIconsSelected[i]);

            } else {
                tabLayout.getTabAt(i).setIcon(tabIcons[i]);
            }
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.e(TAG, "Id:-" + item.getItemId());
//        switch (item.getItemId()) {
//            case R.id.menu_notification:
//                startActivity(new Intent(MarketPlaceHomeActivity.this, NotificationActivity.class));
//                return true;
//            case R.id.menu_second:
////                ((MarketPlaceFragment)viewPagerAdapter.getItem(2)).setGridLayout();
//                return false;
//            default:
//                super.onOptionsItemSelected(item);
//        }
//        return false;
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        Log.e(TAG, "====== onCreateOptionsMenu ======");
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.inflateMenu(R.menu.menu_home);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                return onOptionsItemSelected(item);
//            }
//        });
//        final MenuItem notificationMenuItem = menu.findItem(R.id.menu_notification);
//        notificationMenuItem.getActionView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onOptionsItemSelected(notificationMenuItem);
//            }
//        });
//        return true;
//    }

    public void onNavItemClick(View v) {
        Log.e(TAG, "Click:-" + v.getId());
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        Utils.onNavItemClick(MarketPlaceHomeActivity.this, v, userModel.getUserId());
    }

    public class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("Notification Receiver");
            int count = SharedPreference.getIntegerPreference(MarketPlaceHomeActivity.this, SharedPreference.NOTIFICATION_COUNT);
            tvNotificationCount.setText(count + "");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing())
            Utils.unregisterNotificationReceiver(MarketPlaceHomeActivity.this, notificationReceiver);
    }
}
