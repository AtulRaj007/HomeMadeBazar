package com.munchmash.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.munchmash.R;
import com.munchmash.model.BaseModel;
import com.munchmash.model.CustomAddress;
import com.munchmash.model.UserModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.FoodieMessageRequestAcceptRejectApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.Utils;

import java.util.ArrayList;

/**
 * Created by atulraj on 22/11/17.
 */

public class FoodieMessengerRequestAdapter extends RecyclerView.Adapter<FoodieMessengerRequestAdapter.RequestViewHolder> {
    private Context context;
    private String userId;
    private ArrayList<UserModel> reqDataList;

    public FoodieMessengerRequestAdapter(Context context, String userId, ArrayList<UserModel> reqDataList) {
        this.context = context;
        this.userId = userId;
        this.reqDataList = reqDataList;
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_foodie_messenger_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, int position) {
        holder.tvName.setText(reqDataList.get(position).getFirstName() + " " + reqDataList.get(position).getLastName());
        holder.tvEmail.setText(reqDataList.get(position).getEmailId());
        holder.tvAddress.setText(CustomAddress.getCompleteAddress(reqDataList.get(position).getAddress()));

        if (!TextUtils.isEmpty(reqDataList.get(position).getProfilePic()))
            Glide.with(context).load(reqDataList.get(position).getProfilePic())
                    .apply(new RequestOptions().placeholder(R.drawable.profile_square))
                    .into(holder.profileImage);
        else
            holder.profileImage.setImageResource(R.drawable.profile_square);

    }

    @Override
    public int getItemCount() {
        return reqDataList.size();
    }

    private void callAcceptRejectCallApi(String userId, String requestUserId, String actionType, final int position) {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(context, "Please wait..");
            progressDialog.show();

            final FoodieMessageRequestAcceptRejectApiCall apiCall = new FoodieMessageRequestAcceptRejectApiCall(userId, requestUserId, actionType);
            HttpRequestHandler.getInstance(context).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    progressDialog.hide();
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                DialogUtils.showAlert(context, "Friend request accepted", new Runnable() {
                                    @Override
                                    public void run() {
                                        reqDataList.remove(position);
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

    private interface RequestType {
        String ACCEPT_REQ = "3";
        String IGNORE_REQ = "4";
    }

    class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView profileImage;
        private TextView tvName, tvEmail, tvAddress;
        private Button btnAccept, btnReject;

        RequestViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.iv_profile_pic);
            tvName = itemView.findViewById(R.id.tv_name);
            tvEmail = itemView.findViewById(R.id.tv_emailId);
            tvAddress = itemView.findViewById(R.id.tv_address);
            btnAccept = itemView.findViewById(R.id.btn_accept);
            btnReject = itemView.findViewById(R.id.btn_ignore);
            btnAccept.setOnClickListener(this);
            btnReject.setOnClickListener(this);
            profileImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_accept:
                    callAcceptRejectCallApi(userId, reqDataList.get(getAdapterPosition()).getUserId(), RequestType.ACCEPT_REQ, getAdapterPosition());
                    break;
                case R.id.btn_ignore:
                    callAcceptRejectCallApi(userId, reqDataList.get(getAdapterPosition()).getUserId(), RequestType.IGNORE_REQ, getAdapterPosition());
                    break;
                case R.id.iv_profile_pic:
                    Utils.showProfile(context, reqDataList.get(getAdapterPosition()).getUserId());
                    break;
            }
        }
    }
}
