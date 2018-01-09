package com.homemadebazar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homemadebazar.R;

/**
 * Created by sonu on 12/18/2017.
 */

public class MarketPlaceOrderDetailAdapter extends RecyclerView.Adapter<MarketPlaceOrderDetailAdapter.OrderDetailsViewHolder> {
    private Context context;

    public MarketPlaceOrderDetailAdapter(Context context) {
        this.context = context;
    }

    @Override
    public OrderDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_order_details_products, parent, false);
        return new OrderDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderDetailsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class OrderDetailsViewHolder extends RecyclerView.ViewHolder {
        public OrderDetailsViewHolder(View itemView) {
            super(itemView);
        }
    }
}
