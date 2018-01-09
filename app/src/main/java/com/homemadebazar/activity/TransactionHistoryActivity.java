package com.homemadebazar.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.adapter.TransactionHistoryAdapter;

public class TransactionHistoryActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private TransactionHistoryAdapter transactionHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        setupToolbar();
    }

    @Override
    protected void initUI() {
        recyclerView= (RecyclerView) findViewById(R.id.recycler_view);


    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("TRANSACTION HISTORY");

    }

    @Override
    protected void initialiseListener() {

    }

    @Override
    protected void setData() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        transactionHistoryAdapter =new TransactionHistoryAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(transactionHistoryAdapter);
    }
}
