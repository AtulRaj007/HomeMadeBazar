package com.homemadebazar.activity;

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
import com.homemadebazar.R;
import com.homemadebazar.adapter.ViewPagerAdapter;
import com.homemadebazar.fragment.FoodieDiscoverFragment;
import com.homemadebazar.fragment.FoodieFlashFragment;
import com.homemadebazar.fragment.FoodieHomeFragment;
import com.homemadebazar.fragment.FoodieMessengerFragment;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.ServiceUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

public class FoodieHomeActivity extends BaseActivity {
    private static final String TAG = ">>>>>FoodieHomeActivity";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar mToolbar;
    private UserModel userModel;
    private ViewPagerAdapter viewPagerAdapter;
    private NotificationReceiver notificationReceiver;
    private String titles[] = {"Home", "Discover", "Flash", "Messenger"};
    private int tabIcons[] = {R.drawable.ic_foodie_home_inactive, R.drawable.ic_discover_inactive, R.drawable.ic_discover_flash_inactive, R.drawable.ic_messenger_inactive};
    private int tabIconsSelected[] = {R.drawable.ic_foodie_home_active, R.drawable.ic_discover_active, R.drawable.ic_discover_flash_active, R.drawable.ic_messenger_active};
    private TextView tvNotificationCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodie_home);
        setUpToolbar();
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        ServiceUtils.deviceLoginLogoutApiCall(FoodieHomeActivity.this, userModel.getUserId(), deviceToken, Constants.LoginHistory.LOGIN);
        Utils.runAppWalkthrough(this, getFragmentManager(), userModel.getAccountType());
        Utils.setupUserToCrashAnalytics(userModel);

        notificationReceiver = new NotificationReceiver();
        Utils.registerNotificationReceiver(this, notificationReceiver);

    }


    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(FoodieHomeActivity.this);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        tvNotificationCount = findViewById(R.id.tv_notification_count);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        Constants.socialUserModel = null;
    }

    private void setUpTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        setUpIcons(0);
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
        viewPagerAdapter.addFragment(new FoodieHomeFragment(), "Home");
        viewPagerAdapter.addFragment(new FoodieDiscoverFragment(), "Discover");
        viewPagerAdapter.addFragment(new FoodieFlashFragment(), "Flash");
        viewPagerAdapter.addFragment(new FoodieMessengerFragment(), "Messenger");
        viewPager.setAdapter(viewPagerAdapter);
//        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setUpIcons(position);
                Utils.hideSoftKeyboard(FoodieHomeActivity.this);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.rl_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FoodieHomeActivity.this, NotificationActivity.class));
            }
        });
    }


    public void onNavItemClick(View v) {
        Log.e(TAG, "Click:-" + v.getId());
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        Utils.onNavItemClick(FoodieHomeActivity.this, v, userModel.getUserId());
    }

    @Override
    protected void setData() {
        setUpViewPager();
        setUpTabLayout();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing())
            Utils.unregisterNotificationReceiver(FoodieHomeActivity.this, notificationReceiver);
    }

    public class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("Notification Receiver");
            int count = SharedPreference.getIntegerPreference(FoodieHomeActivity.this, SharedPreference.NOTIFICATION_COUNT);
            tvNotificationCount.setText(count + "");
        }
    }
}
