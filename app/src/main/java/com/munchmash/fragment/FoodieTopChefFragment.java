package com.munchmash.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.munchmash.R;
import com.munchmash.adapter.FoodieDiscoverAdapter;
import com.munchmash.model.BaseModel;
import com.munchmash.model.HomeChefOrderModel;
import com.munchmash.model.UserModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.GetListOfHotDealsApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

import java.util.ArrayList;

/**
 * Created by atulraj on 6/2/18.
 */

public class FoodieTopChefFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FoodieDiscoverAdapter foodieDiscoverAdapter;
    private ArrayList<HomeChefOrderModel> homeChefOrderModelArrayList = new ArrayList<>();
    private UserModel userModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_chef, container, false);
    }

    private void getHomeChefHotDealsOrders(String userId, String tabRequestId) {
        try {
            final GetListOfHotDealsApiCall apiCall = new GetListOfHotDealsApiCall(userId, tabRequestId);
            HttpRequestHandler.getInstance(getActivity().getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    if (e == null) { // Success
                        try {
                            swipeRefreshLayout.setRefreshing(false);
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                ArrayList<HomeChefOrderModel> tempHomeChefOrderModelArrayList = apiCall.getResult();
                                homeChefOrderModelArrayList.clear();
                                homeChefOrderModelArrayList.addAll(tempHomeChefOrderModelArrayList);
                                foodieDiscoverAdapter.notifyDataSetChanged();
                            } else if (baseModel.getStatusCode() == Constants.ServerResponseCode.NO_RECORD_FOUND) {
                                homeChefOrderModelArrayList.clear();
                                foodieDiscoverAdapter.notifyDataSetChanged();
                                getView().findViewById(R.id.tv_no_record_found).setVisibility(View.VISIBLE);
                            } else {
                                DialogUtils.showAlert(getActivity(), baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), getActivity(), null);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), getActivity(), null);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        swipeRefreshLayout = getView().findViewById(R.id.swipe_refresh_layout);
        recyclerView = getView().findViewById(R.id.recycler_view);
    }

    @Override
    protected void initialiseListener() {
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    protected void setData() {
        foodieDiscoverAdapter = new FoodieDiscoverAdapter(getActivity(), homeChefOrderModelArrayList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(foodieDiscoverAdapter);
        swipeRefreshLayout.setRefreshing(true);
        getHomeChefHotDealsOrders(userModel.getUserId(), Constants.DiscoverTab.TopChefs);
    }

    @Override
    public void onRefresh() {
        getHomeChefHotDealsOrders(userModel.getUserId(), Constants.DiscoverTab.TopChefs);
    }
}
