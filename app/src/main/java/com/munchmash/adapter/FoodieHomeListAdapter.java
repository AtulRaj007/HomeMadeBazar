package com.munchmash.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.munchmash.R;
import com.munchmash.activity.ShopDetailsActivity;
import com.munchmash.model.BaseModel;
import com.munchmash.model.CustomAddress;
import com.munchmash.model.HomeChiefNearByModel;
import com.munchmash.model.UserModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.DeleteFavouriteApiCall;
import com.munchmash.util.CircleImageView;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

import java.util.ArrayList;

/**
 * Created by atulraj on 22/11/17.
 */

public class FoodieHomeListAdapter extends RecyclerView.Adapter<FoodieHomeListAdapter.FoodieListViewHolder> {
    private ArrayList<HomeChiefNearByModel> homeChiefNearByModelArrayList;
    private Context context;
    private boolean isFavourite;
    private UserModel userModel;

    public FoodieHomeListAdapter(Context context, ArrayList<HomeChiefNearByModel> homeChiefNearByModelArrayList, boolean isFavourite) {
        this.context = context;
        this.homeChiefNearByModelArrayList = homeChiefNearByModelArrayList;
        this.isFavourite = isFavourite;
        userModel = SharedPreference.getUserModel(context);
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
            holder.tvAddress.setText(CustomAddress.getCompleteAddress(homeChiefNearByModelArrayList.get(position).getAddress()));
        }
        if (!TextUtils.isEmpty(homeChiefNearByModelArrayList.get(position).getProfileImage())) {
            Glide.with(context).load(homeChiefNearByModelArrayList.
                    get(position).getProfileImage())
                    .apply(new RequestOptions().placeholder(R.drawable.profile))
                    .into(holder.profilePic);
        } else {
            holder.profilePic.setImageResource(R.drawable.profile);
        }
        /*Added*/
        if (!isFavourite) {
            if (userModel.getCountryCode().equals("91"))
                holder.tvDistance.setText(Utils.getDistance(homeChiefNearByModelArrayList.get(position).getDistance(), 0) + " Km Away");
            else
                holder.tvDistance.setText(Utils.getDistance(homeChiefNearByModelArrayList.get(position).getDistance(), 1) + " Miles Away");
        }
    }

    @Override
    public int getItemCount() {
        return homeChiefNearByModelArrayList.size();
    }

    private void deleteFavourite(final int position) {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(context, null);
            progressDialog.show();

            final DeleteFavouriteApiCall apiCall = new DeleteFavouriteApiCall(userModel.getUserId(), homeChiefNearByModelArrayList.get(position).getUserId());
            HttpRequestHandler.getInstance(context.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    progressDialog.hide();
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                DialogUtils.showAlert(context, "HomeChef is removed from Favourites", new Runnable() {
                                    @Override
                                    public void run() {
                                        homeChiefNearByModelArrayList.remove(position);
                                        notifyDataSetChanged();
                                    }
                                });

                            } else {
                                DialogUtils.showAlert(context, baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), context, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), context, null);
        }
    }

    class FoodieListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView profilePic;
        private ImageView ivDeleteFavourite;
        private TextView tvName, tvAddress, tvShopName, tvDistance;

        FoodieListViewHolder(View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.iv_profile_pic);
            tvName = itemView.findViewById(R.id.tv_name);
            tvShopName = itemView.findViewById(R.id.tv_shop_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvDistance = itemView.findViewById(R.id.tv_distance);
            ivDeleteFavourite = itemView.findViewById(R.id.iv_delete_fav);
            if (isFavourite)
                ivDeleteFavourite.setVisibility(View.VISIBLE);
            itemView.setOnClickListener(this);
            profilePic.setOnClickListener(this);
            ivDeleteFavourite.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_profile_pic:
                    Utils.showProfile(context, homeChiefNearByModelArrayList.get(getAdapterPosition()).getUserId());
                    break;
                case R.id.iv_delete_fav:
                    deleteFavourite(getAdapterPosition());
                    break;
                default:
                    context.startActivity(ShopDetailsActivity.getIntent(context, homeChiefNearByModelArrayList.get(getAdapterPosition())));
            }
        }
    }
}
