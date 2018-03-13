package com.homemadebazar.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;

import com.homemadebazar.R;
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

public class HomeChefVideoRecipeSearchActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private SkillHubAdapter skillHubAdapter;
    private LinearLayoutManager linearLayoutManager;
    private UserModel userModel;
    private EditText etSearch;
    private ImageView ivClearSearch;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<HomeChefSkillHubVideoModel> homeChefSkillHubVideoModelArrayList = new ArrayList<>();
    private ArrayList<HomeChefSkillHubVideoModel> videoModelsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_chef_video_recipe_search);
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(this);
        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        etSearch = findViewById(R.id.et_search);
    }

    @Override
    protected void initialiseListener() {

    }

    @Override
    public void setData() {
        recyclerView.setLayoutManager(linearLayoutManager);
        skillHubAdapter = new SkillHubAdapter(this, homeChefSkillHubVideoModelArrayList);
        recyclerView.setAdapter(skillHubAdapter);
        getSkillHubVideos();
//        swipeRefreshLayout.setRefreshing(true);
    }

    public void getSkillHubVideos() {
        try {
            final HomeChefSkillVideoApiCall apiCall = new HomeChefSkillVideoApiCall(userModel.getUserId());
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
//                    swipeRefreshLayout.setRefreshing(false);
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
                        Utils.handleError(e.getMessage(), HomeChefVideoRecipeSearchActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), HomeChefVideoRecipeSearchActivity.this, null);
        }
    }
}
