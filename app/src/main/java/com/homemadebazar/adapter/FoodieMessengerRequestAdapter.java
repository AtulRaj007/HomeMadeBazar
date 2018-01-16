package com.homemadebazar.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.homemadebazar.R;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.FoodieMessageRequestAcceptRejectApiCall;
import com.homemadebazar.util.CircleImageView;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.Utils;

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
        if (!TextUtils.isEmpty(reqDataList.get(position).getProfilePic()))
            Glide.with(context).load(reqDataList.get(position).getProfilePic()).into(holder.profileImage);

    }

    @Override
    public int getItemCount() {
        return reqDataList.size();
    }

    private void callAcceptRejectCallApi(String userId, String requestUserId, String actionType) {
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
                                Toast.makeText(context, "" + baseModel.getStatusMessage(), Toast.LENGTH_SHORT).show();
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

    class RequestViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView tvName, tvLastMessage;
        Button acceptReq, ignoreReq;

        RequestViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.iv_profile_pic);
            tvName = itemView.findViewById(R.id.tv_name);
            tvLastMessage = itemView.findViewById(R.id.tv_last_message);
            acceptReq = itemView.findViewById(R.id.btn_accept);
            ignoreReq = itemView.findViewById(R.id.btn_ignore);
            acceptReq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callAcceptRejectCallApi(userId, reqDataList.get(getAdapterPosition()).getUserId(), RequestType.ACCEPT_REQ);
                }
            });
            ignoreReq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callAcceptRejectCallApi(userId, reqDataList.get(getAdapterPosition()).getUserId(), RequestType.IGNORE_REQ);
                }
            });
        }
    }
}
