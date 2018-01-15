package com.homemadebazar.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.adapter.FoodieHomeListAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChiefNearByModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.FoodieHomeChefSearchApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

public class FoodieSearchHomeChefActivity extends BaseActivity {
    private static String KEY_FOOD_CATEGORY_ID = "KEY_FOOD_CATEGORY_ID";
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FoodieHomeListAdapter foodieHomeListAdapter;
    private UserModel userModel;
    private ArrayList<HomeChiefNearByModel> homeChiefNearByModelArrayList = new ArrayList<>();
    private EditText etSearch;
    private String foodCategoryId;

    public static Intent getFoodieSearchIntent(Context context, String foodCategoryId) {
        Intent intent = new Intent(context, FoodieSearchHomeChefActivity.class);
        intent.putExtra(KEY_FOOD_CATEGORY_ID, foodCategoryId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodie_search_home_chef);
    }

    @Override
    protected void initUI() {
        getDataFromBundle();
        userModel = SharedPreference.getUserModel(FoodieSearchHomeChefActivity.this);
        recyclerView = findViewById(R.id.recycler_view);
        etSearch = findViewById(R.id.et_search);
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
                    searchHomeChefApiCall("", "", "", etSearch.getText().toString().trim());
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    protected void setData() {
        foodieHomeListAdapter = new FoodieHomeListAdapter(FoodieSearchHomeChefActivity.this, homeChiefNearByModelArrayList);
        linearLayoutManager = new LinearLayoutManager(FoodieSearchHomeChefActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(foodieHomeListAdapter);
        if (!TextUtils.isEmpty(foodCategoryId))
            searchHomeChefApiCall("", "", foodCategoryId, "");
    }

    //    http://localhost:14013/api/Foodies/GetFoodSearchByCatDishVendors;
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
}