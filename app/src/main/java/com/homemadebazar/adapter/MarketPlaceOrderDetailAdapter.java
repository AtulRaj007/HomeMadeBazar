package com.homemadebazar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homemadebazar.R;
import com.homemadebazar.model.MarketPlaceOrderProductModel;

import java.util.ArrayList;

/**
 * Created by sonu on 12/18/2017.
 */

public class MarketPlaceOrderDetailAdapter extends RecyclerView.Adapter<MarketPlaceOrderDetailAdapter.OrderDetailsViewHolder> {
    private Context context;
    private ArrayList<MarketPlaceOrderProductModel> marketPlaceOrderProductModelArrayList;

    public MarketPlaceOrderDetailAdapter(Context context, ArrayList<MarketPlaceOrderProductModel> marketPlaceOrderProductModelArrayList) {
        this.context = context;
        this.marketPlaceOrderProductModelArrayList = marketPlaceOrderProductModelArrayList;
    }

    @Override
    public OrderDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_order_details_products, parent, false);
        return new OrderDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderDetailsViewHolder holder, int position) {
        MarketPlaceOrderProductModel marketPlaceOrderProductModel = marketPlaceOrderProductModelArrayList.get(position);
        if (!TextUtils.isEmpty(marketPlaceOrderProductModel.getProductUrl())) {
            Glide.with(context).load(marketPlaceOrderProductModel.getProductUrl()).into(holder.ivProductPic);
        } else {
            holder.ivProductPic.setImageDrawable(null);
        }
        holder.tvName.setText(marketPlaceOrderProductModel.getProductName());
        holder.tvPrice.setText(marketPlaceOrderProductModel.getPrice() + "");
        holder.tvOrderId.setText(marketPlaceOrderProductModel.getProductId());
    }

    @Override
    public int getItemCount() {
        return marketPlaceOrderProductModelArrayList.size();
    }

    class OrderDetailsViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductPic;
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvOrderId;

        OrderDetailsViewHolder(View itemView) {
            super(itemView);
            ivProductPic = itemView.findViewById(R.id.iv_product_pic);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvOrderId = itemView.findViewById(R.id.tv_product_id);
        }
    }
}
