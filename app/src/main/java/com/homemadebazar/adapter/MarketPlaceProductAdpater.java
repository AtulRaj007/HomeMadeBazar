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
import com.homemadebazar.R;
import com.homemadebazar.model.MarketPlaceProductModel;

import java.util.ArrayList;

/**
 * Created by sonu on 12/19/2017.
 */

public class MarketPlaceProductAdpater extends RecyclerView.Adapter<MarketPlaceProductAdpater.MyProductsHolder> {
    private Context context;
    private ArrayList<MarketPlaceProductModel> marketPlaceProductModelArrayList;

    public MarketPlaceProductAdpater(Context context, ArrayList<MarketPlaceProductModel> marketPlaceProductModelArrayList) {
        this.context = context;
        this.marketPlaceProductModelArrayList = marketPlaceProductModelArrayList;
    }


    @Override
    public MyProductsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_marketplace_product, parent, false);
        return new MyProductsHolder(view);
    }

    @Override
    public void onBindViewHolder(MyProductsHolder holder, int position) {
        MarketPlaceProductModel marketPlaceProductModel = marketPlaceProductModelArrayList.get(position);
        if (!TextUtils.isEmpty(marketPlaceProductModel.getImageUrl())) {
            Glide.with(context).load(marketPlaceProductModel.getImageUrl()).into(holder.ivProductImage);
        }
        holder.tvName.setText(marketPlaceProductModel.getProductName());
        holder.tvCategory.setText(marketPlaceProductModel.getCategory());
        holder.tvBrand.setText(marketPlaceProductModel.getBrand());
        holder.tvPrice.setText(marketPlaceProductModel.getPrice());
        holder.tvDescription.setText(marketPlaceProductModel.getDescription());
        holder.tvProductId.setText(marketPlaceProductModel.getProductId());

    }

    @Override
    public int getItemCount() {
        return marketPlaceProductModelArrayList.size();
    }

    class MyProductsHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvName, tvCategory, tvBrand, tvPrice, tvDescription, tvProductId;

        public MyProductsHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvBrand = itemView.findViewById(R.id.tv_brand);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvProductId = itemView.findViewById(R.id.tv_orderid);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
        }

    }
}
