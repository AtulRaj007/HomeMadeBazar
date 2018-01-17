package com.homemadebazar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homemadebazar.R;
import com.homemadebazar.adapter.FoodieHomeListAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChiefNearByModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.FoodieHomeChiefNearByListApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

/**
 * Created by atulraj on 18/1/18.
 */

public class FoodieHomeChefBookmarkFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FoodieHomeListAdapter foodieHomeListAdapter;
    private ArrayList<HomeChiefNearByModel> homeChiefNearByModelArrayList = new ArrayList<>();
    private UserModel userModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_chef_bookmark, container, false);
        return view;
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(getActivity());
        recyclerView = getView().findViewById(R.id.recycler_view);
        swipeRefreshLayout = getView().findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    protected void initialiseListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void setData() {
        foodieHomeListAdapter = new FoodieHomeListAdapter(getActivity(), homeChiefNearByModelArrayList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(foodieHomeListAdapter);
        getChiefDetailListApiCall();
    }

    public void getChiefDetailListApiCall() {
        try {
//            final Dialog progressDialog = DialogUtils.getProgressDialog(getActivity(), null);
//            progressDialog.show();

            final FoodieHomeChiefNearByListApiCall apiCall = new FoodieHomeChiefNearByListApiCall(userModel.getUserId(), "28.5244", "77.1855");
            HttpRequestHandler.getInstance(getActivity()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
//                    progressDialog.hide();
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                // DialogUtils.showAlert(getActivity(), "HomeChiefDetailList size:-" + apiCall.getHomeChiefDetailList().size());
                                homeChiefNearByModelArrayList.clear();
                                homeChiefNearByModelArrayList.addAll(apiCall.getResult());
                                foodieHomeListAdapter.notifyDataSetChanged();
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

    @Override
    public void onRefresh() {
        getChiefDetailListApiCall();
    }
}
