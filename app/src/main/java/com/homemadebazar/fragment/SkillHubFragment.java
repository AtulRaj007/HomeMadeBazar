package com.homemadebazar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.homemadebazar.R;
import com.homemadebazar.activity.HomeChefVideoRecipeSearchActivity;
import com.homemadebazar.adapter.SkillHubAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChefSkillHubVideoModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.HomeChefSkillVideoApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

/**
 * Created by HP on 7/29/2017.
 */

public class SkillHubFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private RecyclerView recyclerView;
    private SkillHubAdapter skillHubAdapter;
    private LinearLayoutManager linearLayoutManager;
    private UserModel userModel;
    private EditText etSearch;
    private ImageView ivClearSearch;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<HomeChefSkillHubVideoModel> homeChefSkillHubVideoModelArrayList = new ArrayList<>();
    private ArrayList<HomeChefSkillHubVideoModel> videoModelsArrayList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_skill_hub, container, false);
    }

    public void initUI() {
        userModel = SharedPreference.getUserModel(getActivity());
        recyclerView = getView().findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        etSearch = getView().findViewById(R.id.et_search);
//        ivClearSearch = getView().findViewById(R.id.iv_clear_search);
        swipeRefreshLayout = getView().findViewById(R.id.swipe_refresh_layout);
    }

    public void initialiseListener() {
        etSearch.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void filterSearchItem(String searchString) {
        ArrayList<HomeChefSkillHubVideoModel> tempHomeChefSkillHubVideoModelArrayList = new ArrayList<>();
        for (int i = 0; i < videoModelsArrayList.size(); i++) {
            if (videoModelsArrayList.get(i).getTitle().contains(searchString)) {
                tempHomeChefSkillHubVideoModelArrayList.add(videoModelsArrayList.get(i));
            }
        }
        homeChefSkillHubVideoModelArrayList.clear();
        homeChefSkillHubVideoModelArrayList.addAll(tempHomeChefSkillHubVideoModelArrayList);
        skillHubAdapter.notifyDataSetChanged();

    }

    public void setData() {
        recyclerView.setLayoutManager(linearLayoutManager);
        skillHubAdapter = new SkillHubAdapter(getActivity(), homeChefSkillHubVideoModelArrayList);
        recyclerView.setAdapter(skillHubAdapter);
        getSkillHubVideos();
        swipeRefreshLayout.setRefreshing(true);
    }

    public void getSkillHubVideos() {
        try {
            final HomeChefSkillVideoApiCall apiCall = new HomeChefSkillVideoApiCall(userModel.getUserId());
            HttpRequestHandler.getInstance(getActivity().getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                ArrayList<HomeChefSkillHubVideoModel> tempHomeChefSkillHubVideoArrayList = apiCall.getResult();
                                homeChefSkillHubVideoModelArrayList.clear();
                                videoModelsArrayList.clear();
                                homeChefSkillHubVideoModelArrayList.addAll(tempHomeChefSkillHubVideoArrayList);
                                videoModelsArrayList.addAll(tempHomeChefSkillHubVideoArrayList);
                                skillHubAdapter.notifyDataSetChanged();
                            } else if (baseModel.getStatusCode() == Constants.ServerResponseCode.NO_RECORD_FOUND) {

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
        getSkillHubVideos();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_search:
                startActivity(new Intent(getActivity(), HomeChefVideoRecipeSearchActivity.class));
                break;
        }
    }
}

