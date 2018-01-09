package com.homemadebazar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.homemadebazar.R;
import com.homemadebazar.activity.FoodieAddPostActivity;
import com.homemadebazar.adapter.FoodieFlashPostAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.FoodieFlashPostModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.FoodieGetPostsApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

/**
 * Created by atulraj on 22/11/17.
 */

public class FoodieFlashFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private Switch switchBtn;
    private FoodieFlashPostAdapter adapter;
    private UserModel userModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<FoodieFlashPostModel> foodieFlashPostModelArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foodie_flash, container, false);
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(getActivity());
        fab = getView().findViewById(R.id.fab);
        recyclerView = getView().findViewById(R.id.recycler_view);
        switchBtn = getView().findViewById(R.id.switch_pets_allowed);
        swipeRefreshLayout = getView().findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    protected void initialiseListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postIntent = new Intent(getActivity(), FoodieAddPostActivity.class);
                startActivityForResult(postIntent, Constants.Keys.FOODIE_POST);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void setData() {
        setRecyclerAdapter();
        getFoodieFlashPosts();
    }

    private void setRecyclerAdapter() {
        adapter = new FoodieFlashPostAdapter(getActivity(), userModel.getUserId(), foodieFlashPostModelArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    public void getFoodieFlashPosts() {
        try {
//            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(getActivity(), null);
//            progressDialog.show();
            swipeRefreshLayout.setRefreshing(true);
            final FoodieGetPostsApiCall apiCall = new FoodieGetPostsApiCall(userModel.getUserId());
            HttpRequestHandler.getInstance(getActivity().getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
//                    DialogUtils.hideProgressDialog(progressDialog);
                    swipeRefreshLayout.setRefreshing(false);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                ArrayList<FoodieFlashPostModel> tempFoodieFlashPostArrayList = apiCall.getResult();
                                foodieFlashPostModelArrayList.clear();
                                foodieFlashPostModelArrayList.addAll(tempFoodieFlashPostArrayList);
                                adapter.notifyDataSetChanged();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case Constants.Keys.FOODIE_POST:
                    getFoodieFlashPosts();
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRefresh() {
        getFoodieFlashPosts();
    }
}
