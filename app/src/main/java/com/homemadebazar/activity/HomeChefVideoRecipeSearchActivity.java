package com.homemadebazar.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.adapter.SkillHubAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChefSkillHubVideoModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.HomeChefSkillVideoApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
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
        setupToolbar();
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
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(etSearch.getText().toString().trim()))
                        getSkillHubVideos(etSearch.getText().toString().trim());
                    else
                        DialogUtils.showAlert(HomeChefVideoRecipeSearchActivity.this, "Please enter text to search");
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void setData() {
        recyclerView.setLayoutManager(linearLayoutManager);
        skillHubAdapter = new SkillHubAdapter(this, homeChefSkillHubVideoModelArrayList);
        recyclerView.setAdapter(skillHubAdapter);
    }

    public void getSkillHubVideos(String searchKeyWord) {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(this, null);
            progressDialog.show();

            final HomeChefSkillVideoApiCall apiCall = new HomeChefSkillVideoApiCall(userModel.getUserId(), searchKeyWord);
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
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
                                findViewById(R.id.tv_no_record_found).setVisibility(View.GONE);
                            } else if (baseModel.getStatusCode() == Constants.ServerResponseCode.NO_RECORD_FOUND) {
                                homeChefSkillHubVideoModelArrayList.clear();
                                videoModelsArrayList.clear();
                                skillHubAdapter.notifyDataSetChanged();
                                findViewById(R.id.tv_no_record_found).setVisibility(View.VISIBLE);
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

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyboard(HomeChefVideoRecipeSearchActivity.this);
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("Search Video Recipe");
    }

    @Override
    public void onBackPressed() {
        Utils.hideSoftKeyboard(this);
        super.onBackPressed();
    }
}
