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
import com.homemadebazar.adapter.MessengerInviteParticipatesRecyclerAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.MessegeInviteParticipateModel;
import com.homemadebazar.model.UserLocation;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.MessengerInviteParticipateApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

/**
 * Created by sonu on 12/9/2017.
 */

public class FoodieMessengerInvitesFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private UserModel userModel;
    private ArrayList<MessegeInviteParticipateModel> dataList = new ArrayList<>();
    private MessengerInviteParticipatesRecyclerAdapter adapter;
    private UserLocation userLocation;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messenger_invites, container, false);
    }

    @Override
    protected void initUI() {
        userLocation = SharedPreference.getUserLocation(getActivity());
        recyclerView = getView().findViewById(R.id.participates_recycler_view);
        swipeRefreshLayout = getView().findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    protected void initialiseListener() {
        userModel = SharedPreference.getUserModel(getActivity());
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void setData() {
        adapter = new MessengerInviteParticipatesRecyclerAdapter(getActivity(), dataList, userModel.getUserId());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        getInviteParticipateListApiCall();
        swipeRefreshLayout.setRefreshing(true);
    }


    private void getInviteParticipateListApiCall() {
        try {
            final MessengerInviteParticipateApiCall apiCall = new MessengerInviteParticipateApiCall(userModel.getUserId(), userLocation.getLatitude(), userLocation.getLongitude());
            HttpRequestHandler.getInstance(getActivity()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    if (e == null) { // Success
                        try {
                            swipeRefreshLayout.setRefreshing(false);
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                getView().findViewById(R.id.tv_no_record_found).setVisibility(View.GONE);
                                dataList.clear();
                                dataList.addAll(apiCall.getData());
                                adapter.notifyDataSetChanged();

                            } else if (baseModel.getStatusCode() == Constants.ServerResponseCode.NO_RECORD_FOUND) {
                                getView().findViewById(R.id.tv_no_record_found).setVisibility(View.VISIBLE);
                                return;
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
        getInviteParticipateListApiCall();
    }
}
