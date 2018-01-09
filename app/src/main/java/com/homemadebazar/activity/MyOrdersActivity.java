package com.homemadebazar.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.adapter.MyAcceptedOrderAdapter;

public class MyOrdersActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private MyAcceptedOrderAdapter myAcceptedOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        setupToolbar();
    }

    @Override
    protected void initUI() {
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        myAcceptedOrderAdapter=new MyAcceptedOrderAdapter();
    }

    @Override
    protected void initialiseListener() {

    }

    @Override
    protected void setData() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAcceptedOrderAdapter);
    }

    private void setupToolbar(){
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.tv_title)).setText("MY ORDERS");

    }
}
