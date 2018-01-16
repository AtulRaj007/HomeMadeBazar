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

import java.util.ArrayList;

/**
 * Created by Sumit on 31/07/17.
 */

public class SkillHubAdapter extends RecyclerView.Adapter<SkillHubAdapter.SkillHubViewHolder> {
    ArrayList<HomeChefSkillHubVideoModel> homeChefSkillHubVideoModelArrayList;
    private Context context;

    public SkillHubAdapter(Context context, ArrayList<HomeChefSkillHubVideoModel> homeChefSkillHubVideoModelArrayList) {
        this.context = context;
        this.homeChefSkillHubVideoModelArrayList = homeChefSkillHubVideoModelArrayList;
    }

    @Override
    public SkillHubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SkillHubViewHolder(LayoutInflater.from(context).inflate(R.layout.row_skill_hub, parent, false));
    }

    @Override
    public void onBindViewHolder(SkillHubViewHolder holder, int position) {
        HomeChefSkillHubVideoModel homeChefSkillHubVideoModel = homeChefSkillHubVideoModelArrayList.get(position);
        holder.tvName.setText(homeChefSkillHubVideoModel.getTitle());
        holder.tvDescription.setText(homeChefSkillHubVideoModel.getDescription());
        if (!TextUtils.isEmpty(homeChefSkillHubVideoModel.getThumbNailUrl())) {
            Glide.with(context).load(homeChefSkillHubVideoModel.getThumbNailUrl()).into(holder.ivVideoThumbnail);
        }

    }

    @Override
    public int getItemCount() {
        return homeChefSkillHubVideoModelArrayList.size();
    }

    class SkillHubViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName, tvDescription;
        private ImageView ivVideoThumbnail;

        public SkillHubViewHolder(View itemView) {
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
}
