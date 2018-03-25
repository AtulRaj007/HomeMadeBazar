package com.homemadebazar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homemadebazar.R;
import com.homemadebazar.adapter.MarketPlaceOrderDetailAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.MarketPlaceOrderModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.ServiceUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

/**
 * Created by sonu on 12/18/2017.
 */

public class MarketPlaceOrderDetailsActivity extends BaseActivity implements View.OnClickListener {
    private static String KEY_MARKET_PLACE_ORDER_MODEL = "KEY_MARKET_PLACE_ORDER_MODEL";
    private static String KEY_ORDER_TYPE = "KEY_ORDER_TYPE";
    private RecyclerView recyclerView;
    private MarketPlaceOrderDetailAdapter marketPlaceOrderDetailAdapter;
    private MarketPlaceOrderModel marketPlaceOrderModel;
    private TextView tvName;
    private TextView tvMobileNumber;
    private TextView tvEmailId;
    private TextView tvOrderId;
    private ImageView ivCall;
    private ImageView ivMessage;
    private ImageView ivShowDirections;
    private ImageView ivProfilePic;
    private Button btnAccept, btnReject, btnDispatch;
    private UserModel userModel;
    private String orderType;


    public static Intent getIntent(Context context, MarketPlaceOrderModel marketPlaceOrderModel, String orderType) {
        Intent intent = new Intent(context, MarketPlaceOrderDetailsActivity.class);
        intent.putExtra(KEY_MARKET_PLACE_ORDER_MODEL, marketPlaceOrderModel);
        intent.putExtra(KEY_ORDER_TYPE, orderType);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        setupToolbar();
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(this);
        marketPlaceOrderModel = (MarketPlaceOrderModel) getIntent().getSerializableExtra(KEY_MARKET_PLACE_ORDER_MODEL);
        orderType = getIntent().getStringExtra(KEY_ORDER_TYPE);
        recyclerView = findViewById(R.id.recycler_view);
        tvOrderId = findViewById(R.id.tv_order_id);
        tvName = findViewById(R.id.tv_name);
        tvMobileNumber = findViewById(R.id.tv_mobile_number);
        tvEmailId = findViewById(R.id.tv_email_id);
        ivCall = findViewById(R.id.iv_call);
        ivMessage = findViewById(R.id.iv_message);
        ivShowDirections = findViewById(R.id.iv_show_directions);
        ivProfilePic = findViewById(R.id.iv_profile_pic);

        btnAccept = findViewById(R.id.btn_accept);
        btnReject = findViewById(R.id.btn_reject);
        btnDispatch = findViewById(R.id.btn_dispatch);

    }

    @Override
    protected void initialiseListener() {
        ivProfilePic.setOnClickListener(this);
        ivCall.setOnClickListener(this);
        ivMessage.setOnClickListener(this);

        btnAccept.setOnClickListener(this);
        btnReject.setOnClickListener(this);
        btnDispatch.setOnClickListener(this);
    }

    @Override
    protected void setData() {
        try {
            if (!TextUtils.isEmpty(marketPlaceOrderModel.getProfileImage())) {
                Glide.with(this).load(marketPlaceOrderModel.getProfileImage()).into(ivProfilePic);
            } else {
                ivProfilePic.setImageDrawable(null);
            }
            tvOrderId.setText(marketPlaceOrderModel.getOrderId());
            tvName.setText(marketPlaceOrderModel.getName());
            tvMobileNumber.setText(marketPlaceOrderModel.getMobileNumber());
            tvEmailId.setText(marketPlaceOrderModel.getEmail());

            marketPlaceOrderDetailAdapter = new MarketPlaceOrderDetailAdapter(this, marketPlaceOrderModel.getMarketPlaceOrderProductModelArrayList());
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(marketPlaceOrderDetailAdapter);

            setUpOrderHandling();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpOrderHandling() {
        if (orderType.equals(Constants.MarketPlaceOrder.INCOMING_ORDER)) {
            btnAccept.setVisibility(View.VISIBLE);
            btnReject.setVisibility(View.VISIBLE);

        } else if (orderType.equals(Constants.MarketPlaceOrder.OUTGOING_ORDER)) {
            btnDispatch.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_profile_pic:
                Utils.showProfile(this, marketPlaceOrderModel.getUserId());
                break;
            case R.id.iv_call:
                Utils.startCall(this, marketPlaceOrderModel.getMobileNumber());
                break;
            case R.id.iv_message:
                Utils.message(this, marketPlaceOrderModel.getMobileNumber());
                break;
            case R.id.btn_accept:
                ServiceUtils.actionByMarketPlaceUsers(this, userModel.getUserId(), Constants.MarketPlaceOrderACtionType.ACCEPT, getProductRowsId(), new ServiceUtils.MarketPlaceOrderActionInterface() {
                    @Override
                    public void onOrderAtion(BaseModel baseModel) {
                        if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                            DialogUtils.showAlert(MarketPlaceOrderDetailsActivity.this, "Order Accepted Successfully", new Runnable() {
                                @Override
                                public void run() {
                                    Constants.isMarketPlaceOrderRefresh = true;
                                    finish();
                                }
                            });
                        } else {
                            DialogUtils.showAlert(MarketPlaceOrderDetailsActivity.this, baseModel.getStatusMessage());
                        }
                    }
                });
                break;
            case R.id.btn_reject:
                ServiceUtils.actionByMarketPlaceUsers(this, userModel.getUserId(), Constants.MarketPlaceOrderACtionType.REJECT, getProductRowsId(), new ServiceUtils.MarketPlaceOrderActionInterface() {
                    @Override
                    public void onOrderAtion(BaseModel baseModel) {
                        if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                            DialogUtils.showAlert(MarketPlaceOrderDetailsActivity.this, "Order Rejected Successfully", new Runnable() {
                                @Override
                                public void run() {
                                    Constants.isMarketPlaceOrderRefresh = true;
                                    finish();
                                }
                            });
                        } else {
                            DialogUtils.showAlert(MarketPlaceOrderDetailsActivity.this, baseModel.getStatusMessage());
                        }
                    }
                });
                break;
            case R.id.btn_dispatch:
                ServiceUtils.actionByMarketPlaceUsers(this, userModel.getUserId(), Constants.MarketPlaceOrderACtionType.DISPATCH, getProductRowsId(), new ServiceUtils.MarketPlaceOrderActionInterface() {
                    @Override
                    public void onOrderAtion(BaseModel baseModel) {
                        if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                            DialogUtils.showAlert(MarketPlaceOrderDetailsActivity.this, "Order Dispatched Successfully", new Runnable() {
                                @Override
                                public void run() {
                                    Constants.isMarketPlaceOrderRefresh = true;
                                    finish();
                                }
                            });
                        } else {
                            DialogUtils.showAlert(MarketPlaceOrderDetailsActivity.this, baseModel.getStatusMessage());
                        }
                    }
                });
                break;

        }
    }

    private String getProductRowsId() {
        String rowIds = "";
        for (int i = 0; i < marketPlaceOrderModel.getMarketPlaceOrderProductModelArrayList().size(); i++) {
            rowIds = rowIds + marketPlaceOrderModel.getMarketPlaceOrderProductModelArrayList().get(i).getRowId();
            if (i != marketPlaceOrderModel.getMarketPlaceOrderProductModelArrayList().size() - 1) {
                rowIds = rowIds + ",";
            }
        }
        System.out.println("Product Row Ids:-" + rowIds);
        return rowIds;
    }
}
