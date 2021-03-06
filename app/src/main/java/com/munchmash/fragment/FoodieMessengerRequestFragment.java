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
import com.munchmash.adapter.FoodieMessengerRequestAdapter;
import com.munchmash.model.BaseModel;
import com.munchmash.model.UserModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.MessengerRequestTabApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

import java.util.ArrayList;

/**
 * Created by atulraj on 22/11/17.
 */

public class FoodieMessengerRequestFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    ArrayList<UserModel> reqDataList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FoodieMessengerRequestAdapter foodieMessengerRequestAdapter;
    private UserModel userModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foodie_messenger_request, container, false);
        return view;
    }

    @Override
    protected void initUI() {
        swipeRefreshLayout = getView().findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout = getView().findViewById(R.id.swipe_refresh_layout);
        recyclerView = getView().findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
    }

    @Override
    protected void initialiseListener() {
        reqDataList = new ArrayList<>();
        swipeRefreshLayout.setOnRefreshListener(this);
        userModel = SharedPreference.getUserModel(getActivity());
    }

    @Override
    protected void setData() {
        foodieMessengerRequestAdapter = new FoodieMessengerRequestAdapter(getActivity(), userModel.getUserId(), reqDataList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(foodieMessengerRequestAdapter);
        getFriendRequestListApiCall(userModel.getUserId());
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            getFriendRequestListApiCall(userModel.getUserId());
            swipeRefreshLayout.setRefreshing(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFriendRequestListApiCall(String userId) {
        try {
            final MessengerRequestTabApiCall apiCall = new MessengerRequestTabApiCall(userId);
            HttpRequestHandler.getInstance(getActivity()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                getView().findViewById(R.id.tv_no_record_found).setVisibility(View.GONE);
                                reqDataList.clear();
                                reqDataList.addAll(apiCall.getResult());
                                foodieMessengerRequestAdapter.notifyDataSetChanged();
                            } else if (baseModel.getStatusCode() == Constants.ServerResponseCode.NO_RECORD_FOUND) {
                                reqDataList.clear();
                                foodieMessengerRequestAdapter.notifyDataSetChanged();
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
        getFriendRequestListApiCall(userModel.getUserId());
    }

}
