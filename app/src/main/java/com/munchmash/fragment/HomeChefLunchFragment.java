package com.munchmash.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.munchmash.R;
import com.munchmash.activity.HomeShopViewActivity;
import com.munchmash.adapter.HomeChefFoodTimingAdapter;
import com.munchmash.model.BaseModel;
import com.munchmash.model.HomeChefOrderModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.GetListOfOrdersChefApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.Utils;

import java.util.ArrayList;

/**
 * Created by atulraj on 15/12/17.
 */

public class HomeChefLunchFragment extends BaseFragment {
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private HomeChefFoodTimingAdapter homeChefLunchAdapter;
    private String userId;
    private ArrayList<HomeChefOrderModel> homeChefOrderModelArrayList = new ArrayList<>();


    @Override
    protected void initUI() {
        userId = getArguments().getString(HomeShopViewActivity.KEY_USER_ID);
        recyclerView = getView().findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        homeChefLunchAdapter = new HomeChefFoodTimingAdapter(getActivity(), userId, homeChefOrderModelArrayList, Constants.FoodType.LUNCH);
        recyclerView.setAdapter(homeChefLunchAdapter);

    }

    @Override
    protected void initialiseListener() {

    }

    @Override
    protected void setData() {
        getHomeChefLunchOrders();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_chef_lunch, container, false);
        return view;
    }

    private void getHomeChefLunchOrders() {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(getActivity(), null);
            progressDialog.show();

            final GetListOfOrdersChefApiCall apiCall = new GetListOfOrdersChefApiCall(userId, Constants.FoodType.LUNCH);
            HttpRequestHandler.getInstance(getActivity().getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    progressDialog.hide();
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                ArrayList<HomeChefOrderModel> tempHomeChefOrderModelArrayList = apiCall.getResult();
                                homeChefOrderModelArrayList.clear();
                                homeChefOrderModelArrayList.addAll(tempHomeChefOrderModelArrayList);
                                homeChefLunchAdapter.notifyDataSetChanged();
                            } else if (baseModel.getStatusCode() == Constants.ServerResponseCode.NO_RECORD_FOUND) {
                                homeChefOrderModelArrayList.clear();
                                homeChefLunchAdapter.notifyDataSetChanged();
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
}
