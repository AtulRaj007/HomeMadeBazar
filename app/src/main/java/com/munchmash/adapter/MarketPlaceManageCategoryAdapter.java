package com.munchmash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.munchmash.R;
import com.munchmash.model.MarketPlaceProductCategoryModel;

import java.util.ArrayList;

/**
 * Created by atulraj on 21/12/17.
 */

public class MarketPlaceManageCategoryAdapter extends RecyclerView.Adapter<MarketPlaceManageCategoryAdapter.CategoryViewHolder> {
    private Context context;
    private ArrayList<MarketPlaceProductCategoryModel> marketPlaceProductCategoryModelArrayList;

    public MarketPlaceManageCategoryAdapter(Context context, ArrayList<MarketPlaceProductCategoryModel> marketPlaceProductCategoryModelArrayList) {
        this.context = context;
        this.marketPlaceProductCategoryModelArrayList = marketPlaceProductCategoryModelArrayList;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_marketplace_manage_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        MarketPlaceProductCategoryModel marketPlaceProductCategoryModel = marketPlaceProductCategoryModelArrayList.get(position);
        if (!TextUtils.isEmpty(marketPlaceProductCategoryModel.getCategoryUrl())) {
            Glide.with(context).load(marketPlaceProductCategoryModel.getCategoryUrl()).into(holder.ivCategoryImage);
        }
        holder.tvCategoryId.setText(marketPlaceProductCategoryModel.getProductCategoryId());
        holder.tvCategoryName.setText(marketPlaceProductCategoryModel.getName());
        holder.tvCategoryDesc.setText(marketPlaceProductCategoryModel.getDescription());

    }

    @Override
    public int getItemCount() {
        return marketPlaceProductCategoryModelArrayList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategoryId, tvCategoryName, tvCategoryDesc;
        private ImageView ivCategoryImage;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            tvCategoryId = itemView.findViewById(R.id.tv_category_id);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            tvCategoryDesc = itemView.findViewById(R.id.tv_category_desc);
            ivCategoryImage = itemView.findViewById(R.id.iv_category_image);
        }
    }
}
