package com.munchmash.activity;

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
import com.munchmash.R;
import com.munchmash.adapter.ImagePagerAdapter;
import com.munchmash.adapter.ViewPagerAdapter;
import com.munchmash.fragment.HomeChefBreakFastFragment;
import com.munchmash.fragment.HomeChefDinnerFragment;
import com.munchmash.fragment.HomeChefLunchFragment;
import com.munchmash.model.BaseModel;
import com.munchmash.model.CustomAddress;
import com.munchmash.model.HomeChiefNearByModel;
import com.munchmash.model.UserModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.FoodieBookOrderApiCall;
import com.munchmash.network.apicall.SaveFavouriteApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

import me.relex.circleindicator.CircleIndicator;

public class ShopDetailsActivity extends BaseActivity implements View.OnClickListener {
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
        Intent intent = new Intent(context, ShopDetailsActivity.class);
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
        userModel = SharedPreference.getUserModel(ShopDetailsActivity.this);
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
                Glide.with(ShopDetailsActivity.this).load(homeChefNearByModel.getProfileImage()).into(ivProfileImage);
            }
            tvShopName.setText(homeChefNearByModel.getFirstName() + " " + homeChefNearByModel.getLastName() + "\n" + homeChefNearByModel.getShopName());
            tvPriceRange.setText(homeChefNearByModel.getPriceRange());
            tvAddress.setText(CustomAddress.getCompleteAddress(homeChefNearByModel.getAddress()));
            tvSpeciality.setText(homeChefNearByModel.getSpeciality());

            if (homeChefNearByModel.getCoverPhotoArrayList() != null && homeChefNearByModel.getCoverPhotoArrayList().size() > 0) {
                ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(ShopDetailsActivity.this, homeChefNearByModel.getCoverPhotoArrayList());
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
    }

    public void bookOrder(String homeChefUserId, String orderId, String bookedDate, String orderBookedFor, int noOfPerson) {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(ShopDetailsActivity.this, null);
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
                                DialogUtils.showAlert(ShopDetailsActivity.this, "Order sent for HomeChef Review\nPlease wait for the order to get accepted." + "\nYour Booking Id is - " + apiCall.getBookingId());
                            } else if (baseModel.getStatusCode() == Constants.ServerResponseCode.INSUFFICIENT_MONEY) {
                                DialogUtils.showAlert(ShopDetailsActivity.this, "You have insufficient money. Do you wish to Add Money", new Runnable() {
                                    @Override
                                    public void run() {
                                        //OK
                                        startActivity(new Intent(ShopDetailsActivity.this, AddMoneyActivity.class));
                                    }
                                }, new Runnable() {
                                    @Override
                                    public void run() {
                                        // Cancel

                                    }
                                });
                            } else {
                                DialogUtils.showAlert(ShopDetailsActivity.this, baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), ShopDetailsActivity.this, null);
                    }
                }
            }, false);
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), ShopDetailsActivity.this, null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_call:
                if (ContextCompat.checkSelfPermission(ShopDetailsActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ShopDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, Constants.Keys.REQUEST_CALL_PHONE);
                } else {
                    Utils.startCall(ShopDetailsActivity.this, homeChefNearByModel.getMobile());
                }
                break;
            case R.id.ll_message:

                Utils.message(ShopDetailsActivity.this, homeChefNearByModel.getMobile());

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
                Utils.startCall(ShopDetailsActivity.this, homeChefNearByModel.getMobile());
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void saveHomeChef() {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(ShopDetailsActivity.this, null);
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
                                DialogUtils.showAlert(ShopDetailsActivity.this, "HomeChef is saved to Favourites.");
                                Constants.isFavouritesChange = true;
                                homeChefNearByModel.setFavourite(true);
                            } else {
                                DialogUtils.showAlert(ShopDetailsActivity.this, baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), ShopDetailsActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), ShopDetailsActivity.this, null);
        }
    }
}
