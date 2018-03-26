package com.homemadebazar.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.activity.ShoppingListActivity;
import com.homemadebazar.adapter.MarketPlaceAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.MarketPlaceProductModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.SearchMarketPlaceProductsApiCall;
import com.homemadebazar.shopping.MarketPlaceShoppingCart;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

/**
 * Created by HP on 7/29/2017.
 */

public class MarketPlaceFragment extends BaseFragment implements View.OnClickListener {
    private static int GO_TO_CART = 105;
    private String TAG = ">>>>>MarketPlace";
    private RecyclerView recyclerView;
    private MarketPlaceAdapter marketPlaceAdapter;
    private LinearLayoutManager linearLayoutManager;
    private EditText etSearch;
    private ArrayList<MarketPlaceProductModel> marketPlaceProductModelArrayList = new ArrayList<>();
    private MarketPlaceShoppingCart marketPlaceShoppingCart;
    private Button btnCheckout;
    private TextView tvNoRecordFound;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_market_place, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (marketPlaceAdapter != null) {
            marketPlaceAdapter.notifyDataSetChanged();
        }
    }

    public void initUI() {
        marketPlaceShoppingCart = MarketPlaceShoppingCart.getInstance(getActivity());
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        etSearch = getView().findViewById(R.id.et_search);
        btnCheckout = getView().findViewById(R.id.btn_checkout);
        tvNoRecordFound = getView().findViewById(R.id.tv_no_record_found);
    }

    public void initialiseListener() {
        btnCheckout.setOnClickListener(this);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Utils.hideSoftKeyboard(getActivity());
                    performSearchApiCall(v.getText().toString());
                }
                return false;
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(etSearch.getText().toString().trim())) {
                    marketPlaceProductModelArrayList.clear();
                    marketPlaceAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void performSearchApiCall(String searchString) {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(getActivity(), null);
            progressDialog.show();

            final SearchMarketPlaceProductsApiCall apiCall = new SearchMarketPlaceProductsApiCall(searchString);
            HttpRequestHandler.getInstance(getActivity().getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                marketPlaceProductModelArrayList.clear();
                                marketPlaceProductModelArrayList.addAll(apiCall.getResult());
                                marketPlaceAdapter.notifyDataSetChanged();
                                tvNoRecordFound.setVisibility(View.GONE);
                            } else if (baseModel.getStatusCode() == Constants.ServerResponseCode.NO_RECORD_FOUND) {
                                marketPlaceProductModelArrayList.clear();
                                marketPlaceAdapter.notifyDataSetChanged();
                                tvNoRecordFound.setVisibility(View.VISIBLE);
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


    public void setData() {
        marketPlaceAdapter = new MarketPlaceAdapter(getActivity(), marketPlaceProductModelArrayList, marketPlaceShoppingCart);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(marketPlaceAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_checkout:
                ArrayList<MarketPlaceProductModel> marketPlaceProductModelArrayList = marketPlaceShoppingCart.getProductFromCart();
                if (marketPlaceProductModelArrayList.size() > 0) {
                    Intent intent = new Intent(getActivity(), ShoppingListActivity.class);
                    startActivityForResult(intent, GO_TO_CART);
                } else {
                    DialogUtils.showAlert(getActivity(), "Cart is empty. Please add items to proceed");
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GO_TO_CART && requestCode == getActivity().RESULT_OK) {
            etSearch.setText("");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
