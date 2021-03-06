package com.munchmash.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.munchmash.R;
import com.munchmash.activity.MarketPlaceAddCategoryActivity;
import com.munchmash.adapter.MarketPlaceManageCategoryAdapter;
import com.munchmash.model.MarketPlaceProductCategoryModel;
import com.munchmash.model.UserModel;
import com.munchmash.network.GetRequest;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;
import com.munchmash.util.SharedPreference;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by atulraj on 23/11/17.
 */

public class MarketPlaceManageCategoryFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private MarketPlaceManageCategoryAdapter marketPlaceManageCategoryAdapter;
    private UserModel userModel;
    private ArrayList<MarketPlaceProductCategoryModel> marketPlaceProductCategoryModelArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market_place_category, container, false);
        return view;
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(getActivity());
        recyclerView = getView().findViewById(R.id.recycler_view);

    }

    @Override
    protected void initialiseListener() {
        getView().findViewById(R.id.ll_add_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MarketPlaceAddCategoryActivity.class);
                startActivityForResult(intent, Constants.Keys.REQUEST_ADD_CATEGORY);
            }
        });
    }

    @Override
    protected void setData() {
        marketPlaceManageCategoryAdapter = new MarketPlaceManageCategoryAdapter(getActivity(), marketPlaceProductCategoryModelArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(marketPlaceManageCategoryAdapter);
        getProductCategoryList();
    }

    private String getCategoryServiceUrl() {
        String url = Constants.ServerURL.MARKETPLACE_GET_PRODUCT_CATEGORY_LIST + userModel.getUserId();
        System.out.println(Constants.ServiceTAG.URL + url);
        return url;
    }

    private void getProductCategoryList() {
        new GetRequest(getActivity(), new GetRequest.ApiCompleteListener() {
            @Override
            public void onApiCompleteListener(String response) {
                System.out.println(Constants.ServiceTAG.RESPONSE + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("StatusCode") == Constants.ServerResponseCode.SUCCESS) {
                        ArrayList<MarketPlaceProductCategoryModel> productCategoryModelArrayList = JSONParsingUtils.parseProductCategoryModel(object);
                        marketPlaceProductCategoryModelArrayList.clear();
                        marketPlaceProductCategoryModelArrayList.addAll(productCategoryModelArrayList);
                        marketPlaceManageCategoryAdapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).execute(getCategoryServiceUrl());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK && requestCode == Constants.Keys.REQUEST_ADD_CATEGORY) {
            getProductCategoryList();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
