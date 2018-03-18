package com.homemadebazar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.adapter.MyOrdersAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChefIncomingOrderModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.HomeChefIncomingOrderApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

/**
 * Created by Sumit on 30/07/17.
 */

public class ScheduledMyOrdersFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private MyOrdersAdapter myOrdersAdapter;
    private UserModel userModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNoRecordFound;
    private ArrayList<HomeChefIncomingOrderModel> homeChefIncomingOrderModelArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule_myorders, container, false);
    }

    public void initUI() {
        recyclerView = getView().findViewById(R.id.recyclerView);
        swipeRefreshLayout = getView().findViewById(R.id.swipe_refresh_layout);
        tvNoRecordFound = getView().findViewById(R.id.tv_no_record_found);
        userModel = SharedPreference.getUserModel(getActivity());
    }

    public void initialiseListener() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    public void setData() {
        myOrdersAdapter = new MyOrdersAdapter(getActivity(), true, homeChefIncomingOrderModelArrayList);
        recyclerView.setAdapter(myOrdersAdapter);
        swipeRefreshLayout.setRefreshing(true);
        getBookOrderedList();
    }

    private void getBookOrderedList() {
        try {
            final HomeChefIncomingOrderApiCall apiCall = new HomeChefIncomingOrderApiCall(userModel.getUserId(), Constants.HomeChefOrder.SCHEDULED);
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
