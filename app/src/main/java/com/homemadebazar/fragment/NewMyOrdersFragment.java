package com.homemadebazar.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homemadebazar.R;
import com.homemadebazar.adapter.MyOrdersAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChefIncomingOrderModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.HomeChefFoodieOrderAcceptRejectApiCall;
import com.homemadebazar.network.apicall.HomeChefIncomingOrderApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

/**
 * Created by Sumit on 30/07/17.
 */

public class NewMyOrdersFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private MyOrdersAdapter myOrdersAdapter;
    private UserModel userModel;
    private ArrayList<HomeChefIncomingOrderModel> homeChefIncomingOrderModelArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_myorders, container, false);
    }

    public void initUI() {
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        userModel = SharedPreference.getUserModel(getActivity());
    }

    public void initialiseListener() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void setData() {
        myOrdersAdapter = new MyOrdersAdapter(getActivity(), false, homeChefIncomingOrderModelArrayList);
        recyclerView.setAdapter(myOrdersAdapter);
        getBookOrderedList();
    }

    private void getBookOrderedList() {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(getActivity(), null);
            progressDialog.show();

            final HomeChefIncomingOrderApiCall apiCall = new HomeChefIncomingOrderApiCall(userModel.getUserId(), "NOW");
            HttpRequestHandler.getInstance(getActivity().getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

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
