package com.homemadebazar.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.adapter.FoodieHomeListAdapter;
import com.homemadebazar.adapter.FoodiesCategoryAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.FoodCategoryModel;
import com.homemadebazar.model.HomeChiefNearByModel;
import com.homemadebazar.network.GetRequest;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.FoodieHomeChefSearchApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.JSONParsingUtils;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;


public class FoodieHomeChefSearchActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView rvCategories, rvHomeChef;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private FoodiesCategoryAdapter foodiesCategoryAdapter;
    private ArrayList<FoodCategoryModel> foodCategoryModelArrayList = new ArrayList<>();
    private EditText etSearch;
    private ArrayList<HomeChiefNearByModel> homeChiefNearByModelArrayList = new ArrayList<>();
    private FoodieHomeListAdapter foodieHomeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodie_home_chef_search);
        setupToolbar();

    }

    @Override
    protected void initUI() {
        linearLayoutManager = new LinearLayoutManager(FoodieHomeChefSearchActivity.this);
        gridLayoutManager = new GridLayoutManager(FoodieHomeChefSearchActivity.this, 2);
        rvCategories = findViewById(R.id.category_recycler_view);
        rvHomeChef = findViewById(R.id.home_chef_recycler_view);
        etSearch = findViewById(R.id.et_search);
    }

//    private void setRecyclerAdapter(ArrayList<HomeChiefNearByModel> dataList) {
//        foodieHomeListAdapter = new FoodieHomeListAdapter(FoodieHomeChefSearchActivity.this, dataList);
//        linearLayoutManager = new LinearLayoutManager(FoodieHomeChefSearchActivity.this);
//        rvHomeChef.setLayoutManager(linearLayoutManager);
//        rvHomeChef.setAdapter(foodieHomeListAdapter);
//    }

    @Override
    protected void initialiseListener() {
        etSearch.setOnClickListener(this);

    }

//    {"Lattitude":"","Longtitude":"","FoodCatId":"","SearchString":""}

    @Override
    protected void setData() {
        foodiesCategoryAdapter = new FoodiesCategoryAdapter(FoodieHomeChefSearchActivity.this, foodCategoryModelArrayList);
        rvCategories.setLayoutManager(gridLayoutManager);
        rvCategories.setAdapter(foodiesCategoryAdapter);
        getFoodCategories();
    }

    private void getFoodCategories() {
        new GetRequest(FoodieHomeChefSearchActivity.this, new GetRequest.ApiCompleteListener() {
            @Override
            public void onApiCompleteListener(String response) {
                System.out.println("====== Categories ======" + response);
                ArrayList<FoodCategoryModel> tempFoodCategoryModelArrayList = JSONParsingUtils.parseFoodCategoryModel(response);
                foodCategoryModelArrayList.clear();
                foodCategoryModelArrayList.addAll(tempFoodCategoryModelArrayList);
                foodiesCategoryAdapter.notifyDataSetChanged();

            }
        }).execute(Constants.ServerURL.GET_FOOD_CATEGORIES);
    }

    private void searchHomeChef(String foodCategoryId, String searchString) {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(FoodieHomeChefSearchActivity.this, null);
            progressDialog.show();

            final FoodieHomeChefSearchApiCall apiCall = new FoodieHomeChefSearchApiCall("28.5244", "77.1855", foodCategoryId, searchString);
            HttpRequestHandler.getInstance(FoodieHomeChefSearchActivity.this).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    progressDialog.hide();
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                // DialogUtils.showAlert(getActivity(), "HomeChiefDetailList size:-" + apiCall.getHomeChiefDetailList().size());
                                homeChiefNearByModelArrayList = apiCall.getHomeChiefDetailList();

                            } else {
                                DialogUtils.showAlert(FoodieHomeChefSearchActivity.this, baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), FoodieHomeChefSearchActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), FoodieHomeChefSearchActivity.this, null);
        }
    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("Search");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_search:
//                FoodieSearchHomeChefActivity.getFoodieSearchIntent(FoodieHomeChefSearchActivity.this, "");
                break;
        }
    }
}
