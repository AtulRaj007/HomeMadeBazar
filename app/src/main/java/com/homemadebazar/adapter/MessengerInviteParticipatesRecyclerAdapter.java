package com.homemadebazar.adapter;

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

import com.homemadebazar.R;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.MessegeInviteParticipateModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.MessengerJoinSendRequestApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sonu on 12/9/2017.
 */

public class MessengerInviteParticipatesRecyclerAdapter extends RecyclerView.Adapter<MessengerInviteParticipatesRecyclerAdapter.InviteParticipateViewHolder> {
    private Context context;
    private ArrayList<MessegeInviteParticipateModel> dataList;
    private String userId;

    public MessengerInviteParticipatesRecyclerAdapter(Context context, ArrayList<MessegeInviteParticipateModel> dataList, String userId) {
        this.context = context;
        this.dataList = dataList;
        this.userId = userId;
    }

    @Override
    public InviteParticipateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.invite_participate_single_item, parent, false);
        return new InviteParticipateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InviteParticipateViewHolder holder, int position) {
        MessegeInviteParticipateModel messegeInviteParticipateModel = dataList.get(position);
        holder.name.setText(messegeInviteParticipateModel.getfName() + " " + messegeInviteParticipateModel.getlName());
        holder.address.setText(messegeInviteParticipateModel.getAddress());
        if (!TextUtils.isEmpty(messegeInviteParticipateModel.getProfileImage())) {
            Picasso.with(context).load(messegeInviteParticipateModel.getProfileImage()).into(holder.ivProfilePic);
        }
        holder.btnRequestType.setText(messegeInviteParticipateModel.getStatus());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void joinParticipateApiCall(String fromUserId, String toReqUserId) {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(context, null);
            progressDialog.show();

            final MessengerJoinSendRequestApiCall apiCall = new MessengerJoinSendRequestApiCall(fromUserId, toReqUserId);
            HttpRequestHandler.getInstance(context).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    progressDialog.hide();
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                DialogUtils.showAlert(context, "Friend Request has been sent.");

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

    class InviteParticipateViewHolder extends RecyclerView.ViewHolder {
        private TextView name, address;
        private ImageView ivProfilePic;
        private Button btnRequestType;

        public InviteParticipateViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            address = itemView.findViewById(R.id.tv_address);
            btnRequestType = itemView.findViewById(R.id.btn_request_type);
            ivProfilePic = itemView.findViewById(R.id.iv_profile_pic);

            btnRequestType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int numericStatus = dataList.get(getAdapterPosition()).getNumericStatus();
                    if (numericStatus == Constants.RequestType.REQUEST_NOT_SENT)
                        joinParticipateApiCall(userId, dataList.get(getAdapterPosition()).getUserId());
                }
            });
        }
    }
}
