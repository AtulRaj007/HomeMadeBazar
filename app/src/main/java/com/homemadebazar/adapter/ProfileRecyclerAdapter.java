package com.homemadebazar.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.model.ProfileInterestsModel;

import java.util.ArrayList;

/**
 * Created by atulraj on 4/3/18.
 */

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.ProfileViewHolder> {

    private Context context;
    private ArrayList<ProfileInterestsModel> profileInterestsModels;
    private boolean isEditable;

    public ProfileRecyclerAdapter(Context context, ArrayList<ProfileInterestsModel> profileInterestsModels, boolean isEditable) {
        this.context = context;
        this.profileInterestsModels = profileInterestsModels;
        this.isEditable = isEditable;
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProfileViewHolder(LayoutInflater.from(context).inflate(R.layout.row_profile_interest, parent, false));
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.ivInterestIcon.setClipToOutline(true);
        }
        holder.ivInterestIcon.setImageResource(profileInterestsModels.get(position).getIconId());
        holder.tvTitle.setText(profileInterestsModels.get(position).getInterestName());
        if (profileInterestsModels.get(position).isSelected()) {
            holder.ivProfileSelected.setVisibility(View.VISIBLE);
        } else {
            holder.ivProfileSelected.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return profileInterestsModels.size();
    }

    class ProfileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivInterestIcon;
        private TextView tvTitle;
        private ImageView ivProfileSelected;

        ProfileViewHolder(View itemView) {
            super(itemView);
            ivInterestIcon = itemView.findViewById(R.id.iv_interest_icon);
            ivProfileSelected = itemView.findViewById(R.id.iv_profile_selected);
            tvTitle = itemView.findViewById(R.id.tv_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (isEditable) {
                profileInterestsModels.get(getAdapterPosition()).setSelected(!profileInterestsModels.get(getAdapterPosition()).isSelected());
                notifyDataSetChanged();
            }
        }
    }
}
