package com.homemadebazar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homemadebazar.R;
import com.homemadebazar.activity.YouTubePlayerActivity;
import com.homemadebazar.model.HomeChefSkillHubVideoModel;
import com.homemadebazar.util.Constants;

import java.util.ArrayList;

/**
 * Created by Sumit on 31/07/17.
 */

public class SkillHubAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<HomeChefSkillHubVideoModel> homeChefSkillHubVideoModelArrayList;
    private Context context;

    public SkillHubAdapter(Context context, ArrayList<HomeChefSkillHubVideoModel> homeChefSkillHubVideoModelArrayList) {
        this.context = context;
        this.homeChefSkillHubVideoModelArrayList = homeChefSkillHubVideoModelArrayList;
    }

    @Override
    public int getItemViewType(int position) {
        return homeChefSkillHubVideoModelArrayList.get(position).getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.SkillHubViewType.TITLE) {
            return new TitleViewHolder((LayoutInflater.from(context).inflate(R.layout.row_title, parent, false)));
        } else {
            return new SkillHubViewHolder(LayoutInflater.from(context).inflate(R.layout.row_skill_hub, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder) {
            ((TitleViewHolder) holder).tvCategoryName.setText(homeChefSkillHubVideoModelArrayList.get(position).getCategoryName());
        } else {
            HomeChefSkillHubVideoModel homeChefSkillHubVideoModel = homeChefSkillHubVideoModelArrayList.get(position);
            ((SkillHubViewHolder) holder).tvName.setText(homeChefSkillHubVideoModel.getTitle());
            ((SkillHubViewHolder) holder).tvDescription.setText(homeChefSkillHubVideoModel.getDescription());
            if (!TextUtils.isEmpty(homeChefSkillHubVideoModel.getThumbNailUrl())) {
                Glide.with(context).load(homeChefSkillHubVideoModel.getThumbNailUrl()).into(((SkillHubViewHolder) holder).ivVideoThumbnail);
            }
        }

    }

    @Override
    public int getItemCount() {
        return homeChefSkillHubVideoModelArrayList.size();
    }

    class SkillHubViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName, tvDescription;
        private ImageView ivVideoThumbnail;

        SkillHubViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            ivVideoThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            HomeChefSkillHubVideoModel homeChefSkillHubVideoModel = homeChefSkillHubVideoModelArrayList.get(getAdapterPosition());
            Intent intent = new Intent(context, YouTubePlayerActivity.class);
            intent.putExtra("news_title", homeChefSkillHubVideoModel.getTitle());
            intent.putExtra("video_link", homeChefSkillHubVideoModel.getYoutubeUrl());
            context.startActivity(intent);
        }

    }

    class TitleViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategoryName;

        public TitleViewHolder(View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tv_title);
        }
    }


}
