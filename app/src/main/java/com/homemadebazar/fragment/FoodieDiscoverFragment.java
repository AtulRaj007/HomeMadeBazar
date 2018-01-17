package com.homemadebazar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homemadebazar.R;
import com.homemadebazar.adapter.FoodieDiscoverAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChefOrderModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.GetListOfHotDealsApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

/**
 * Created by atulraj on 22/11/17.
 */

public class FoodieDiscoverFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FoodieDiscoverAdapter foodieDiscoverAdapter;
    private ArrayList<HomeChefOrderModel> homeChefOrderModelArrayList = new ArrayList<>();
    private UserModel userModel;
//    private HomeChefFoodTimingAdapter homeChefLunchAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foodie_discover, container, false);
        return view;
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(getActivity());
        foodieDiscoverAdapter = new FoodieDiscoverAdapter(getActivity(), homeChefOrderModelArrayList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = getView().findViewById(R.id.recycler_view);
    }

    @Override
    protected void initialiseListener() {

    }

    @Override
    protected void setData() {
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(foodieDiscoverAdapter);
        getHomeChefHotDealsOrders(userModel.getUserId());
    }

    private void getHomeChefHotDealsOrders(String userId) {
        try {
//            final Dialog progressDialog = DialogUtils.getProgressDialog(getActivity(), null);
//            progressDialog.show();

            final GetListOfHotDealsApiCall apiCall = new GetListOfHotDealsApiCall(userId);
            HttpRequestHandler.getInstance(getActivity().getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
//                    progressDialog.hide();
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                ArrayList<HomeChefOrderModel> tempHomeChefOrderModelArrayList = apiCall.getResult();
                                homeChefOrderModelArrayList.clear();
                                homeChefOrderModelArrayList.addAll(tempHomeChefOrderModelArrayList);
                                foodieDiscoverAdapter.notifyDataSetChanged();
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
