package com.homemadebazar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.model.ChatMessageModel;
import com.homemadebazar.model.FoodiePostCommentModel;
import com.homemadebazar.util.Constants;

import java.util.ArrayList;

/**
 * Created by atulraj on 3/1/18.
 */

public class PostCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<FoodiePostCommentModel> foodiePostCommentModelArrayList;
    private String userId;

    public PostCommentAdapter(Context context, ArrayList<FoodiePostCommentModel> foodiePostCommentModelArrayList, String userId) {
        this.context = context;
        this.foodiePostCommentModelArrayList = foodiePostCommentModelArrayList;
        this.userId = userId;
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }

    private int getViewType(int position) {
        try {
            String commentUserId = foodiePostCommentModelArrayList.get(position).getUserId();
            if (userId.equals(commentUserId)) {
                return Constants.MessageViewType.MESSAGE_TEXT_OWN;
            } else {
                return Constants.MessageViewType.MESSAGE_TEXT_OTHER;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.parseInt(Constants.MessageType.TEXT);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == Constants.CommentViewType.MESSAGE_TEXT_OWN) {
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_own_text, parent, false);
            return new CommentViewHolder(view);
        } else if (viewType == Constants.CommentViewType.MESSAGE_TEXT_OTHER) {
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_other_text, parent, false);
            return new CommentViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            FoodiePostCommentModel foodiePostCommentModel = foodiePostCommentModelArrayList.get(position);
            if (holder instanceof CommentViewHolder) {
                ((CommentViewHolder) holder).tvMessage.setText(foodiePostCommentModel.getComments());
                ((CommentViewHolder) holder).tvTime.setText(foodiePostCommentModel.getSentTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return foodiePostCommentModelArrayList.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMessage, tvTime;

        CommentViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }

}
