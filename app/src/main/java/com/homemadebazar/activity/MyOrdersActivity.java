package com.homemadebazar.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.adapter.MyAcceptedOrderAdapter;
import com.homemadebazar.adapter.MyOrdersAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChefIncomingOrderModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.HomeChefIncomingOrderApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

public class MyOrdersActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private MyOrdersAdapter myOrdersAdapter;
    private UserModel userModel;
    private ArrayList<HomeChefIncomingOrderModel> homeChefIncomingOrderModelArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        setupToolbar();
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(MyOrdersActivity.this);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
    }

    @Override
    protected void initialiseListener() {

    }

    @Override
    protected void setData() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        myOrdersAdapter = new MyOrdersAdapter(MyOrdersActivity.this, false, homeChefIncomingOrderModelArrayList);
        recyclerView.setAdapter(myOrdersAdapter);
        getBookOrderedList();
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

    private void getBookOrderedList() {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(MyOrdersActivity.this, null);
            progressDialog.show();

            final HomeChefIncomingOrderApiCall apiCall = new HomeChefIncomingOrderApiCall(userModel.getUserId(), Constants.HomeChefOrder.COMPLETED);
            HttpRequestHandler.getInstance(getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                ArrayList<HomeChefIncomingOrderModel> tempHomeChefIncomingOrderArrayList = apiCall.getResult();
                                homeChefIncomingOrderModelArrayList.clear();
                                homeChefIncomingOrderModelArrayList.addAll(tempHomeChefIncomingOrderArrayList);
                                myOrdersAdapter.notifyDataSetChanged();


                            } else {
                                DialogUtils.showAlert(MyOrdersActivity.this, baseModel.getStatusMessage());
                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), MyOrdersActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), MyOrdersActivity.this, null);
        }
    }
}
