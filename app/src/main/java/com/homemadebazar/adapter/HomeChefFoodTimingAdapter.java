package com.homemadebazar.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.activity.HomeShopViewActivity;
import com.homemadebazar.model.HomeChefOrderModel;
import com.homemadebazar.util.DialogUtils;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by atulraj on 16/12/17.
 */

public class HomeChefFoodTimingAdapter extends RecyclerView.Adapter<HomeChefFoodTimingAdapter.LunchViewHolder> {
    private Context context;
    private ArrayList<HomeChefOrderModel> homeChefOrderModelArrayList;
    private String homeChefUserId;
    private String foodTimeType;

    public HomeChefFoodTimingAdapter(Context context, String homeChefUserId, ArrayList<HomeChefOrderModel> homeChefOrderModelArrayList, String foodTimeType) {
        this.context = context;
        this.homeChefOrderModelArrayList = homeChefOrderModelArrayList;
        this.homeChefUserId = homeChefUserId;
        this.foodTimeType = foodTimeType;
    }

    @Override
    public LunchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_homechef_lunch, parent, false);
        return new LunchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LunchViewHolder holder, int position) {
        HomeChefOrderModel homeChefOrderModel = homeChefOrderModelArrayList.get(position);
        holder.tvFoodName.setText(homeChefOrderModel.getDishName());
        holder.tvFoodType.setText(homeChefOrderModel.getOrderType());
        holder.tvNoOfPeople.setText(homeChefOrderModel.getMinGuest() + " to " + homeChefOrderModel.getMaxGuest() + " people ");
        holder.tvPrice.setText(homeChefOrderModel.getPrice());
        holder.tvDescription.setText(homeChefOrderModel.getDescription());
        holder.tvRules.setText(homeChefOrderModel.getRules());

        if (homeChefOrderModel.getFoodImagesArrayList() != null && homeChefOrderModel.getFoodImagesArrayList().size() > 0) {
            ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(context, homeChefOrderModel.getFoodImagesArrayList());
            holder.viewPager.setAdapter(imagePagerAdapter);
            holder.circleIndicator.setViewPager(holder.viewPager);
        }
    }

    @Override
    public int getItemCount() {
        return homeChefOrderModelArrayList.size();
    }

    public interface BookOrderInterface {
        void onOrderSelected(String foodDate, int foodTime, int NoOfPerson);
    }

    class LunchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvFoodName;
        private TextView tvFoodType;
        private TextView tvNoOfPeople;
        private TextView tvPrice;
        private TextView tvDescription;
        private TextView tvRules;
        private Button btnBookOrder;
        private ViewPager viewPager;
        private CircleIndicator circleIndicator;

        LunchViewHolder(View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tv_food_name);
            tvFoodType = itemView.findViewById(R.id.tv_food_type);
            tvNoOfPeople = itemView.findViewById(R.id.tv_no_of_people);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvRules = itemView.findViewById(R.id.tv_rules);
            btnBookOrder = itemView.findViewById(R.id.btn_book_order);
            viewPager = itemView.findViewById(R.id.view_pager);
            circleIndicator = itemView.findViewById(R.id.indicator);
            btnBookOrder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_book_order:
                    System.out.println("Booking Availability:-" + homeChefOrderModelArrayList.get(getAdapterPosition()).getDishAvailability());
                    DialogUtils.bookFoodOnSelectedDatesDialog(context, homeChefOrderModelArrayList.get(getAdapterPosition()), new BookOrderInterface() {
                        @Override
                        public void onOrderSelected(String foodDate, int foodTime, int noOfPerson) {
                            System.out.println(">>>>> onOrderSelected" + foodDate);
                            System.out.println(">>>>> onOrderSelected" + foodTime);
                            System.out.println(">>>>> onOrderSelected" + noOfPerson);
                            ((HomeShopViewActivity) context).bookOrder(homeChefUserId, homeChefOrderModelArrayList.get(getAdapterPosition()).getOrderId(), foodDate, String.valueOf(foodTime), noOfPerson);

                        }
                    }, foodTimeType);
                    break;
            }
        }
    }
}
