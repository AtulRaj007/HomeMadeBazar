package com.homemadebazar.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homemadebazar.R;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChefOrderModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.FoodieBookOrderApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;


/**
 * Created by atulraj on 22/11/17.
 */

public class FoodieDiscoverAdapter extends RecyclerView.Adapter<FoodieDiscoverAdapter.DiscoverViewHolder> {
    private Context context;
    private ArrayList<HomeChefOrderModel> homeChefOrderModelArrayList;
    private UserModel userModel;

    public FoodieDiscoverAdapter(Context context, ArrayList<HomeChefOrderModel> homeChefOrderModelArrayList) {
        this.context = context;
        this.homeChefOrderModelArrayList = homeChefOrderModelArrayList;
        userModel = SharedPreference.getUserModel(context);
    }

    @Override
    public DiscoverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_foodie_discover, parent, false);
        return new DiscoverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DiscoverViewHolder holder, int position) {
        ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(context, homeChefOrderModelArrayList.get(position).getFoodImagesArrayList());
        holder.viewPager.setAdapter(imagePagerAdapter);

        holder.tvFoodName.setText(homeChefOrderModelArrayList.get(position).getDishName());
        holder.tvFoodSpeciality.setText(homeChefOrderModelArrayList.get(position).getCategory());
        holder.tvDiscount.setText(homeChefOrderModelArrayList.get(position).getDiscount());

        holder.tvName.setText(homeChefOrderModelArrayList.get(position).getFirstName() + " " + homeChefOrderModelArrayList.get(position).getLastName());
        holder.tvRating.setText("5.0");
        if (!TextUtils.isEmpty(homeChefOrderModelArrayList.get(position).getProfilePic()))
            Glide.with(context).load(homeChefOrderModelArrayList.get(position).getProfilePic()).into(holder.ivProfilePic);
    }

    @Override
    public int getItemCount() {
        return homeChefOrderModelArrayList.size();
    }

    public void bookOrder(String homeChefUserId, String orderId, String bookedDate, String orderBookedFor) {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(context, null);
            progressDialog.show();

            final FoodieBookOrderApiCall apiCall = new FoodieBookOrderApiCall(userModel.getUserId(), homeChefUserId, orderId, bookedDate, orderBookedFor);
            HttpRequestHandler.getInstance(context.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    progressDialog.hide();
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                DialogUtils.showAlert(context, "Order is successfully booked" + "\n Booking Id is :-" + apiCall.getBookingId());
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

    class DiscoverViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ViewPager viewPager;
        private ImageView ivProfilePic;
        private TextView tvName, tvRating, tvFoodName, tvFoodSpeciality, tvDiscount;
        private Button btnBookOrder;

        DiscoverViewHolder(View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.view_pager);
            ivProfilePic = itemView.findViewById(R.id.iv_profile_pic);

            tvName = itemView.findViewById(R.id.tv_name);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvFoodName = itemView.findViewById(R.id.tv_food_name);
            tvFoodSpeciality = itemView.findViewById(R.id.tv_food_speciality);
            tvDiscount = itemView.findViewById(R.id.tv_discount);

            btnBookOrder = itemView.findViewById(R.id.btn_book_order);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_book_order:
                    bookOrder(homeChefOrderModelArrayList.get(getAdapterPosition()).getUserId(), homeChefOrderModelArrayList.get(getAdapterPosition()).getOrderId(), "2017-01-01", "1");
                    break;
            }
        }
    }
}
