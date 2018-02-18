package com.homemadebazar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class FoodieMessengerInvitesFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private UserModel userModel;
    private ArrayList<MessegeInviteParticipateModel> dataList;

    private TextView toolbarTitle;
    private MessengerInviteParticipatesRecyclerAdapter adapter;
    private UserLocation userLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messenger_invites, container, false);
    }

    @Override
    protected void initUI() {
        userLocation = SharedPreference.getUserLocation(getActivity());
        toolbar = getView().findViewById(R.id.toolbar);
        recyclerView = getView().findViewById(R.id.participates_recycler_view);
    }

    @Override
    protected void initialiseListener() {
        dataList = new ArrayList<>();
        userModel = SharedPreference.getUserModel(getActivity());
    }

    @Override
    protected void setData() {
        setRecyclerAdapter();
        getInviteParticipateListApiCall();
    }

    private void setRecyclerAdapter() {
        adapter = new MessengerInviteParticipatesRecyclerAdapter(getActivity(), dataList, userModel.getUserId());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }


//    {"StatusCode":"100","StatusMessage":"Successful","RequestObjects":[{"EmailId":"atul.raj@gmail.com","FName":"Atul","LName":"Jio","Mobile":"8709646364","UserId":"1801062","Distance":0,"Address":"","Pincode":"","DP":"","Status":"Request Not sent","StatusInNumeric":-1}]}

    private void getInviteParticipateListApiCall() {
        try {
//            final Dialog progressDialog = DialogUtils.getProgressDialog(getActivity(), null);
//            progressDialog.show();

            final MessengerInviteParticipateApiCall apiCall = new MessengerInviteParticipateApiCall(userModel.getUserId(), userLocation.getLatitude(), userLocation.getLongitude());
            HttpRequestHandler.getInstance(getActivity()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
//                    progressDialog.hide();
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                // DialogUtils.showAlert(getActivity(), "HomeChiefDetailList size:-" + apiCall.getHomeChiefDetailList().size());
                                dataList.clear();
                                dataList.addAll(apiCall.getData());
                                setRecyclerAdapter();
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
