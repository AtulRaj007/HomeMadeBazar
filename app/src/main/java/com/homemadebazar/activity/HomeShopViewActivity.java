package com.homemadebazar.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homemadebazar.R;
import com.homemadebazar.adapter.ImagePagerAdapter;
import com.homemadebazar.adapter.ViewPagerAdapter;
import com.homemadebazar.fragment.HomeChefBreakFastFragment;
import com.homemadebazar.fragment.HomeChefDinnerFragment;
import com.homemadebazar.fragment.HomeChefLunchFragment;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.CustomAddress;
import com.homemadebazar.model.HomeChiefNearByModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.FoodieBookOrderApiCall;
import com.homemadebazar.network.apicall.SaveFavouriteApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import me.relex.circleindicator.CircleIndicator;

public class HomeShopViewActivity extends BaseActivity implements View.OnClickListener {
    public static final String KEY_HOME_CHEF_NEARBY_MODEL = "KEY_HOME_CHEF_NEARBY_MODEL";
    public static String KEY_USER_ID = "KEY_USER_ID";
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private HomeChiefNearByModel homeChefNearByModel;
    private ViewPager viewPagerCoverImages;
    private ImageView ivProfileImage;
    private TextView tvShopName, tvPriceRange, tvAddress, tvSpeciality;
    private UserModel userModel;
    private LinearLayout llCall, llMessage, llSave;
    private CircleIndicator circleIndicator;

    public static Intent getIntent(Context context, HomeChiefNearByModel homeChiefNearByModel) {
        Intent intent = new Intent(context, HomeShopViewActivity.class);
        intent.putExtra(KEY_HOME_CHEF_NEARBY_MODEL, homeChiefNearByModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_shop_details);
        setupViewPager();
        setupToolbar();
    }

    private void getDataFromBundle() {
        homeChefNearByModel = (HomeChiefNearByModel) getIntent().getSerializableExtra(KEY_HOME_CHEF_NEARBY_MODEL);
    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("Shop Details");

    }

    @Override
    protected void initUI() {
        getDataFromBundle();
        userModel = SharedPreference.getUserModel(HomeShopViewActivity.this);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        viewPagerCoverImages = findViewById(R.id.view_pager_cover);
        ivProfileImage = findViewById(R.id.iv_profile);
        tvShopName = findViewById(R.id.tv_shop_name);
        tvPriceRange = findViewById(R.id.tv_price_range);
        tvAddress = findViewById(R.id.tv_address);
        tvSpeciality = findViewById(R.id.tv_speciality);
        llCall = findViewById(R.id.ll_call);
        llMessage = findViewById(R.id.ll_message);
        llSave = findViewById(R.id.ll_save);
        circleIndicator = findViewById(R.id.indicator);

    }

    @Override
    protected void initialiseListener() {
        llCall.setOnClickListener(this);
        llMessage.setOnClickListener(this);
        llSave.setOnClickListener(this);
    }

    @Override
    protected void setData() {
        try {
            if (!TextUtils.isEmpty(homeChefNearByModel.getProfileImage())) {
                Glide.with(HomeShopViewActivity.this).load(homeChefNearByModel.getProfileImage()).into(ivProfileImage);
            }
            tvShopName.setText(homeChefNearByModel.getFirstName() + " " + homeChefNearByModel.getLastName());
            tvPriceRange.setText(homeChefNearByModel.getPriceRange());
            tvAddress.setText(CustomAddress.getCompleteAddress(homeChefNearByModel.getAddress()));
            tvSpeciality.setText(homeChefNearByModel.getSpeciality());

            if (homeChefNearByModel.getCoverPhotoArrayList() != null && homeChefNearByModel.getCoverPhotoArrayList().size() > 0) {
                ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(HomeShopViewActivity.this, homeChefNearByModel.getCoverPhotoArrayList());
                viewPagerCoverImages.setAdapter(imagePagerAdapter);
                circleIndicator.setViewPager(viewPagerCoverImages);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setupViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        HomeChefBreakFastFragment breakFastFragment = new HomeChefBreakFastFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_USER_ID, homeChefNearByModel.getUserId());
        breakFastFragment.setArguments(bundle);
        viewPagerAdapter.addFragment(breakFastFragment, "Breakfast");

        HomeChefLunchFragment homeChefLunchFragment = new HomeChefLunchFragment();
        homeChefLunchFragment.setArguments(bundle);
        viewPagerAdapter.addFragment(homeChefLunchFragment, "Lunch");

        HomeChefDinnerFragment homeChefDinnerFragment = new HomeChefDinnerFragment();
        homeChefDinnerFragment.setArguments(bundle);
        viewPagerAdapter.addFragment(homeChefDinnerFragment, "Dinner");

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        circleIndicator.setViewPager(viewPager);
    }

    public void bookOrder(String homeChefUserId, String orderId, String bookedDate, String orderBookedFor, int noOfPerson) {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(HomeShopViewActivity.this, null);
            progressDialog.show();

            final FoodieBookOrderApiCall apiCall = new FoodieBookOrderApiCall(userModel.getUserId(), homeChefUserId, orderId, bookedDate, orderBookedFor, noOfPerson);
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    progressDialog.hide();
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                DialogUtils.showAlert(HomeShopViewActivity.this, "Order is successfully booked" + "\n Booking Id is :-" + apiCall.getBookingId());
                            } else {
                                DialogUtils.showAlert(HomeShopViewActivity.this, baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), HomeShopViewActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), HomeShopViewActivity.this, null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_call:
                if (ContextCompat.checkSelfPermission(HomeShopViewActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HomeShopViewActivity.this, new String[]{Manifest.permission.CALL_PHONE}, Constants.Keys.REQUEST_CALL_PHONE);
                } else {
                    Utils.startCall(HomeShopViewActivity.this, homeChefNearByModel.getMobile());
                }
                break;
            case R.id.ll_message:

                Utils.message(HomeShopViewActivity.this, homeChefNearByModel.getMobile());

                break;
            case R.id.ll_save:

                saveHomeChef();

                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.Keys.REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.startCall(HomeShopViewActivity.this, homeChefNearByModel.getMobile());
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void saveHomeChef() {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(HomeShopViewActivity.this, null);
            progressDialog.show();

            final SaveFavouriteApiCall apiCall = new SaveFavouriteApiCall(userModel.getUserId(), homeChefNearByModel.getUserId());
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    progressDialog.hide();
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                DialogUtils.showAlert(HomeShopViewActivity.this, "HomeChef is saved.");
                                homeChefNearByModel.setFavourite(true);
                            } else {
                                DialogUtils.showAlert(HomeShopViewActivity.this, baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), HomeShopViewActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), HomeShopViewActivity.this, null);
        }
    }
}
