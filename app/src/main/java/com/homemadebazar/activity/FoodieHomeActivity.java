package com.homemadebazar.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.homemadebazar.R;
import com.homemadebazar.adapter.ViewPagerAdapter;
import com.homemadebazar.fragment.FoodieDiscoverFragment;
import com.homemadebazar.fragment.FoodieFlashFragment;
import com.homemadebazar.fragment.FoodieHomeFragment;
import com.homemadebazar.fragment.FoodieMessengerFragment;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.DeviceLoginLogoutApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
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
    private String titles[] = {"Home", "Discover", "Flash", "Messenger"};
    private int tabIcons[] = {R.drawable.ic_foodie_home_inactive, R.drawable.ic_discover_inactive, R.drawable.ic_discover_flash_inactive, R.drawable.ic_messenger_inactive};
    private int tabIconsSelected[] = {R.drawable.ic_foodie_home_active, R.drawable.ic_discover_active, R.drawable.ic_discover_flash_active, R.drawable.ic_messenger_active};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodie_home);
        UserModel userModel = SharedPreference.getUserModel(this);
        if (userModel != null)
            Log.d("Foodie user id", userModel.getUserId());
    }

    @Override
    protected void initUI() {
        userModel=SharedPreference.getUserModel(FoodieHomeActivity.this);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
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
        findViewById(R.id.menu_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.menu_first:
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
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.e(TAG, "onPageScrolled:-" + position);
            }

            @Override
            public void onPageSelected(int position) {
//                Log.e(TAG, "onPageSelected:-" + position);
                setUpIcons(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initialiseListener() {

    }

    public void deviceLoginLogoutApiCall(String userId, String token, final int loginHistory) {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(this, null);
            if (loginHistory == Constants.LoginHistory.LOGOUT) {
                progressDialog.show();
            }

            final DeviceLoginLogoutApiCall apiCall = new DeviceLoginLogoutApiCall(userId, token, loginHistory);
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    if (loginHistory == Constants.LoginHistory.LOGOUT)
                        DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                if (loginHistory == Constants.LoginHistory.LOGOUT)
                                    Toast.makeText(FoodieHomeActivity.this, baseModel.getStatusMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                DialogUtils.showAlert(FoodieHomeActivity.this, baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), FoodieHomeActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), FoodieHomeActivity.this, null);
        }
    }

    public void onNavItemClick(View v) {
        Log.e(TAG, "Click:-" + v.getId());
        switch (v.getId()) {
            case R.id.iv_edit_profile:
                startActivity(new Intent(FoodieHomeActivity.this, MyProfileActivity.class));
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_my_orders:
                startActivity(new Intent(FoodieHomeActivity.this, MyOrdersActivity.class));
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_my_wallet:
                startActivity(new Intent(FoodieHomeActivity.this, WalletActivity.class));
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_sales_report:
                Toast.makeText(FoodieHomeActivity.this, "Development Mode", Toast.LENGTH_LONG).show();
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_transaction_history:
                startActivity(new Intent(FoodieHomeActivity.this, TransactionHistoryActivity.class));
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_terms_of_use:
                Toast.makeText(FoodieHomeActivity.this, "Development Mode", Toast.LENGTH_LONG).show();
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_privacy_policy:
                Toast.makeText(FoodieHomeActivity.this, "Development Mode", Toast.LENGTH_LONG).show();
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_about:
                Toast.makeText(FoodieHomeActivity.this, "Development Mode", Toast.LENGTH_LONG).show();
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
                                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                deviceLoginLogoutApiCall(userModel.getUserId(), deviceToken, Constants.LoginHistory.LOGOUT);
                                SharedPreference.clearSharedPreference(FoodieHomeActivity.this);
                                startActivity(new Intent(FoodieHomeActivity.this, LoginActivity.class));
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

        }
    }

    @Override
    protected void setData() {
        setUpToolbar();
        setUpViewPager();
        setUpTabLayout();
        deviceLoginLogoutApiCall(userModel.getUserId(), FirebaseInstanceId.getInstance().getToken(),Constants.LoginHistory.LOGIN);
//        userModel= SharedPreference.getUserModel(HomeActivity.this);
//        Log.e("UserModel:-",userModel.toString());

    }
}
