package com.homemadebazar.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.homemadebazar.R;
import com.homemadebazar.activity.FoodieSearchHomeChefActivity;
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


public class FoodieHomeChefSearchFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView rvCategories, rvHomeChef;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private FoodiesCategoryAdapter foodiesCategoryAdapter;
    private ArrayList<FoodCategoryModel> foodCategoryModelArrayList = new ArrayList<>();
    private EditText etSearch;
    private ArrayList<HomeChiefNearByModel> homeChiefNearByModelArrayList = new ArrayList<>();
//    private FoodieHomeListAdapter foodieHomeListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_foodie_home_chef_search);
//        setupToolbar();
//
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_foodie_home_chef_search, container, false);
    }

    @Override
    protected void initUI() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvCategories = getView().findViewById(R.id.category_recycler_view);
        rvHomeChef = getView().findViewById(R.id.home_chef_recycler_view);
        etSearch = getView().findViewById(R.id.et_search);
        swipeRefreshLayout = getView().findViewById(R.id.swipe_refresh_layout);
    }

//    private void setRecyclerAdapter(ArrayList<HomeChiefNearByModel> dataList) {
//        foodieHomeListAdapter = new FoodieHomeListAdapter(getActivity(), dataList);
//        linearLayoutManager = new LinearLayoutManager(getActivity());
//        rvHomeChef.setLayoutManager(linearLayoutManager);
//        rvHomeChef.setAdapter(foodieHomeListAdapter);
//    }

    @Override
    protected void initialiseListener() {
        etSearch.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);

    }

//    {"Lattitude":"","Longtitude":"","FoodCatId":"","SearchString":""}

    @Override
    protected void setData() {
        foodiesCategoryAdapter = new FoodiesCategoryAdapter(getActivity(), foodCategoryModelArrayList);
        rvCategories.setLayoutManager(gridLayoutManager);
        rvCategories.setAdapter(foodiesCategoryAdapter);
        getFoodCategories();
    }

    private void getFoodCategories() {
        new GetRequest(getActivity(), new GetRequest.ApiCompleteListener() {
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
            final Dialog progressDialog = DialogUtils.getProgressDialog(getActivity(), null);
            progressDialog.show();

            final FoodieHomeChefSearchApiCall apiCall = new FoodieHomeChefSearchApiCall("28.5244", "77.1855", foodCategoryId, searchString);
            HttpRequestHandler.getInstance(getActivity()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

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
                                DialogUtils.showAlert(getActivity(), baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), getActivity(), null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), getActivity(), null);
        }
    }

//    private void setupToolbar() {
//        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        ((TextView) findViewById(R.id.tv_title)).setText("Search");
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_search:
                FoodieSearchHomeChefActivity.getFoodieSearchIntent(getActivity(), "");
                break;
        }
    }

    @Override
    public void onRefresh() {
        getFoodCategories();
    }
}
