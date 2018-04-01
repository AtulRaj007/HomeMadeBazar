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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.homemadebazar.R;
import com.homemadebazar.activity.ChatActivity;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.CustomAddress;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.FoodieUnfriendUserApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

/**
 * Created by atulraj on 22/11/17.
 */

public class FoodieMessengerFriendsAdapter extends RecyclerView.Adapter<FoodieMessengerFriendsAdapter.FriendsViewHolder> {
    private Context context;
    private ArrayList<UserModel> friendList;
    private UserModel userModel;

    public FoodieMessengerFriendsAdapter(Context context, ArrayList<UserModel> friendList) {
        this.context = context;
        this.friendList = friendList;
        userModel = SharedPreference.getUserModel(context);
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_foodie_messenger_friends, parent, false);
        return new FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        holder.tvName.setText(friendList.get(position).getFirstName() + " " + friendList.get(position).getLastName());
        holder.tvEmailId.setText(friendList.get(position).getEmailId());
        holder.tvAddress.setText(CustomAddress.getCompleteAddress(friendList.get(position).getAddress()));

        try {
            if (!TextUtils.isEmpty(friendList.get(position).getProfilePic()))
                Glide.with(context).load(friendList.get(position).getProfilePic())
                        .apply(new RequestOptions().placeholder(R.drawable.profile_square))
                        .into(holder.imageView);
            else
                holder.imageView.setImageResource(R.drawable.profile_square);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    private void unFriendUser(String unFriendUserId, final int position) {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(context, null);
            progressDialog.show();

            final FoodieUnfriendUserApiCall apiCall = new FoodieUnfriendUserApiCall(userModel.getUserId(), unFriendUserId);
            HttpRequestHandler.getInstance(context.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    progressDialog.hide();
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                DialogUtils.showAlert(context, "You have successfully unfriend the user", new Runnable() {
                                    @Override
                                    public void run() {
                                        friendList.remove(position);
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

    class FriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView tvName, tvEmailId, tvAddress;
        private Button btnUnfriend;

        FriendsViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_profile_pic);
            tvName = itemView.findViewById(R.id.tv_name);
            tvEmailId = itemView.findViewById(R.id.tv_email_id);
            tvAddress = itemView.findViewById(R.id.tv_address);
            btnUnfriend = itemView.findViewById(R.id.btn_unfriend);
            itemView.setOnClickListener(this);
            imageView.setOnClickListener(this);
            btnUnfriend.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_profile_pic:
                    Utils.showProfile(context, friendList.get(getAdapterPosition()).getUserId());
                    break;
                case R.id.btn_unfriend:
                    unFriendUser(friendList.get(getAdapterPosition()).getUserId(), getAdapterPosition());
                    break;
                default:
                    context.startActivity(ChatActivity.getChatIntent(context, friendList.get(getAdapterPosition())));

            }
        }
    }
}
