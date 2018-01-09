package com.homemadebazar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.activity.ChatActivity;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.util.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by atulraj on 22/11/17.
 */

public class FoodieMessengerFriendsAdapter extends RecyclerView.Adapter<FoodieMessengerFriendsAdapter.FriendsViewHolder> {
    private Context context;
    private ArrayList<UserModel> friendList;

    public FoodieMessengerFriendsAdapter(Context context, ArrayList<UserModel> friendList) {
        this.context = context;
        this.friendList = friendList;
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_foodie_messenger_friends, parent, false);
        return new FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        holder.tvName.setText(friendList.get(position).getFirstName() + " " + friendList.get(position).getLastName());
        try {
            if (!TextUtils.isEmpty(friendList.get(position).getProfilePic()))
                Picasso.with(context).load(friendList.get(position).getProfilePic()).into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    class FriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView imageView;
        TextView tvName, tvLastMessage, tvTime;

        FriendsViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_profile_pic);
            tvName = itemView.findViewById(R.id.tv_name);
            tvLastMessage = itemView.findViewById(R.id.tv_last_message);
            tvTime = itemView.findViewById(R.id.tv_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            context.startActivity(ChatActivity.getChatIntent(context, friendList.get(getAdapterPosition())));
        }
    }
}
