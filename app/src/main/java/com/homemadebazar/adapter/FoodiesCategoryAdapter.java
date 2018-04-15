package com.homemadebazar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.homemadebazar.R;
import com.homemadebazar.activity.FoodieSearchHomeChefActivity;
import com.homemadebazar.model.FoodCategoryModel;

import java.util.ArrayList;

/**
 * Created by atulraj on 17/12/17.
 */

public class FoodiesCategoryAdapter extends RecyclerView.Adapter<FoodiesCategoryAdapter.CategoryViewHolder> {
    private Context context;
    private ArrayList<FoodCategoryModel> foodCategoryModelArrayList;

    public FoodiesCategoryAdapter(Context context, ArrayList<FoodCategoryModel> foodCategoryModelArrayList) {
        this.context = context;
        this.foodCategoryModelArrayList = foodCategoryModelArrayList;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_foodie_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        FoodCategoryModel foodCategoryModel = foodCategoryModelArrayList.get(position);
        if (!TextUtils.isEmpty(foodCategoryModel.getThumbnail())) {
            Glide.with(context)
                    .load(foodCategoryModel.getThumbnail())
                    .apply(new RequestOptions().override(400, 400))
                    .into(holder.ivCategoryImage);
        }
        holder.tvCategoryTitle.setText(foodCategoryModel.getName());
    }

    @Override
    public int getItemCount() {
        return foodCategoryModelArrayList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivCategoryImage;
        private TextView tvCategoryTitle;

        CategoryViewHolder(View itemView) {
            super(itemView);
            ivCategoryImage = itemView.findViewById(R.id.iv_category_image);
            tvCategoryTitle = itemView.findViewById(R.id.tv_category_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            context.startActivity(FoodieSearchHomeChefActivity.getFoodieSearchIntent(context, foodCategoryModelArrayList.get(getAdapterPosition()).getFoodCategoryId()));
        }
    }
}
