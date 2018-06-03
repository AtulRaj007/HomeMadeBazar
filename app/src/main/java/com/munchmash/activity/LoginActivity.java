package com.munchmash.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.munchmash.R;
import com.munchmash.adapter.ViewPagerAdapter;
import com.munchmash.fragment.LoginAnimationFragment;
import com.munchmash.fragment.LoginFoodieFragment;
import com.munchmash.fragment.LoginHomeChefFragment;
import com.munchmash.fragment.LoginMarketPlaceSplashFragment;
import com.munchmash.model.UserModel;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

import me.relex.circleindicator.CircleIndicator;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private ViewPagerAdapter loginSplashPagerAdapter;
    private CircleIndicator circleIndicator;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userModel = SharedPreference.getUserModel(LoginActivity.this);
//        Log.e("UserModel:-", userModel.toString());
//        Log.e("DeviceToken:-", FirebaseInstanceId.getInstance().getToken());
        if (userModel != null) {
            if (!TextUtils.isEmpty(userModel.getUserId())) {
//                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                Utils.openAccountTypeHomeScreen(LoginActivity.this, userModel.getAccountType());
                finish();
            }
        }
        setContentView(R.layout.activity_login);
    }


    @Override
    protected void initUI() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        circleIndicator = (CircleIndicator) findViewById(R.id.indicator);
    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.tv_connect_social_account).setOnClickListener(this);
        findViewById(R.id.view_number).setOnClickListener(this);
    }

    @Override
    protected void setData() {
        setUpLoginSplashViewPager();
    }

    private void setUpLoginSplashViewPager() {
        loginSplashPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        loginSplashPagerAdapter.addFragment(new LoginAnimationFragment(), "");
        loginSplashPagerAdapter.addFragment(new LoginHomeChefFragment(), "HomeChef");
        loginSplashPagerAdapter.addFragment(new LoginFoodieFragment(), "Foodie");
        loginSplashPagerAdapter.addFragment(new LoginMarketPlaceSplashFragment(), "MarketPlace");
        viewPager.setAdapter(loginSplashPagerAdapter);

        circleIndicator.setViewPager(viewPager);
        loginSplashPagerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_number:
                startActivity(new Intent(LoginActivity.this, EnterMobileNumberActivity.class));
                break;
            case R.id.tv_connect_social_account:
                startActivity(SocialLoginActivity.getIntent(LoginActivity.this, false));
                break;
        }
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
}
