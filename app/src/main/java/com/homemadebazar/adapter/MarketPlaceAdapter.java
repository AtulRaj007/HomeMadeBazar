package com.homemadebazar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.model.IngredientsRowsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sumit on 31/07/17.
 */

public class MarketPlaceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<IngredientsRowsModel> ingredientsRowsModelArrayList;

    public MarketPlaceAdapter(Context context, ArrayList<IngredientsRowsModel> ingredientsRowsModelArrayList) {
        this.context = context;
        this.ingredientsRowsModelArrayList = ingredientsRowsModelArrayList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new DishViewHolder(LayoutInflater.from(context).inflate(R.layout.row_list_marketplace_header, parent, false));
        else
            return new IngredientViewHolder(LayoutInflater.from(context).inflate(R.layout.row_list_marketplace, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IngredientsRowsModel ingredientsRowsModel = ingredientsRowsModelArrayList.get(position);
        if (holder instanceof DishViewHolder) {
            ((DishViewHolder) holder).tvDishName.setText(ingredientsRowsModel.getDishName());
        } else if(holder instanceof IngredientViewHolder){
            if(ingredientsRowsModel.getMarketPlaceProductModel()!=null) {
                if (!TextUtils.isEmpty(ingredientsRowsModel.getMarketPlaceProductModel().getImageUrl()))
                    Picasso.with(context).load(ingredientsRowsModel.getMarketPlaceProductModel().getImageUrl()).into(((IngredientViewHolder) holder).ivDishImage);
                ((IngredientViewHolder) holder).tvDishName.setText(ingredientsRowsModel.getMarketPlaceProductModel().getProductName());
                ((IngredientViewHolder) holder).tvDishQuantity.setText("");
                ((IngredientViewHolder) holder).tvDishDescription.setText(ingredientsRowsModel.getMarketPlaceProductModel().getDescription());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return ingredientsRowsModelArrayList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return ingredientsRowsModelArrayList.size();
    }

    class DishViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDishName;

        DishViewHolder(View itemView) {
            super(itemView);
            tvDishName = itemView.findViewById(R.id.tv_dish_name);
        }
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivDishImage;
        private TextView tvDishName;
        private TextView tvDishQuantity;
        private TextView tvDishDescription;

        IngredientViewHolder(View itemView) {
            super(itemView);
            ivDishImage = itemView.findViewById(R.id.iv_dish_image);
            tvDishName = itemView.findViewById(R.id.tv_dish_name);
            tvDishQuantity = itemView.findViewById(R.id.tv_dish_quantity);
            tvDishDescription = itemView.findViewById(R.id.tv_dish_desc);
        }
    }
}
