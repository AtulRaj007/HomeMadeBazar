package com.homemadebazar.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homemadebazar.R;
import com.homemadebazar.activity.HomeShopDetailsActivity;
import com.homemadebazar.adapter.HomeChefFoodTimingAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChefOrderModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.GetListOfOrdersChefApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

/**
 * Created by atulraj on 15/12/17.
 */

public class HomeChefDinnerFragment extends BaseFragment {
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private HomeChefFoodTimingAdapter homeChefLunchAdapter;
    private String userId;
    private ArrayList<HomeChefOrderModel> homeChefOrderModelArrayList = new ArrayList<>();

    @Override
    protected void initUI() {
        userId = getArguments().getString(HomeShopDetailsActivity.KEY_USER_ID);
        recyclerView = getView().findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        homeChefLunchAdapter = new HomeChefFoodTimingAdapter(getActivity(),userId, homeChefOrderModelArrayList);
        recyclerView.setAdapter(homeChefLunchAdapter);
    }

    @Override
    protected void initialiseListener() {

    }

    @Override
    protected void setData() {
        getHomeChefDinnerOrders();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_chef_dinner, container, false);
        return view;
    }

    private void getHomeChefDinnerOrders() {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(getActivity(), null);
            progressDialog.show();

            final GetListOfOrdersChefApiCall apiCall = new GetListOfOrdersChefApiCall(userId, Constants.FoodType.DINNER);
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
