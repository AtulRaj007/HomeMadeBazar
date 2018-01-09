package com.homemadebazar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homemadebazar.R;
import com.homemadebazar.adapter.FoodieMessengerFriendsAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.MessengerFriendApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

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
        getMessengerFriendListApiCall();

    }

    private void getMessengerFriendListApiCall() {
        try {
            swipeRefreshLayout.setRefreshing(true);
            final MessengerFriendApiCall apiCall = new MessengerFriendApiCall(userModel.getUserId());
            HttpRequestHandler.getInstance(getActivity()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                friendList.clear();
                                friendList.addAll(apiCall.getResult());
                                foodieMessengerFriendsAdapter.notifyDataSetChanged();
                            } else if (baseModel.getStatusCode() == Constants.ServerResponseCode.NO_RECORD_FOUND) {
                                friendList.clear();
                                foodieMessengerFriendsAdapter.notifyDataSetChanged();
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
