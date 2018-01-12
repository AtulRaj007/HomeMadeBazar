package com.homemadebazar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.homemadebazar.R;
import com.homemadebazar.activity.FoodieHomeChefSearchActivity;
import com.homemadebazar.adapter.FoodieHomeListAdapter;
import com.homemadebazar.model.HomeChiefNearByModel;
import com.homemadebazar.util.DialogUtils;

import java.util.ArrayList;

/**
 * Created by atulraj on 22/11/17.
 */

public class FoodieHomeListFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FoodieHomeListAdapter foodieHomeListAdapter;
    private ArrayList<HomeChiefNearByModel> homeChiefNearByModelArrayList = new ArrayList<>();
    private ImageView ivSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foodie_home_list, container, false);
    }

    @Override
    protected void initUI() {
        recyclerView = getView().findViewById(R.id.recycler_view);
        ivSearch = getView().findViewById(R.id.iv_search);
    }

    @Override
    protected void initialiseListener() {
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FoodieHomeChefSearchActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void setData() {

    }

    public void setFoodieHomeListDetails(ArrayList<HomeChiefNearByModel> dataList) {
        this.homeChiefNearByModelArrayList.clear();
        this.homeChiefNearByModelArrayList.addAll(dataList);
        setRecyclerAdapter(this.homeChiefNearByModelArrayList);
    }

    private void setRecyclerAdapter(ArrayList<HomeChiefNearByModel> dataList) {
        foodieHomeListAdapter = new FoodieHomeListAdapter(getActivity(), dataList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(foodieHomeListAdapter);
    }
}
