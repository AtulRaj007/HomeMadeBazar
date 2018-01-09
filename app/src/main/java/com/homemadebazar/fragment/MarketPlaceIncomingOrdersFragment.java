package com.homemadebazar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homemadebazar.R;
import com.homemadebazar.adapter.MarketPlaceOrdersAdapter;

/**
 * Created by atulraj on 23/11/17.
 */

public class MarketPlaceIncomingOrdersFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private MarketPlaceOrdersAdapter marketPlaceOrdersAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market_place_incoming_orders, container, false);
        return view;
    }

    @Override
    protected void initUI() {
        recyclerView = getView().findViewById(R.id.recycler_view);
    }

    @Override
    protected void initialiseListener() {

    }

    @Override
    protected void setData() {
        marketPlaceOrdersAdapter = new MarketPlaceOrdersAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(marketPlaceOrdersAdapter);
    }

}
