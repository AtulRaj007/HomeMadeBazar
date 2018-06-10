package com.munchmash.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.munchmash.R;
import com.munchmash.adapter.FoodieHomeListAdapter;
import com.munchmash.model.BaseModel;
import com.munchmash.model.HomeChiefNearByModel;
import com.munchmash.model.UserLocation;
import com.munchmash.model.UserModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.FoodieHomeChiefNearByListApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

import java.util.ArrayList;

/**
 * Created by atulraj on 22/11/17.
 */

public class FoodieMyCityFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FoodieHomeListAdapter foodieHomeListAdapter;
    private ArrayList<HomeChiefNearByModel> homeChiefNearByModelArrayList = new ArrayList<>();
    private UserModel userModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UserLocation userLocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foodie_home_list, container, false);
    }

    @Override
    protected void initUI() {
        userLocation = SharedPreference.getUserLocation(getActivity());
        userModel = SharedPreference.getUserModel(getActivity());
        recyclerView = getView().findViewById(R.id.recycler_view);
        swipeRefreshLayout = getView().findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    protected void initialiseListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void setData() {
        foodieHomeListAdapter = new FoodieHomeListAdapter(getActivity(), homeChiefNearByModelArrayList, false);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(foodieHomeListAdapter);
        swipeRefreshLayout.setRefreshing(true);
        getChiefDetailListApiCall();
    }


    public void getChiefDetailListApiCall() {
        try {
            final FoodieHomeChiefNearByListApiCall apiCall = new FoodieHomeChiefNearByListApiCall(userModel.getUserId(), userLocation.getLatitude(), userLocation.getLongitude());
            HttpRequestHandler.getInstance(getActivity()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                homeChiefNearByModelArrayList.clear();
                                homeChiefNearByModelArrayList.addAll(apiCall.getResult());
                                foodieHomeListAdapter.notifyDataSetChanged();
                                getView().findViewById(R.id.tv_no_record_found).setVisibility(View.GONE);
                            } else if (baseModel.getStatusCode() == Constants.ServerResponseCode.NO_RECORD_FOUND) {
                                getView().findViewById(R.id.tv_no_record_found).setVisibility(View.VISIBLE);
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

    @Override
    public void onRefresh() {
        getChiefDetailListApiCall();
    }
}
