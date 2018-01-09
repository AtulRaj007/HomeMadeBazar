package com.homemadebazar.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.adapter.NotificationAdapter;

public class NotificationActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setupToolbar();
    }

    @Override
    protected void initUI() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    @Override
    protected void initialiseListener() {

    }

    @Override
    protected void setData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        notificationAdapter = new NotificationAdapter();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(notificationAdapter);
    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("WALLET");

    }
}
