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
import com.homemadebazar.activity.MarketPlaceOrderDetailsActivity;
import com.homemadebazar.model.MarketPlaceOrderModel;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

/**
 * Created by sonu on 12/16/2017.
 */

public class MarketPlaceOrdersAdapter extends RecyclerView.Adapter<MarketPlaceOrdersAdapter.OrderViewHolder> {
    private Context context;
    private ArrayList<MarketPlaceOrderModel> marketPlaceOrderModelArrayList;

    public MarketPlaceOrdersAdapter(Context context, ArrayList<MarketPlaceOrderModel> marketPlaceOrderModelArrayList) {
        this.context = context;
        this.marketPlaceOrderModelArrayList = marketPlaceOrderModelArrayList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_marketplace_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        try {
            MarketPlaceOrderModel marketPlaceOrderModel = marketPlaceOrderModelArrayList.get(position);
            if (!TextUtils.isEmpty(marketPlaceOrderModel.getProfileImage())) {
                Glide.with(context).load(marketPlaceOrderModel.getProfileImage()).into(holder.ivProfilePic);
            } else {
                holder.ivProfilePic.setImageDrawable(null);
            }
            holder.tvOrderId.setText(marketPlaceOrderModel.getOrderId());
            holder.tvName.setText(marketPlaceOrderModel.getName());
            holder.tvMobileNumber.setText(marketPlaceOrderModel.getMobileNumber());
            holder.tvEmailId.setText(marketPlaceOrderModel.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return marketPlaceOrderModelArrayList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName;
        private TextView tvMobileNumber;
        private TextView tvEmailId;
        private TextView tvOrderId;
        private ImageView ivCall;
        private ImageView ivMessage;
        private ImageView ivShowDirections;
        private ImageView ivProfilePic;

        public OrderViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvName = itemView.findViewById(R.id.tv_name);
            tvMobileNumber = itemView.findViewById(R.id.tv_mobile_number);
            tvEmailId = itemView.findViewById(R.id.tv_email_id);
            ivCall = itemView.findViewById(R.id.iv_call);
            ivMessage = itemView.findViewById(R.id.iv_message);
            ivShowDirections = itemView.findViewById(R.id.iv_show_directions);
            ivProfilePic = itemView.findViewById(R.id.iv_profile_pic);

            ivProfilePic.setOnClickListener(this);
            ivCall.setOnClickListener(this);
            ivMessage.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_profile_pic:
                    Utils.showProfile(context, marketPlaceOrderModelArrayList.get(getAdapterPosition()).getUserId());
                    break;
                case R.id.iv_call:
                    Utils.startCall(context, marketPlaceOrderModelArrayList.get(getAdapterPosition()).getMobileNumber());
                    break;
                case R.id.iv_message:
                    Utils.message(context, marketPlaceOrderModelArrayList.get(getAdapterPosition()).getMobileNumber());
                    break;
                default:
                    context.startActivity(MarketPlaceOrderDetailsActivity.getIntent(context, marketPlaceOrderModelArrayList.get(getAdapterPosition())));
                    break;
            }
        }
    }
}
