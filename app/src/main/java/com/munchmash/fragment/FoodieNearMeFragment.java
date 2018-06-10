package com.munchmash.fragment;

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

import com.munchmash.R;
import com.munchmash.activity.FoodieSearchHomeChefActivity;
import com.munchmash.adapter.FoodiesCategoryAdapter;
import com.munchmash.model.FoodCategoryModel;
import com.munchmash.model.HomeChiefNearByModel;
import com.munchmash.network.GetRequest;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import java.util.ArrayList;


public class FoodieNearMeFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView rvCategories, rvHomeChef;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private FoodiesCategoryAdapter foodiesCategoryAdapter;
    private ArrayList<FoodCategoryModel> foodCategoryModelArrayList = new ArrayList<>();
    private EditText etSearch;
    private ArrayList<HomeChiefNearByModel> homeChiefNearByModelArrayList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

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

    @Override
    protected void initialiseListener() {
        etSearch.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);

    }


    @Override
    protected void setData() {
        foodiesCategoryAdapter = new FoodiesCategoryAdapter(getActivity(), foodCategoryModelArrayList);
        rvCategories.setLayoutManager(gridLayoutManager);
        rvCategories.setAdapter(foodiesCategoryAdapter);
        getFoodCategories();
        swipeRefreshLayout.setRefreshing(true);
    }

    private void getFoodCategories() {
        System.out.println();
        new GetRequest(getActivity(), new GetRequest.ApiCompleteListener() {
            @Override
            public void onApiCompleteListener(String response) {
                System.out.println("====== Categories ======" + response);
                swipeRefreshLayout.setRefreshing(false);
                ArrayList<FoodCategoryModel> tempFoodCategoryModelArrayList = JSONParsingUtils.parseFoodCategoryModel(response);
                foodCategoryModelArrayList.clear();
                foodCategoryModelArrayList.addAll(tempFoodCategoryModelArrayList);
                foodiesCategoryAdapter.notifyDataSetChanged();

            }
        }).execute(Constants.ServerURL.GET_FOOD_CATEGORIES);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_search:
                startActivity(FoodieSearchHomeChefActivity.getFoodieSearchIntent(getActivity(), ""));
                break;
        }
    }

    @Override
    public void onRefresh() {
        getFoodCategories();
    }
}
