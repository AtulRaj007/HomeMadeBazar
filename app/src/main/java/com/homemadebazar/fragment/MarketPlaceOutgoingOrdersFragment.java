package com.homemadebazar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homemadebazar.R;
import com.homemadebazar.adapter.MarketPlaceOrdersAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.MarketPlaceOrderModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.MarketPlaceIncomingOutgoingOrderApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

/**
 * Created by atulraj on 23/11/17.
 */

public class MarketPlaceOutgoingOrdersFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MarketPlaceOrdersAdapter marketPlaceOrdersAdapter;
    private UserModel userModel;
    private ArrayList<MarketPlaceOrderModel> marketPlaceOrderModelArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marketplace_outgoing_orders, container, false);
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
        marketPlaceOrdersAdapter = new MarketPlaceOrdersAdapter(getActivity(), marketPlaceOrderModelArrayList, Constants.MarketPlaceOrder.OUTGOING_ORDER);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(marketPlaceOrdersAdapter);
        swipeRefreshLayout.setRefreshing(true);
        showOutgoingOrders();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constants.isMarketPlaceOrderRefresh) {
            showOutgoingOrders();
        }
    }

    public void showOutgoingOrders() {
        try {
            final MarketPlaceIncomingOutgoingOrderApiCall apiCall = new MarketPlaceIncomingOutgoingOrderApiCall(userModel.getUserId(), Constants.MarketPlaceOrder.OUTGOING_ORDER);
            HttpRequestHandler.getInstance(getActivity()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    swipeRefreshLayout.setRefreshing(false);
                    Constants.isMarketPlaceOrderRefresh = false;
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                marketPlaceOrderModelArrayList.clear();
                                marketPlaceOrderModelArrayList.addAll(apiCall.getResult());
                                marketPlaceOrdersAdapter.notifyDataSetChanged();
                                getView().findViewById(R.id.tv_no_record_found).setVisibility(View.GONE);
                            } else if (baseModel.getStatusCode() == Constants.ServerResponseCode.NO_RECORD_FOUND) {
                                getView().findViewById(R.id.tv_no_record_found).setVisibility(View.VISIBLE);
                                marketPlaceOrderModelArrayList.clear();
                                marketPlaceOrdersAdapter.notifyDataSetChanged();
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
        showOutgoingOrders();
    }
}
