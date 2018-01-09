package com.homemadebazar.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.adapter.MarketPlaceAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.IngredientsRowsModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.SearchMarketPlaceIngredientsApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

/**
 * Created by HP on 7/29/2017.
 */

public class MarketPlaceFragment extends BaseFragment {
    private String TAG = ">>>>>MarketPlace";
    private RecyclerView recyclerView;
    private MarketPlaceAdapter marketPlaceAdapter;
    private LinearLayoutManager linearLayoutManager;
    private EditText etSearch;
    private ArrayList<IngredientsRowsModel> ingredientsRowsModelArrayList = new ArrayList<>();

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


    public void initUI() {
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        etSearch = getView().findViewById(R.id.et_search);
    }

    public void initialiseListener() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearchApiCall(v.getText().toString());
                }
                return false;
            }
        });
    }

    private void performSearchApiCall(String searchString) {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(getActivity(), null);
            progressDialog.show();

            final SearchMarketPlaceIngredientsApiCall apiCall = new SearchMarketPlaceIngredientsApiCall(searchString);
            HttpRequestHandler.getInstance(getActivity().getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                ingredientsRowsModelArrayList.clear();
                                ingredientsRowsModelArrayList.addAll(apiCall.getIngredientsRowsModelArrayList());
                                marketPlaceAdapter.notifyDataSetChanged();
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
        marketPlaceAdapter = new MarketPlaceAdapter(getActivity(), ingredientsRowsModelArrayList);
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
}
