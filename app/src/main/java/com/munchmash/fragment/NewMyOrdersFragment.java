package com.munchmash.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.munchmash.R;
import com.munchmash.adapter.MyOrdersAdapter;
import com.munchmash.model.BaseModel;
import com.munchmash.model.HomeChefIncomingOrderModel;
import com.munchmash.model.UserModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.HomeChefIncomingOrderApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

import java.util.ArrayList;

/**
 * Created by Sumit on 30/07/17.
 */

public class NewMyOrdersFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private MyOrdersAdapter myOrdersAdapter;
    private UserModel userModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<HomeChefIncomingOrderModel> homeChefIncomingOrderModelArrayList = new ArrayList<>();
    private TextView tvNoRecordFound;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_myorders, container, false);
    }

    public void initUI() {
        recyclerView = getView().findViewById(R.id.recyclerView);
        swipeRefreshLayout = getView().findViewById(R.id.swipe_refresh_layout);
        tvNoRecordFound = getView().findViewById(R.id.tv_no_record_found);
        userModel = SharedPreference.getUserModel(getActivity());
    }

    public void initialiseListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void setData() {
        myOrdersAdapter = new MyOrdersAdapter(getActivity(), false, homeChefIncomingOrderModelArrayList);
        recyclerView.setAdapter(myOrdersAdapter);
        swipeRefreshLayout.setRefreshing(true);
        getBookOrderedList();
    }

    private void getBookOrderedList() {
        try {
            final HomeChefIncomingOrderApiCall apiCall = new HomeChefIncomingOrderApiCall(userModel.getUserId(), Constants.HomeChefOrder.NowOrder);
            HttpRequestHandler.getInstance(getActivity().getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                ArrayList<HomeChefIncomingOrderModel> tempHomeChefIncomingOrderArrayList = apiCall.getResult();
                                homeChefIncomingOrderModelArrayList.clear();
                                homeChefIncomingOrderModelArrayList.addAll(tempHomeChefIncomingOrderArrayList);
                                myOrdersAdapter.notifyDataSetChanged();
                                tvNoRecordFound.setVisibility(View.GONE);
                            } else if (baseModel.getStatusCode() == Constants.ServerResponseCode.NO_RECORD_FOUND) {
                                homeChefIncomingOrderModelArrayList.clear();
                                myOrdersAdapter.notifyDataSetChanged();
                                tvNoRecordFound.setVisibility(View.VISIBLE);
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
        getBookOrderedList();
    }
}
