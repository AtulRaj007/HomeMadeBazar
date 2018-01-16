package com.homemadebazar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homemadebazar.R;
import com.homemadebazar.activity.HomeShopDetailsActivity;
import com.homemadebazar.model.HomeChiefNearByModel;
import com.homemadebazar.util.CircleImageView;

import java.util.ArrayList;

/**
 * Created by atulraj on 22/11/17.
 */

public class FoodieHomeListAdapter extends RecyclerView.Adapter<FoodieHomeListAdapter.FoodieListViewHolder> {
    private ArrayList<HomeChiefNearByModel> homeChiefNearByModelArrayList;
    private Context context;

    public FoodieHomeListAdapter(Context context, ArrayList<HomeChiefNearByModel> homeChiefNearByModelArrayList) {
        this.context = context;
        this.homeChiefNearByModelArrayList = homeChiefNearByModelArrayList;
    }

    @Override
    public FoodieListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_foodie_home_list, parent, false);
        return new FoodieListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodieListViewHolder holder, int position) {
        holder.tvName.setText(homeChiefNearByModelArrayList.get(position).getFirstName() + " " + homeChiefNearByModelArrayList.get(position).getLastName());

        if (TextUtils.isEmpty(homeChiefNearByModelArrayList.get(position).getShopName())) {
            holder.tvShopName.setVisibility(View.GONE);
        } else {
            holder.tvShopName.setText(homeChiefNearByModelArrayList.get(position).getShopName() + "");
        }
        if (TextUtils.isEmpty(homeChiefNearByModelArrayList.get(position).getAddress())) {
            holder.tvAddress.setVisibility(View.GONE);
        } else {
            holder.tvAddress.setText(homeChiefNearByModelArrayList.get(position).getAddress() + "");
        }
        if (!TextUtils.isEmpty(homeChiefNearByModelArrayList.get(position).getProfileImage())) {
            try {
                Glide.with(context).load(homeChiefNearByModelArrayList.get(position).getProfileImage()).into(holder.profilePic);
                holder.tvDistance.setText(String.format("%.2f", homeChiefNearByModelArrayList.get(position).getDistance()) + " Meter Away");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return homeChiefNearByModelArrayList.size();
    }

    class FoodieListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView profilePic;
        private TextView tvName, tvAddress, tvShopName, tvDistance;

        FoodieListViewHolder(View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.iv_profile_pic);
            tvName = itemView.findViewById(R.id.tv_name);
            tvShopName = itemView.findViewById(R.id.tv_shop_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvDistance = itemView.findViewById(R.id.tv_distance);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            context.startActivity(HomeShopDetailsActivity.getIntent(context, homeChiefNearByModelArrayList.get(getAdapterPosition())));
        }
    }
}
