package com.homemadebazar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.model.NotificationModel;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

/**
 * Created by Sumit on 07/08/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private ArrayList<NotificationModel> notificationModelArrayList;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> notificationModelArrayList) {
        this.context = context;
        this.notificationModelArrayList = notificationModelArrayList;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotificationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        NotificationModel notificationModel = notificationModelArrayList.get(position);
        holder.tvTitle.setText(notificationModel.getTitle());
        holder.tvMessage.setText(notificationModel.getMessage());
        holder.tvNotificationId.setText(Html.fromHtml("<font color='#000000'>Id#</font>" + "<font color='#A3A3A3'>" + notificationModel.getNotificationId() + "</font>"));
        holder.tvTime.setText(Utils.formatDateTwoLines(notificationModel.getNotificationTime()));
    }

    @Override
    public int getItemCount() {
        return notificationModelArrayList.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNotificationId, tvTitle, tvMessage, tvTime;

        NotificationViewHolder(View itemView) {
            super(itemView);
            tvNotificationId = itemView.findViewById(R.id.tv_notification_id);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
