package com.munchmash.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.munchmash.R;
import com.munchmash.activity.FoodiePostCommentActivity;
import com.munchmash.model.BaseModel;
import com.munchmash.model.FoodieFlashPostModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.FoodiePostLikeCommentApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.Utils;

import java.util.ArrayList;


/**
 * Created by sonu on 12/9/2017.
 */

public class FoodieFlashPostAdapter extends RecyclerView.Adapter<FoodieFlashPostAdapter.FlashRecyclerHolder> {
    private Context context;
    private ArrayList<FoodieFlashPostModel> foodieFlashPostModelArrayList;
    private String userId;

    public FoodieFlashPostAdapter(Context context, String userId, ArrayList<FoodieFlashPostModel> foodieFlashPostModelArrayList) {
        this.context = context;
        this.foodieFlashPostModelArrayList = foodieFlashPostModelArrayList;
        this.userId = userId;
    }

    @Override
    public FlashRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_flash_foodie, parent, false);
        return new FlashRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(FlashRecyclerHolder holder, int position) {
        FoodieFlashPostModel foodieFlashPostModel = foodieFlashPostModelArrayList.get(position);
        if (!TextUtils.isEmpty(foodieFlashPostModel.getUserProfileUrl()))
            Glide.with(context).load(foodieFlashPostModel.getUserProfileUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.profile_square))
                    .into(holder.ivProfileImage);
        else
            holder.ivProfileImage.setImageResource(R.drawable.profile_square);

        holder.tvName.setText(foodieFlashPostModel.getUserFirstName() + " " + foodieFlashPostModel.getUserLastName());
        holder.tvMessage.setText(foodieFlashPostModel.getPostMessage());
        holder.tvDateTime.setText(foodieFlashPostModel.getPostDateTime());

        holder.tvLikeCount.setText("(" + foodieFlashPostModel.getNoOfLikes() + ")");
        holder.tvCommentCount.setText("(" + foodieFlashPostModel.getNoOfComments() + ")");

        if (!TextUtils.isEmpty(foodieFlashPostModel.getPostImageUrl()))
            Glide.with(context).load(foodieFlashPostModel.getPostImageUrl())
                    .apply(new RequestOptions().placeholder(R.color.grey))
                    .into(holder.ivPostImage);
        else
            holder.ivPostImage.setImageResource(R.color.grey);
    }

    @Override
    public int getItemCount() {
        return foodieFlashPostModelArrayList.size();
    }


    private void performLikeUnlikeComment(final FoodieFlashPostModel foodieFlashPostModel, final String actionType, String comments, String actionDoneByUserId) {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(context, null);
            progressDialog.show();

            final FoodiePostLikeCommentApiCall apiCall = new FoodiePostLikeCommentApiCall(foodieFlashPostModel.getPostId(), actionType, comments, actionDoneByUserId);
            HttpRequestHandler.getInstance(context.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                foodieFlashPostModel.setNoOfComments(apiCall.getCommentsCount());
                                foodieFlashPostModel.setNoOfLikes(apiCall.getLikesCount());
                                if (actionType == Constants.PostActionTAG.LIKES) {
                                    foodieFlashPostModel.setLike(true);
                                } else {
                                    foodieFlashPostModel.setLike(false);
                                }
                                notifyDataSetChanged();
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


    class FlashRecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivProfileImage, ivPostImage;
        private TextView tvName, tvDateTime;
        private TextView tvMessage;
        private LinearLayout llLike, llComment, llShare;
        private TextView tvLikeCount, tvCommentCount;

        FlashRecyclerHolder(View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.iv_profile_pic);
            ivPostImage = itemView.findViewById(R.id.iv_post_image);
            tvName = itemView.findViewById(R.id.tv_user_name);
            tvDateTime = itemView.findViewById(R.id.tv_post_datetime);
            tvMessage = itemView.findViewById(R.id.tv_message);
            llLike = itemView.findViewById(R.id.ll_like);
            llComment = itemView.findViewById(R.id.ll_comment);
            llShare = itemView.findViewById(R.id.ll_share);
            tvLikeCount = itemView.findViewById(R.id.tv_like_count);
            tvCommentCount = itemView.findViewById(R.id.tv_comment_count);
            llLike.setOnClickListener(this);
            llShare.setOnClickListener(this);
            llComment.setOnClickListener(this);
            ivProfileImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_like:
                    if (foodieFlashPostModelArrayList.get(getAdapterPosition()).isLike())
                        performLikeUnlikeComment(foodieFlashPostModelArrayList.get(getAdapterPosition()), Constants.PostActionTAG.UNLIKE, "", userId);
                    else
                        performLikeUnlikeComment(foodieFlashPostModelArrayList.get(getAdapterPosition()), Constants.PostActionTAG.LIKES, "", userId);
                    break;
                case R.id.ll_comment:
                    context.startActivity(FoodiePostCommentActivity.getCommentIntent(context, foodieFlashPostModelArrayList.get(getAdapterPosition()).getPostId()));
                    break;
                case R.id.ll_share:
                    try {
                        Bitmap screenShotBitmap = Utils.takeScreenshot(itemView);
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, "download this image");
                        String bitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), screenShotBitmap, "title", null);
                        Uri bitmapUri = Uri.parse(bitmapPath);
                        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                        intent.setType("image/*");
                        context.startActivity(Intent.createChooser(intent, "Share image via..."));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case R.id.iv_profile_pic:
                    Utils.showProfile(context, foodieFlashPostModelArrayList.get(getAdapterPosition()).getUserId());
                    break;
            }
        }
    }
}
