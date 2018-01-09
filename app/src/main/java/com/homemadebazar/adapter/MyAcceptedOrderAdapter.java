package com.homemadebazar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homemadebazar.R;

/**
 * Created by Sumit on 31/07/17.
 */

public class MyAcceptedOrderAdapter extends RecyclerView.Adapter<MyAcceptedOrderAdapter.OrderViewHolder> {

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accepted_order, parent, false));
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        public OrderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
