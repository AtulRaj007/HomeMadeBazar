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
import android.widget.Toast;

import com.homemadebazar.R;
import com.homemadebazar.model.HomeChefIncomingOrderModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sumit on 30/07/17.
 */

public class MyOrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private boolean isScheduledFragment = false;
    private ArrayList<HomeChefIncomingOrderModel> homeChefIncomingOrderModelArrayList;

//    public MyOrdersAdapter(Context context, ArrayList<HomeChefIncomingOrderModel> homeChefIncomingOrderModelArrayList) {
//        this.context = context;
//        this.homeChefIncomingOrderModelArrayList = homeChefIncomingOrderModelArrayList;
//    }

    public MyOrdersAdapter(Context context, boolean isScheduledFragment, ArrayList<HomeChefIncomingOrderModel> homeChefIncomingOrderModelArrayList) {
        this.context = context;
        this.isScheduledFragment = isScheduledFragment;
        this.homeChefIncomingOrderModelArrayList = homeChefIncomingOrderModelArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_scheduled_separator_view, parent, false);
            return new TitleSeparatorViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_now_myorders, parent, false);
            return new MyOrdersViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HomeChefIncomingOrderModel homeChefIncomingOrderModel = homeChefIncomingOrderModelArrayList.get(position);
        if (holder instanceof MyOrdersViewHolder) {
            if (!TextUtils.isEmpty(homeChefIncomingOrderModel.getFoodiesDp())) {
                Picasso.with(context).load(homeChefIncomingOrderModel.getFoodiesDp()).into(((MyOrdersViewHolder) holder).ivProfile);
            }

            ((MyOrdersViewHolder) holder).tvName.setText(homeChefIncomingOrderModel.getFoodiesFirstName() + " " + homeChefIncomingOrderModel.getFoodiesLastName());
            ((MyOrdersViewHolder) holder).tvDesignation.setText("");
            ((MyOrdersViewHolder) holder).tvDishName.setText(homeChefIncomingOrderModel.getDishName());

        } else if (holder instanceof TitleSeparatorViewHolder) {
            ((TitleSeparatorViewHolder) holder).tvTitle.setText(homeChefIncomingOrderModel.getDateTitle());
        }
    }


    @Override
    public int getItemViewType(int position) {
//        if (!isScheduledFragment)
//            return 2;
//        else {
//            if (position % 2 == 0)
//                return 1;
//            else
//                return 2;
//        }
        if (homeChefIncomingOrderModelArrayList.get(position).getType() == 0)
            return 2;
        else
            return 1;
    }

    @Override
    public int getItemCount() {
        return homeChefIncomingOrderModelArrayList.size();
    }

    class MyOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivProfile;
        private TextView tvName, tvDesignation, tvDishName;
        private Button btnAccept, btnReject;

        MyOrdersViewHolder(View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDesignation = itemView.findViewById(R.id.tv_designation);
            tvDishName = itemView.findViewById(R.id.tv_dish_name);

            btnAccept = itemView.findViewById(R.id.btn_accept);
            btnReject = itemView.findViewById(R.id.btn_reject);

            btnAccept.setOnClickListener(this);
            btnReject.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_accept:
                    Toast.makeText(context, "Under development", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_reject:
                    Toast.makeText(context, "Under development", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    class TitleSeparatorViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;

        public TitleSeparatorViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }
}
