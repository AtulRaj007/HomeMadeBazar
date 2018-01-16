package com.homemadebazar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homemadebazar.R;
import com.homemadebazar.model.ChatMessageModel;
import com.homemadebazar.util.Constants;

import java.util.ArrayList;

/**
 * Created by atulraj on 3/1/18.
 */

public class ChatConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<ChatMessageModel> chatMessageModelArrayList;
    private String userId;

    public ChatConversationAdapter(Context context, ArrayList<ChatMessageModel> chatMessageModelArrayList, String userId) {
        this.context = context;
        this.chatMessageModelArrayList = chatMessageModelArrayList;
        this.userId = userId;
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }

    private int getViewType(int position) {
        try {
            ChatMessageModel chatMessageModel = chatMessageModelArrayList.get(position);
            String messageType = chatMessageModel.getMessageType();
            if (messageType.equals(Constants.MessageType.TEXT)) {
                if (chatMessageModel.getSenderId().equals(userId)) {
                    return Constants.MessageViewType.MESSAGE_TEXT_OWN;
                } else {
                    return Constants.MessageViewType.MESSAGE_TEXT_OTHER;
                }
            } else if (messageType.equals(Constants.MessageType.FILE)) {
                if (chatMessageModel.getSenderId().equals(userId)) {
                    return Constants.MessageViewType.MESSAGE_IMAGE_OWN;
                } else {
                    return Constants.MessageViewType.MESSAGE_IMAGE_OTHER;
                }

            } else if (messageType.equals(Constants.MessageType.LOCATION)) {
                if (chatMessageModel.getSenderId().equals(userId)) {
                    return Constants.MessageViewType.MESSAGE_LOCATION_OWN;
                } else {
                    return Constants.MessageViewType.MESSAGE_LOCATION_OTHER;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.parseInt(Constants.MessageType.TEXT);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == Constants.MessageViewType.MESSAGE_TEXT_OWN) {
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_own_text, parent, false);
            return new MessageViewHolder(view);
        } else if (viewType == Constants.MessageViewType.MESSAGE_TEXT_OTHER) {
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_other_text, parent, false);
            return new MessageViewHolder(view);
        } else if (viewType == Constants.MessageViewType.MESSAGE_IMAGE_OWN) {
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_own_image, parent, false);
            return new ImageViewHolder(view);
        } else if (viewType == Constants.MessageViewType.MESSAGE_IMAGE_OTHER) {
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_other_image, parent, false);
            return new ImageViewHolder(view);
        } else if (viewType == Constants.MessageViewType.MESSAGE_LOCATION_OWN) {
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_own_location, parent, false);
            return new LocationViewHolder(view);
        } else if (viewType == Constants.MessageViewType.MESSAGE_LOCATION_OTHER) {
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_other_location, parent, false);
            return new LocationViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {

            ChatMessageModel chatMessageModel = chatMessageModelArrayList.get(position);
            if (holder instanceof MessageViewHolder) {
                ((MessageViewHolder) holder).tvMessage.setText(chatMessageModel.getChatMessage());
                ((MessageViewHolder) holder).tvTime.setText(chatMessageModel.getSentTime());
            } else if (holder instanceof ImageViewHolder) {
                if (!TextUtils.isEmpty(chatMessageModel.getShareFile())) {
                    Glide.with(context).load(chatMessageModel.getShareFile()).into(((ImageViewHolder) holder).ivChatImage);
                }
                ((ImageViewHolder) holder).tvTime.setText(chatMessageModel.getSentTime());

            } else if (holder instanceof LocationViewHolder) {
                if (!TextUtils.isEmpty(chatMessageModel.getLatitude()) && !TextUtils.isEmpty(chatMessageModel.getLongitude())) {
                    String locationImageUrl = makeUrl(chatMessageModel.getLatitude(), chatMessageModel.getLongitude());
                    System.out.println("Location Image Url:-" + locationImageUrl);
                    Glide.with(context).load(locationImageUrl).into(((LocationViewHolder) holder).ivLocationImage);
//                    PicassoTrustAll.getInstance(context)
//                            .load(locationImageUrl)
//                            .into(((LocationViewHolder) holder).ivLocationImage);

                }
                ((LocationViewHolder) holder).tvTime.setText(chatMessageModel.getSentTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageModelArrayList.size();
    }

    public String makeUrl(String latitude, String longitude) {
//        String url = "https://maps.googleapis.com/maps/api/staticmap?zoom=15&size=300x150&maptype=roadmap&markers=color:red%7Clabel:⬤%7C" + latitude + "," + longitude + "&key=" + context.getResources().getString(R.string.google_api_key);
        String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + latitude + "," + longitude + "&zoom=12&size=200x200";
        Log.e("Map Image Url:-", url);
        return url;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMessage, tvTime;

        MessageViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivChatImage;
        private TextView tvTime;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ivChatImage = itemView.findViewById(R.id.iv_image);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivLocationImage;
        private TextView tvTime;

        public LocationViewHolder(View itemView) {
            super(itemView);
            ivLocationImage = itemView.findViewById(R.id.iv_location_image);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
