package com.homemadebazar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.adapter.MarketPlaceOrderDetailAdapter;

/**
 * Created by sonu on 12/18/2017.
 */

public class MarketPlaceOrderDetailsActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private MarketPlaceOrderDetailAdapter marketPlaceOrderDetailAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        setupToolbar();
    }

    @Override
    protected void initUI() {
        recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected void initialiseListener() {

    }

    @Override
    protected void setData() {
        marketPlaceOrderDetailAdapter = new MarketPlaceOrderDetailAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(marketPlaceOrderDetailAdapter);
    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("Order Details");

    }

}
