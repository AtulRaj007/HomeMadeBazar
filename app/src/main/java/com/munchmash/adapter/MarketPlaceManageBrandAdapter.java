package com.munchmash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.munchmash.R;
import com.munchmash.model.MarketPlaceProductBrandModel;

import java.util.ArrayList;

/**
 * Created by atulraj on 21/12/17.
 */

public class MarketPlaceManageBrandAdapter extends RecyclerView.Adapter<MarketPlaceManageBrandAdapter.BrandViewHolder> {
    private Context context;
    private ArrayList<MarketPlaceProductBrandModel> marketPlaceProductBrandModelArrayList;

    public MarketPlaceManageBrandAdapter(Context context, ArrayList<MarketPlaceProductBrandModel> marketPlaceProductBrandModelArrayList) {
        this.context = context;
        this.marketPlaceProductBrandModelArrayList = marketPlaceProductBrandModelArrayList;
    }

    @Override
    public BrandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_marketplace_manage_brand, parent, false);
        return new BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BrandViewHolder holder, int position) {
        holder.tvBrandId.setText(marketPlaceProductBrandModelArrayList.get(position).getBrandId());
        holder.tvBrandName.setText(marketPlaceProductBrandModelArrayList.get(position).getBrandName());
        holder.tvBrandDesc.setText(marketPlaceProductBrandModelArrayList.get(position).getBrandDesc());
    }

    @Override
    public int getItemCount() {
        return marketPlaceProductBrandModelArrayList.size();
    }

    class BrandViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBrandId, tvBrandName, tvBrandDesc;

        BrandViewHolder(View itemView) {
            super(itemView);
            tvBrandId = itemView.findViewById(R.id.tv_brand_id);
            tvBrandName = itemView.findViewById(R.id.tv_brand_name);
            tvBrandDesc = itemView.findViewById(R.id.tv_brand_desc);
        }
    }
}
