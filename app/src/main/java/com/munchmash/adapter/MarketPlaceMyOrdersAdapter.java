package com.munchmash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.munchmash.R;
import com.munchmash.model.MarketPlaceMyOrdersModel;
import com.munchmash.model.UserModel;
import com.munchmash.util.Constants;
import com.munchmash.util.SharedPreference;

import java.util.ArrayList;

/**
 * Created by atulraj on 25/3/18.
 */

public class MarketPlaceMyOrdersAdapter extends RecyclerView.Adapter<MarketPlaceMyOrdersAdapter.OrdersViewHolder> {
    private Context context;
    private ArrayList<MarketPlaceMyOrdersModel> marketPlaceMyOrdersModels;
    private UserModel userModel;

    public MarketPlaceMyOrdersAdapter(Context context, ArrayList<MarketPlaceMyOrdersModel> marketPlaceMyOrdersModels) {
        this.context = context;
        this.marketPlaceMyOrdersModels = marketPlaceMyOrdersModels;
        userModel = SharedPreference.getUserModel(context);
    }

    @Override
    public OrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_marketplace_myorders, parent, false);
        return new OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrdersViewHolder holder, int position) {
        try {
            if (!TextUtils.isEmpty(marketPlaceMyOrdersModels.get(position).getProductImageUrl())) {
                Glide.with(context).load(marketPlaceMyOrdersModels.get(position).getProductImageUrl()).into(holder.ivProductImage);
            } else {
                holder.ivProductImage.setImageDrawable(null);
            }

            holder.tvOrderId.setText(marketPlaceMyOrdersModels.get(position).getOrderId());
            holder.tvProductName.setText(marketPlaceMyOrdersModels.get(position).getProductName());
            holder.tvBrandName.setText(marketPlaceMyOrdersModels.get(position).getBrandName());
            holder.tvPrice.setText(marketPlaceMyOrdersModels.get(position).getPrice());

            statusTextHandling(marketPlaceMyOrdersModels.get(position).getActionId(), holder);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return marketPlaceMyOrdersModels.size();
    }

    private void statusTextHandling(String actionId, OrdersViewHolder holder) {
        if (userModel.getAccountType().equals(Constants.Role.HOME_CHEF.getStringRole())) {
            // Home Chef
            if (actionId.equals(Constants.MarketPlaceOrderACtionType.PRODUCT_BOOK)) {
                holder.tvStatus.setText("You have successfully placed order.\n Waiting for MarketPlace Approval.");
            } else if (actionId.equals(Constants.MarketPlaceOrderACtionType.ACCEPT)) {
                holder.tvStatus.setText("You Order is successfully Accepted");
            } else if (actionId.equals(Constants.MarketPlaceOrderACtionType.REJECT)) {
                holder.tvStatus.setText("You Order is rejected by MarketPlace user");
            } else if (actionId.equals(Constants.MarketPlaceOrderACtionType.DISPATCH)) {
                holder.tvStatus.setText("You Order is dispatched by MarketPlace user");
            }
        } else {
            // MarketPlace
            if (actionId.equals(Constants.MarketPlaceOrderACtionType.PRODUCT_BOOK)) {
                holder.tvStatus.setText("You have an incoming order.");
            } else if (actionId.equals(Constants.MarketPlaceOrderACtionType.ACCEPT)) {
                holder.tvStatus.setText("You have accepted the order.");
            } else if (actionId.equals(Constants.MarketPlaceOrderACtionType.REJECT)) {
                holder.tvStatus.setText("You have rejected the order.");
            } else if (actionId.equals(Constants.MarketPlaceOrderACtionType.DISPATCH)) {
                holder.tvStatus.setText("Order is successfully dispatched.");
            }

        }
    }

    class OrdersViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProductImage;
        private TextView tvOrderId;
        private TextView tvProductName;
        private TextView tvBrandName;
        private TextView tvPrice;
        private TextView tvStatus;

        OrdersViewHolder(View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvBrandName = itemView.findViewById(R.id.tv_brand_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }
}
