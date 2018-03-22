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
        try {
            HomeChefOrderModel homeChefOrderModel = homeChefOrderModelArrayList.get(position);

            holder.tvOrderId.setText("Order Id:-" + homeChefOrderModel.getOrderId());
            holder.tvName.setText(homeChefOrderModel.getFirstName() + " " + homeChefOrderModel.getLastName());
            holder.tvFoodName.setText(homeChefOrderModel.getDishName());
            holder.tvFoodType.setText(homeChefOrderModel.getOrderType());
            holder.tvNoOfPeople.setText(homeChefOrderModel.getMinGuest() + " to " + homeChefOrderModel.getMaxGuest() + " People");
            holder.tvPrice.setText(homeChefOrderModel.getPrice());
            holder.tvOrderTiming.setText(homeChefOrderModel.getOrderTime());
            holder.tvDiscount.setText(homeChefOrderModel.getDiscount());
            holder.tvRules.setText(Utils.getRulesText(homeChefOrderModel.getRules()));
            holder.tvDescription.setText(homeChefOrderModel.getDescription());

            holder.tvDiscount.setText(homeChefOrderModel.getDiscount());

            if (homeChefOrderModel.getFoodImagesArrayList() != null) {
                ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(context, homeChefOrderModelArrayList.get(position).getFoodImagesArrayList());
                holder.viewPager.setAdapter(imagePagerAdapter);
            }

            if (!TextUtils.isEmpty(homeChefOrderModelArrayList.get(position).getProfilePic()))
                Glide.with(context).load(homeChefOrderModelArrayList.get(position).getProfilePic()).into(holder.ivProfilePic);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return homeChefOrderModelArrayList.size();
    }

    private void bookOrder(String homeChefUserId, String orderId, String bookedDate, String orderBookedFor, int noOfPeople) {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(context, null);
            progressDialog.show();

            final FoodieBookOrderApiCall apiCall = new FoodieBookOrderApiCall(userModel.getUserId(), homeChefUserId, orderId, bookedDate, orderBookedFor, noOfPeople);
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
        private TextView tvOrderId, tvName, tvFoodName, tvFoodType, tvNoOfPeople, tvPrice, tvDiscount, tvOrderTiming, tvRules, tvDescription;
        private Button btnBookOrder;

        DiscoverViewHolder(View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.view_pager);
            ivProfilePic = itemView.findViewById(R.id.iv_profile_pic);

            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvName = itemView.findViewById(R.id.tv_name);
            tvFoodName = itemView.findViewById(R.id.tv_food_name);
            tvFoodType = itemView.findViewById(R.id.tv_food_type);
            tvNoOfPeople = itemView.findViewById(R.id.tv_no_of_people);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvDiscount = itemView.findViewById(R.id.tv_discount);
            tvOrderTiming = itemView.findViewById(R.id.tv_order_timing);
            tvRules = itemView.findViewById(R.id.tv_rules);
            tvDescription = itemView.findViewById(R.id.tv_description);

            btnBookOrder = itemView.findViewById(R.id.btn_book_order);
            btnBookOrder.setOnClickListener(this);
            ivProfilePic.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_book_order:
//                    bookOrder(homeChefOrderModelArrayList.get(getAdapterPosition()).getUserId(), homeChefOrderModelArrayList.get(getAdapterPosition()).getOrderId(), "2017-01-01", "1", 1);

                    DialogUtils.bookFoodOnSelectedDatesDialog(context, homeChefOrderModelArrayList.get(getAdapterPosition()), new HomeChefFoodTimingAdapter.BookOrderInterface() {
                        @Override
                        public void onOrderSelected(String foodDate, int foodTime, int noOfPerson) {
                            System.out.println(">>>>> onOrderSelected" + foodDate);
                            System.out.println(">>>>> onOrderSelected" + foodTime);
                            System.out.println(">>>>> onOrderSelected" + noOfPerson);
                            bookOrder(homeChefOrderModelArrayList.get(getAdapterPosition()).getUserId(), homeChefOrderModelArrayList.get(getAdapterPosition()).getOrderId(), foodDate, String.valueOf(foodTime), noOfPerson);

                        }
                    }, "");
                    break;
                case R.id.iv_profile_pic:
                    Utils.showProfile(context, homeChefOrderModelArrayList.get(getAdapterPosition()).getUserId());
                    break;
            }
        }
    }
}
