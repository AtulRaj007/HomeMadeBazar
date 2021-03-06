package com.munchmash.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.munchmash.R;
import com.munchmash.activity.MarketPlaceAddProductActivity;
import com.munchmash.adapter.MarketPlaceProductAdpater;
import com.munchmash.model.BaseModel;
import com.munchmash.model.MarketPlaceProductModel;
import com.munchmash.model.UserModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.MarketPlaceProductListApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

import java.util.ArrayList;

/**
 * Created by atulraj on 23/11/17.
 */

public class MarketPlaceMyProductsFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private MarketPlaceProductAdpater marketPlaceProductAdpater;
    private ImageView ivAddProduct;
    private ArrayList<MarketPlaceProductModel> marketPlaceProductModelArrayList = new ArrayList<>();
    private UserModel userModel;

    @Override
    protected void initUI() {
        userModel= SharedPreference.getUserModel(getActivity());
        recyclerView = getView().findViewById(R.id.recycler_view);
        ivAddProduct = getView().findViewById(R.id.iv_add_product);
    }

    @Override
    protected void initialiseListener() {
        ivAddProduct.setOnClickListener(this);
    }

    @Override
    protected void setData() {
        marketPlaceProductAdpater = new MarketPlaceProductAdpater(getActivity(), marketPlaceProductModelArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(marketPlaceProductAdpater);
        getProductListApiCall();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market_place_my_products, container, false);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_product:
                Intent intent = new Intent(getActivity(), MarketPlaceAddProductActivity.class);
                startActivityForResult(intent, Constants.Keys.REQUEST_ADD_PRODUCT);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.Keys.REQUEST_ADD_PRODUCT && resultCode == getActivity().RESULT_OK) {
            getProductListApiCall();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getProductListApiCall() {
        try {

            final MarketPlaceProductListApiCall apiCall = new MarketPlaceProductListApiCall(userModel.getUserId());
            HttpRequestHandler.getInstance(getActivity().getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                ArrayList<MarketPlaceProductModel> tempMarketPlaceProductModelArrayList = apiCall.getResult();
                                marketPlaceProductModelArrayList.clear();
                                marketPlaceProductModelArrayList.addAll(tempMarketPlaceProductModelArrayList);
                                marketPlaceProductAdpater.notifyDataSetChanged();
                            } else {
                                DialogUtils.showAlert(getActivity(), baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), getActivity(), null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), getActivity(), null);
        }
    }
}
