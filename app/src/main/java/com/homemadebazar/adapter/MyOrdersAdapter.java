package com.homemadebazar.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChefIncomingOrderModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.HomeChefFoodieOrderAcceptRejectApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sumit on 30/07/17.
 */

public class MyOrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private boolean isScheduledFragment = false;
    private ArrayList<HomeChefIncomingOrderModel> homeChefIncomingOrderModelArrayList;

    public MyOrdersAdapter(Context context, boolean isScheduledFragment, ArrayList<HomeChefIncomingOrderModel> homeChefIncomingOrderModelArrayList) {
        this.context = context;
        this.isScheduledFragment = isScheduledFragment;
        this.homeChefIncomingOrderModelArrayList = homeChefIncomingOrderModelArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_scheduled_separator_view, parent, false);
            return new TitleSeparatorViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_now_myorders, parent, false);
            return new MyOrdersViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HomeChefIncomingOrderModel homeChefIncomingOrderModel = homeChefIncomingOrderModelArrayList.get(position);
        if (holder instanceof MyOrdersViewHolder) {
            if (!TextUtils.isEmpty(homeChefIncomingOrderModel.getFoodiesDp())) {
                Picasso.with(context).load(homeChefIncomingOrderModel.getFoodiesDp()).into(((MyOrdersViewHolder) holder).ivProfile);
            }

            ((MyOrdersViewHolder) holder).tvName.setText(homeChefIncomingOrderModel.getFoodiesFirstName() + " " + homeChefIncomingOrderModel.getFoodiesLastName());
            ((MyOrdersViewHolder) holder).tvDesignation.setText("");
            ((MyOrdersViewHolder) holder).tvDishName.setText(homeChefIncomingOrderModel.getDishName());
            ((MyOrdersViewHolder) holder).tvOrderId.setText(homeChefIncomingOrderModel.getOrderId());
            ((MyOrdersViewHolder) holder).tvDateTime.setText(homeChefIncomingOrderModel.getOrderReqDate());
            ((MyOrdersViewHolder) holder).tvDishTime.setText(homeChefIncomingOrderModel.getOrderFor());


        } else if (holder instanceof TitleSeparatorViewHolder) {
            ((TitleSeparatorViewHolder) holder).tvTitle.setText(homeChefIncomingOrderModel.getDateTitle());
        }
    }


    @Override
    public int getItemViewType(int position) {

        if (homeChefIncomingOrderModelArrayList.get(position).getType() == 0)
            return 2;
        else
            return 1;
    }

    @Override
    public int getItemCount() {
        return homeChefIncomingOrderModelArrayList.size();
    }

    private void FoodieOrderAcceptReject(String bookingReferenceId, String responseType) {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(context, null);
            progressDialog.show();

            final HomeChefFoodieOrderAcceptRejectApiCall apiCall = new HomeChefFoodieOrderAcceptRejectApiCall(bookingReferenceId, responseType);
            HttpRequestHandler.getInstance(context.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
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

    interface OrderResponse {
        String ACCEPT = "2";
        String REJECT = "3";
    }

    class MyOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivProfile;
        private TextView tvName, tvDesignation, tvDishName, tvOrderId, tvDateTime, tvDishTime;
        private Button btnAccept, btnReject;

        MyOrdersViewHolder(View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDesignation = itemView.findViewById(R.id.tv_designation);
            tvDishName = itemView.findViewById(R.id.tv_dish_name);

            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);

            tvDishTime = itemView.findViewById(R.id.tv_dish_time);
            tvDishTime = itemView.findViewById(R.id.tv_dish_time);

            btnAccept = itemView.findViewById(R.id.btn_accept);
            btnReject = itemView.findViewById(R.id.btn_reject);

            btnAccept.setOnClickListener(this);
            btnReject.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_accept:
                    FoodieOrderAcceptReject(homeChefIncomingOrderModelArrayList.get(getAdapterPosition()).getOrderRequestId(), OrderResponse.ACCEPT);
                    break;
                case R.id.btn_reject:
                    FoodieOrderAcceptReject(homeChefIncomingOrderModelArrayList.get(getAdapterPosition()).getOrderRequestId(), OrderResponse.REJECT);
                    break;
            }
        }
    }

    class TitleSeparatorViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;

        public TitleSeparatorViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }
}
