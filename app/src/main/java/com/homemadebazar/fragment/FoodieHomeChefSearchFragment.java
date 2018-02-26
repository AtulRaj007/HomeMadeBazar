package com.homemadebazar.fragment;

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
import com.homemadebazar.adapter.FoodiesCategoryAdapter;
import com.homemadebazar.model.FoodCategoryModel;
import com.homemadebazar.model.HomeChiefNearByModel;
import com.homemadebazar.network.GetRequest;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import java.util.ArrayList;


public class FoodieHomeChefSearchFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
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
    }

    private void getFoodCategories() {
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
