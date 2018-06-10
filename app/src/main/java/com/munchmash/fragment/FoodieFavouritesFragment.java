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
import com.munchmash.adapter.FoodieHomeListAdapter;
import com.munchmash.model.BaseModel;
import com.munchmash.model.HomeChiefNearByModel;
import com.munchmash.model.UserModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.FoodieHomeChiefFavouriteListApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

import java.util.ArrayList;

/**
 * Created by atulraj on 18/1/18.
 */

public class FoodieFavouritesFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FoodieHomeListAdapter foodieHomeListAdapter;
    private ArrayList<HomeChiefNearByModel> homeChiefNearByModelArrayList = new ArrayList<>();
    private UserModel userModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_chef_bookmark, container, false);
        return view;
    }

    @Override
    protected void initUI() {
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
        foodieHomeListAdapter = new FoodieHomeListAdapter(getActivity(), homeChiefNearByModelArrayList, true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(foodieHomeListAdapter);
        swipeRefreshLayout.setRefreshing(true);
        getChiefDetailListApiCall();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constants.isFavouritesChange) {
            Constants.isFavouritesChange = false;
            swipeRefreshLayout.setRefreshing(true);
            getChiefDetailListApiCall();
        }
    }

    public void getChiefDetailListApiCall() {
        try {

            final FoodieHomeChiefFavouriteListApiCall apiCall = new FoodieHomeChiefFavouriteListApiCall(userModel.getUserId());
            HttpRequestHandler.getInstance(getActivity()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                getView().findViewById(R.id.tv_no_record_found).setVisibility(View.GONE);
                                homeChiefNearByModelArrayList.clear();
                                homeChiefNearByModelArrayList.addAll(apiCall.getResult());
                                foodieHomeListAdapter.notifyDataSetChanged();
                            } else if (baseModel.getStatusCode() == Constants.ServerResponseCode.NO_RECORD_FOUND) {
                                homeChiefNearByModelArrayList.clear();
                                foodieHomeListAdapter.notifyDataSetChanged();
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
