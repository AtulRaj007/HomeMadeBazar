package com.homemadebazar.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.homemadebazar.R;
import com.homemadebazar.adapter.ViewPagerAdapter;
import com.homemadebazar.fragment.MarketPlaceFragment;
import com.homemadebazar.fragment.MyOrdersFragment;
import com.homemadebazar.fragment.MyShopFragment;
import com.homemadebazar.fragment.NavigationDrawerFragment;
import com.homemadebazar.fragment.SkillHubFragment;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.ServiceUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

public class HomeActivity extends BaseActivity {
    private static final String TAG = ">>>>>HomeActivity";
    private DrawerLayout mDrawerLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private int tabIcons[] = {R.drawable.ic_order_tab_unselect, R.drawable.ic_myshop_unselect, R.drawable.market_place_tab_unselect, R.drawable.skill_hub_unselected};
    private int tabIconsSelected[] = {R.drawable.ic_order_tab_select, R.drawable.ic_myshop_select, R.drawable.ic_market_place_tab_select, R.drawable.skill_hub_selected};
    private Toolbar mToolbar;
    private UserModel userModel;
    private NavigationDrawerFragment navigationDrawerFragment;
    private Handler handler = new Handler();
    private String titles[] = {"Order", "My Shop", "Market Place", "Skill Hub"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUpToolbar();
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        ServiceUtils.deviceLoginLogoutApiCall(HomeActivity.this, userModel.getUserId(), deviceToken, Constants.LoginHistory.LOGIN);
    }

    @Override
    protected void initUI() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);


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


    @Override
    protected void initialiseListener() {

    }

    @Override
    protected void setData() {
        setUpNavigationDrawer();
        setUpViewPager();
        setUpTabLayout();
        userModel = SharedPreference.getUserModel(HomeActivity.this);
        Log.e("UserModel:-", userModel.toString());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateNavigationProfile();
            }
        }, 1000);
    }

    private void updateNavigationProfile() {
        if (userModel != null && navigationDrawerFragment != null)
            navigationDrawerFragment.updateUI(userModel.getProfilePic(), userModel.getFirstName() + " " + userModel.getLastName(), userModel.getMobile());
    }

    private void setUpNavigationDrawer() {
        navigationDrawerFragment = new NavigationDrawerFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_drawer, navigationDrawerFragment);
        transaction.commit();
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

    private void setUpViewPager() {
        viewPagerAdapter.addFragment(new MyOrdersFragment(), "Orders");
        viewPagerAdapter.addFragment(new MyShopFragment(), "My Shop");
        viewPagerAdapter.addFragment(new MarketPlaceFragment(), "Market Place");
        viewPagerAdapter.addFragment(new SkillHubFragment(), "Skill Hub");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setUpIcons(position);
                Utils.hideSoftKeyboard(HomeActivity.this);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void onNavItemClick(View v) {
        Log.e(TAG, "Click:-" + v.getId());
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        Utils.onNavItemClick(HomeActivity.this, v, userModel.getUserId());
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("HomeMadeBazar");
        alertDialogBuilder.setMessage("Are you sure you want to exit...");
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e(TAG, "Id:-" + item.getItemId());
        switch (item.getItemId()) {
            case R.id.menu_notification:
                startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
                return true;
            case R.id.menu_second:
//                ((MarketPlaceFragment)viewPagerAdapter.getItem(2)).setGridLayout();
                return false;
            default:
                super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e(TAG, "====== onCreateOptionsMenu ======");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_home);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
        final MenuItem notificationMenuItem = menu.findItem(R.id.menu_notification);
        notificationMenuItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(notificationMenuItem);
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
