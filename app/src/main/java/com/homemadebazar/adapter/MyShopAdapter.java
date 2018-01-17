package com.homemadebazar.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.homemadebazar.R;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChefOrderModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.AddPromoteBusinessApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Sumit on 31/07/17.
 */

public class MyShopAdapter extends RecyclerView.Adapter<MyShopAdapter.MyShopViewHolder> {
    private Context context;
    private ArrayList<HomeChefOrderModel> homeChefOrderModelArrayList;
    private UserModel userModel;

    public MyShopAdapter(Context context, ArrayList<HomeChefOrderModel> homeChefOrderModelArrayList) {
        this.context = context;
        this.homeChefOrderModelArrayList = homeChefOrderModelArrayList;
        userModel = SharedPreference.getUserModel(context);
    }

    @Override
    public MyShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyShopViewHolder(LayoutInflater.from(context).inflate(R.layout.row_my_shop, parent, false));
    }

    @Override
    public void onBindViewHolder(MyShopViewHolder holder, int position) {
        try {
            HomeChefOrderModel homeChefOrderModel = homeChefOrderModelArrayList.get(position);
            holder.tvName.setText(homeChefOrderModel.getDishName());
            holder.tvDescription.setText(homeChefOrderModel.getDescription());

            holder.tvPrice.setText("Rs. " + homeChefOrderModel.getPrice());
            holder.tvServe.setText("Serves " + homeChefOrderModel.getMinGuest() + "-" + homeChefOrderModel.getMaxGuest());

            ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(context, homeChefOrderModel.getFoodImagesArrayList());
            holder.viewPager.setAdapter(imagePagerAdapter);
            holder.circleIndicator.setViewPager(holder.viewPager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return homeChefOrderModelArrayList.size();
    }

    class MyShopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName, tvDescription, tvPermoteBusiness;
        private ImageView ivDeleteOrder, ivEditOrder;
        private TextView tvDinner, tvServe, tvPrice, tvTiming;
        private ViewPager viewPager;
        private CircleIndicator circleIndicator;

        MyShopViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvPermoteBusiness = itemView.findViewById(R.id.tv_promote_business);

            ivDeleteOrder = itemView.findViewById(R.id.iv_delete_order);
            ivEditOrder = itemView.findViewById(R.id.iv_edit_order);
            tvDinner = itemView.findViewById(R.id.tv_dinner);
            tvServe = itemView.findViewById(R.id.tv_serve);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvTiming = itemView.findViewById(R.id.tv_timing);

            viewPager = itemView.findViewById(R.id.view_pager);
            circleIndicator = itemView.findViewById(R.id.indicator);

            ivDeleteOrder.setOnClickListener(this);
            ivEditOrder.setOnClickListener(this);
            tvPermoteBusiness.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_delete_order:
                    Toast.makeText(context, "Under Development", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.iv_edit_order:
                    Toast.makeText(context, "Under Development", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_promote_business:
                    applyForHotDeals(homeChefOrderModelArrayList.get(getAdapterPosition()).getOrderId());
                    break;
            }
        }

        void applyForHotDeals(String orderId) {
            try {
                final Dialog progressDialog = DialogUtils.getProgressDialog(context, null);
                progressDialog.show();

                final AddPromoteBusinessApiCall apiCall = new AddPromoteBusinessApiCall(orderId);
                HttpRequestHandler.getInstance(context.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                    @Override
                    public void onComplete(Exception e) {
                        DialogUtils.hideProgressDialog(progressDialog);
                        if (e == null) { // Success
                            try {
                                BaseModel baseModel = apiCall.getResult();

                                if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                    DialogUtils.showAlert(context, baseModel.getStatusMessage());
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
    }
}
