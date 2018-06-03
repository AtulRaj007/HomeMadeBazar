package com.munchmash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.munchmash.R;
import com.munchmash.activity.CheckInActivity;
import com.munchmash.model.CustomAddress;
import com.munchmash.model.FoodieCheckInModel;
import com.munchmash.util.CircleImageView;
import com.munchmash.util.Utils;

import java.util.ArrayList;

/**
 * Created by atulraj on 6/3/18.
 */

public class FoodieCheckInAdapter extends RecyclerView.Adapter<FoodieCheckInAdapter.CheckInViewHolder> {
    private ArrayList<FoodieCheckInModel> foodieCheckInModelArrayList;
    private Context context;

    public FoodieCheckInAdapter(Context context, ArrayList<FoodieCheckInModel> foodieCheckInModelArrayList) {
        this.foodieCheckInModelArrayList = foodieCheckInModelArrayList;
        this.context = context;
    }

    @Override
    public CheckInViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CheckInViewHolder(LayoutInflater.from(context).inflate(R.layout.row_foodie_check_in, parent, false));
    }

    @Override
    public void onBindViewHolder(CheckInViewHolder holder, int position) {
        holder.tvName.setText(foodieCheckInModelArrayList.get(position).getFirstName() + " " + foodieCheckInModelArrayList.get(position).getLastName());
        holder.tvAddress.setText(CustomAddress.getCompleteAddress(foodieCheckInModelArrayList.get(position).getAddress()));
        holder.tvShopName.setText(foodieCheckInModelArrayList.get(position).getShopName());
        if (!TextUtils.isEmpty(foodieCheckInModelArrayList.get(position).getImageUrl())) {
            Glide.with(context).load(foodieCheckInModelArrayList.get(position).getImageUrl()).into(holder.ivProfilePic);
        }
    }

    @Override
    public int getItemCount() {
        return foodieCheckInModelArrayList.size();
    }

    class CheckInViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName, tvShopName, tvAddress;
        private CircleImageView ivProfilePic;

        CheckInViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvShopName = itemView.findViewById(R.id.tv_shop_name);
            ivProfilePic = itemView.findViewById(R.id.iv_profile_pic);
            itemView.setOnClickListener(this);
            ivProfilePic.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_profile_pic:
                    Utils.showProfile(context, foodieCheckInModelArrayList.get(getAdapterPosition()).getUserId());
                    break;
                default:
                    ((CheckInActivity) context).onCheckInSelected(getAdapterPosition());
                    break;
            }
        }
    }
}
