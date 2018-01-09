package com.homemadebazar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homemadebazar.R;
import com.homemadebazar.activity.MarketPlaceOrderDetailsActivity;

/**
 * Created by sonu on 12/16/2017.
 */

public class MarketPlaceOrdersAdapter extends RecyclerView.Adapter<MarketPlaceOrdersAdapter.OrderViewHolder> {
    private Context context;

    public MarketPlaceOrdersAdapter(Context context) {
        this.context = context;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_marketplace_order, parent, false);
        return new OrderViewHolder(view);
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MarketPlaceOrderDetailsActivity.class);
                    context.startActivity(intent);
                }
            });
        }

    }
}
