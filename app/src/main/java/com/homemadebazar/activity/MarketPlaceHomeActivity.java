package com.homemadebazar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.homemadebazar.R;
import com.homemadebazar.adapter.ViewPagerAdapter;
import com.homemadebazar.fragment.MarketPlaceMyProductsFragment;
import com.homemadebazar.fragment.MarketPlaceMyShopFragment;
import com.homemadebazar.fragment.MarketPlaceOrdersFragment;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.ServiceUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place_home);
        setUpToolbar();
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        ServiceUtils.deviceLoginLogoutApiCall(MarketPlaceHomeActivity.this, userModel.getUserId(), deviceToken, Constants.LoginHistory.LOGIN);
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(MarketPlaceHomeActivity.this);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

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

    }

    @Override
    protected void setData() {
        setUpViewPager();
        setUpTabLayout();
        setUpIcons(0);
    }

    private void setUpToolbar() {
//        mToolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
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
        for (int i = 0; i < viewPagerAdapter.fragmentArrayList.size(); i++) {
            if (selectedPosition == i) {
                tabLayout.getTabAt(i).setIcon(tabIconsSelected[i]);

            } else {
                tabLayout.getTabAt(i).setIcon(tabIcons[i]);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e(TAG, "Id:-" + item.getItemId());
        switch (item.getItemId()) {
            case R.id.menu_notification:
                startActivity(new Intent(MarketPlaceHomeActivity.this, NotificationActivity.class));
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

    public void onNavItemClick(View v) {
        Log.e(TAG, "Click:-" + v.getId());
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        Utils.onNavItemClick(MarketPlaceHomeActivity.this, v, userModel.getUserId());
    /*    switch (v.getId()) {
            case R.id.iv_edit_profile:
                startActivity(new Intent(MarketPlaceHomeActivity.this, MyProfileActivity.class));
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_my_orders:
                startActivity(new Intent(MarketPlaceHomeActivity.this, MyOrdersActivity.class));
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_my_wallet:
                startActivity(new Intent(MarketPlaceHomeActivity.this, WalletActivity.class));
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_sales_report:
                Toast.makeText(MarketPlaceHomeActivity.this, "Development Mode", Toast.LENGTH_LONG).show();
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_transaction_history:
                startActivity(new Intent(MarketPlaceHomeActivity.this, TransactionHistoryActivity.class));
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_change_password:
                Intent intent = new Intent(MarketPlaceHomeActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_terms_of_use:
                Toast.makeText(MarketPlaceHomeActivity.this, "Development Mode", Toast.LENGTH_LONG).show();
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_privacy_policy:
                Toast.makeText(MarketPlaceHomeActivity.this, "Development Mode", Toast.LENGTH_LONG).show();
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_about:
                Toast.makeText(MarketPlaceHomeActivity.this, "Development Mode", Toast.LENGTH_LONG).show();
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_logout:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("HomeMadeBazar");
                alertDialogBuilder.setMessage("Are you sure you want to exit...");
                alertDialogBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                SharedPreference.clearSharedPreference(MarketPlaceHomeActivity.this);
                                startActivity(new Intent(MarketPlaceHomeActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialogBuilder.show();
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;

        }*/
    }
}
