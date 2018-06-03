package com.munchmash.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.munchmash.R;
import com.munchmash.adapter.FoodieHomeListAdapter;
import com.munchmash.model.BaseModel;
import com.munchmash.model.HomeChiefNearByModel;
import com.munchmash.model.UserLocation;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.FoodieHomeChefSearchApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

import java.util.ArrayList;

public class FoodieSearchHomeChefActivity extends BaseActivity {
    private static String KEY_FOOD_CATEGORY_ID = "KEY_FOOD_CATEGORY_ID";
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FoodieHomeListAdapter foodieHomeListAdapter;
    private ArrayList<HomeChiefNearByModel> homeChiefNearByModelArrayList = new ArrayList<>();
    private EditText etSearch;
    private String foodCategoryId;
    private TextView tvNoRecordFound;

    public static Intent getFoodieSearchIntent(Context context, String foodCategoryId) {
        Intent intent = new Intent(context, FoodieSearchHomeChefActivity.class);
        intent.putExtra(KEY_FOOD_CATEGORY_ID, foodCategoryId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodie_search_home_chef);
        setUpToolbar();
    }

    @Override
    protected void initUI() {
        getDataFromBundle();
        recyclerView = findViewById(R.id.recycler_view);
        etSearch = findViewById(R.id.et_search);
        tvNoRecordFound = findViewById(R.id.tv_no_record_found);
    }

    private void getDataFromBundle() {
        foodCategoryId = getIntent().getStringExtra(KEY_FOOD_CATEGORY_ID);
    }

    @Override
    protected void initialiseListener() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(etSearch.getText().toString().trim())) {
                        UserLocation userLocation = SharedPreference.getUserLocation(FoodieSearchHomeChefActivity.this);
                        searchHomeChefApiCall(/*String.valueOf(userLocation.getLatitude())*/"",/*String.valueOf(userLocation.getLongitude())*/"", "", etSearch.getText().toString().trim());
                        return true;
                    } else {
                        DialogUtils.showAlert(FoodieSearchHomeChefActivity.this, "Please enter text to search");
                    }
                }

                return false;
            }
        });
    }

    @Override
    protected void setData() {
        foodieHomeListAdapter = new FoodieHomeListAdapter(FoodieSearchHomeChefActivity.this, homeChiefNearByModelArrayList, false);
        linearLayoutManager = new LinearLayoutManager(FoodieSearchHomeChefActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(foodieHomeListAdapter);
        if (!TextUtils.isEmpty(foodCategoryId)) {
            UserLocation userLocation = SharedPreference.getUserLocation(FoodieSearchHomeChefActivity.this);
            searchHomeChefApiCall(/*String.valueOf(userLocation.getLatitude())*/"", /*String.valueOf(userLocation.getLongitude())*/"", foodCategoryId, "");

        }
    }

    public void searchHomeChefApiCall(String latitude, String longitude, String foodCategoryId, String searchString) {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(this, null);
            progressDialog.show();

            final FoodieHomeChefSearchApiCall apiCall = new FoodieHomeChefSearchApiCall(latitude, longitude, foodCategoryId, searchString);
            HttpRequestHandler.getInstance(this).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    progressDialog.hide();
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                homeChiefNearByModelArrayList.clear();
                                homeChiefNearByModelArrayList.addAll(apiCall.getHomeChiefDetailList());
                                foodieHomeListAdapter.notifyDataSetChanged();
                                tvNoRecordFound.setVisibility(View.GONE);

                            } else if (baseModel.getStatusCode() == Constants.ServerResponseCode.NO_RECORD_FOUND) {
                                homeChiefNearByModelArrayList.clear();
                                foodieHomeListAdapter.notifyDataSetChanged();
                                tvNoRecordFound.setVisibility(View.VISIBLE);

                            } else {
                                DialogUtils.showAlert(FoodieSearchHomeChefActivity.this, baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), FoodieSearchHomeChefActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), FoodieSearchHomeChefActivity.this, null);
        }
    }

    private void setUpToolbar() {
        ((TextView) findViewById(R.id.tv_title)).setText("HomeChef List");
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
