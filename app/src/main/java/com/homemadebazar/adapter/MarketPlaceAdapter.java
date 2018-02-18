package com.homemadebazar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homemadebazar.R;
import com.homemadebazar.model.MarketPlaceProductModel;
import com.homemadebazar.shopping.MarketPlaceShoppingCart;

import java.util.ArrayList;

/**
 * Created by Sumit on 31/07/17.
 */

public class MarketPlaceAdapter extends RecyclerView.Adapter<MarketPlaceAdapter.ProductViewHolder> {

    private Context context;
    private ArrayList<MarketPlaceProductModel> marketPlaceProductModelArrayList;
    private MarketPlaceShoppingCart marketPlaceShoppingCart;

    public MarketPlaceAdapter(Context context, ArrayList<MarketPlaceProductModel> marketPlaceProductModelArrayList, MarketPlaceShoppingCart marketPlaceShoppingCart) {
        this.context = context;
        this.marketPlaceProductModelArrayList = marketPlaceProductModelArrayList;
        this.marketPlaceShoppingCart = marketPlaceShoppingCart;
    }


    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.row_list_marketplace, parent, false));
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        MarketPlaceProductModel productModel = marketPlaceProductModelArrayList.get(position);
        if (!TextUtils.isEmpty(productModel.getImageUrl()))
            Glide.with(context).load(productModel.getImageUrl()).into(holder.ivProductImage);
        holder.tvProductName.setText(productModel.getProductName());
        holder.tvDescription.setText(productModel.getDescription());
        holder.tvBrandName.setText(productModel.getBrand());
        holder.tvPrice.setText(productModel.getPrice());
        if (productModel.getStatus() == 0) {
            holder.btnAdd.setText("ADD");
        } else {
            holder.btnAdd.setText("REMOVE");
        }
    }

    @Override
    public int getItemCount() {
        return marketPlaceProductModelArrayList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivProductImage;
        private TextView tvProductName;
        private TextView tvBrandName;
        private TextView tvDescription;
        private Button btnAdd;
        private TextView tvPrice;

        ProductViewHolder(View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvBrandName = itemView.findViewById(R.id.tv_brand);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvPrice = itemView.findViewById(R.id.tv_price);
            btnAdd = itemView.findViewById(R.id.btn_add);
            btnAdd.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_add:
                    if (marketPlaceProductModelArrayList.get(getAdapterPosition()).getStatus() == 0) {
                        marketPlaceProductModelArrayList.get(getAdapterPosition()).setStatus(1);
                        marketPlaceShoppingCart.addProductToCart(context, marketPlaceProductModelArrayList.get(getAdapterPosition()));
                        notifyDataSetChanged();
                    } else {
                        marketPlaceProductModelArrayList.get(getAdapterPosition()).setStatus(0);
                        marketPlaceShoppingCart.removeProductToCart(context, marketPlaceProductModelArrayList.get(getAdapterPosition()));
                        notifyDataSetChanged();
                    }
                    break;
            }
        }
    }
}
