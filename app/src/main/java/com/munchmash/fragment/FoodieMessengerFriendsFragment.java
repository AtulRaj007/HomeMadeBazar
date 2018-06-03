package com.munchmash.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.munchmash.R;
import com.munchmash.adapter.FoodieMessengerFriendsAdapter;
import com.munchmash.model.BaseModel;
import com.munchmash.model.UserModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.MessengerFriendApiCall;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;

import java.util.ArrayList;

/**
 * Created by atulraj on 22/11/17.
 */

public class FoodieMessengerFriendsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private FoodieMessengerFriendsAdapter foodieMessengerFriendsAdapter;
    private UserModel userModel;
    private ArrayList<UserModel> friendList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foodie_messenger_friends, container, false);
        return view;
    }

    @Override
    protected void initUI() {
        recyclerView = getView().findViewById(R.id.recycler_view);
        swipeRefreshLayout = getView().findViewById(R.id.swipe_refresh_layout);
        linearLayoutManager = new LinearLayoutManager(getActivity());
    }

    @Override
    protected void initialiseListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
        userModel = SharedPreference.getUserModel(getActivity());
    }

    @Override
    protected void setData() {
        foodieMessengerFriendsAdapter = new FoodieMessengerFriendsAdapter(getActivity(), friendList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(foodieMessengerFriendsAdapter);
//        getMessengerFriendListApiCall();
//        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getMessengerFriendListApiCall();
        swipeRefreshLayout.setRefreshing(true);
    }

    private void getMessengerFriendListApiCall() {
        try {
            final MessengerFriendApiCall apiCall = new MessengerFriendApiCall(userModel.getUserId());
            HttpRequestHandler.getInstance(getActivity()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                getView().findViewById(R.id.tv_no_record_found).setVisibility(View.GONE);
                                friendList.clear();
                                friendList.addAll(apiCall.getResult());
                                foodieMessengerFriendsAdapter.notifyDataSetChanged();
                            } else if (baseModel.getStatusCode() == Constants.ServerResponseCode.NO_RECORD_FOUND) {
                                friendList.clear();
                                foodieMessengerFriendsAdapter.notifyDataSetChanged();
                                getView().findViewById(R.id.tv_no_record_found).setVisibility(View.VISIBLE);
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
        getMessengerFriendListApiCall();
    }
}
