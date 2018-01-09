package com.homemadebazar.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.homemadebazar.R;
import com.homemadebazar.adapter.SkillHubAdapter;
import com.homemadebazar.model.HomeChefSkillHubVideoModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.HomeChefSkillVideoApiCall;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

/**
 * Created by HP on 7/29/2017.
 */

public class SkillHubFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private SkillHubAdapter skillHubAdapter;
    private LinearLayoutManager linearLayoutManager;
    private UserModel userModel;
    private EditText etSearch;
    private ImageView ivClearSearch;
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
        ivClearSearch = getView().findViewById(R.id.iv_clear_search);
    }

    public void initialiseListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    filterSearchItem(s.toString());
                    ivClearSearch.setVisibility(View.VISIBLE);
                } else {
                    ivClearSearch.setVisibility(View.GONE);
                    homeChefSkillHubVideoModelArrayList.clear();
                    homeChefSkillHubVideoModelArrayList.addAll(videoModelsArrayList);
                    skillHubAdapter.notifyDataSetChanged();
                }
            }
        });

        getView().findViewById(R.id.iv_clear_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
                homeChefSkillHubVideoModelArrayList.clear();
                homeChefSkillHubVideoModelArrayList.addAll(videoModelsArrayList);
                skillHubAdapter.notifyDataSetChanged();
            }
        });
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
    }

    public void getSkillHubVideos() {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(getActivity(), null);
            progressDialog.show();

            final HomeChefSkillVideoApiCall apiCall = new HomeChefSkillVideoApiCall(userModel.getUserId());
            HttpRequestHandler.getInstance(getActivity().getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            ArrayList<HomeChefSkillHubVideoModel> tempHomeChefSkillHubVideoArrayList = apiCall.getResult();
                            homeChefSkillHubVideoModelArrayList.clear();
                            videoModelsArrayList.clear();
                            homeChefSkillHubVideoModelArrayList.addAll(tempHomeChefSkillHubVideoArrayList);
                            videoModelsArrayList.addAll(tempHomeChefSkillHubVideoArrayList);
                            skillHubAdapter.notifyDataSetChanged();
                     /*       if (isAccountExistModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {

                            } else {
                                DialogUtils.showAlert(getActivity(), isAccountExistModel.getStatusMessage());
                            }*/


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
