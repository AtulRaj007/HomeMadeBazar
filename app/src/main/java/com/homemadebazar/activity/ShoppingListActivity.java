package com.homemadebazar.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.adapter.MarketPlaceAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.MarketPlaceProductModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.HomeChefBookProductsApiCall;
import com.homemadebazar.shopping.MarketPlaceShoppingCart;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

public class ShoppingListActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private MarketPlaceShoppingCart marketPlaceShoppingCart;
    private MarketPlaceAdapter marketPlaceAdapter;
    private ArrayList<MarketPlaceProductModel> marketPlaceProductModelArrayList;
    private TextView tvTotalPrice;
    private double price = 0;
    private UserModel userModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        setupToolbar();
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(ShoppingListActivity.this);
        marketPlaceShoppingCart = MarketPlaceShoppingCart.getInstance(this);
        recyclerView = findViewById(R.id.recycler_view);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        linearLayoutManager = new LinearLayoutManager(this);
    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.btn_book_order).setOnClickListener(this);
    }

    @Override
    protected void setData() {
        marketPlaceProductModelArrayList = marketPlaceShoppingCart.getProductFromCart();
        marketPlaceAdapter = new MarketPlaceAdapter(this, marketPlaceProductModelArrayList, marketPlaceShoppingCart);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(marketPlaceAdapter);
        getTotalPrice();
    }

    public void getTotalPrice() {
        for (int i = 0; i < marketPlaceProductModelArrayList.size(); i++) {
            price += Double.parseDouble(marketPlaceProductModelArrayList.get(i).getPrice());
        }
        tvTotalPrice.setText("Total price :-" + price + "");
    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("Shopping Cart");

    }

    private String getProductIds() {
        String productIds = "";
        for (int i = 0; i < marketPlaceProductModelArrayList.size(); i++) {
            if (TextUtils.isEmpty(productIds))
                productIds += marketPlaceProductModelArrayList.get(i).getProductId();
            else
                productIds = productIds + "," + marketPlaceProductModelArrayList.get(i).getProductId();
        }
        System.out.println("ProductIds:-" + productIds);
        return productIds;
    }

    private String getQuantity() {
        String quantity = "";
        for (int i = 0; i < marketPlaceProductModelArrayList.size(); i++) {
            if (TextUtils.isEmpty(quantity))
                quantity += marketPlaceProductModelArrayList.get(i).getQuantity();
            else
                quantity = quantity + "," + marketPlaceProductModelArrayList.get(i).getQuantity();
        }
        System.out.println("Quantity:-" + quantity);
        return quantity;
    }

    private void bookProducts() {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(this, null);
            progressDialog.show();

            final HomeChefBookProductsApiCall apiCall = new HomeChefBookProductsApiCall(userModel.getUserId(), getProductIds(), getQuantity());
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                DialogUtils.showAlert(ShoppingListActivity.this, "Your order is booked successfully.\nReference Id:-" + apiCall.getReferenceId(), new Runnable() {
                                    @Override
                                    public void run() {
                                        marketPlaceShoppingCart.removeAllProductsFromCart(ShoppingListActivity.this);
                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                });
                            } else {
                                DialogUtils.showAlert(ShoppingListActivity.this, baseModel.getStatusMessage());
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), ShoppingListActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), ShoppingListActivity.this, null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_book_order:
                if (marketPlaceProductModelArrayList.size() > 0)
                    bookProducts();
                else
                    DialogUtils.showAlert(ShoppingListActivity.this, "No products available to book");
                break;
        }
    }
}
