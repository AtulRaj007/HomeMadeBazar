package com.homemadebazar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.adapter.PostCommentAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.FoodiePostCommentModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.FoodiePostLikeCommentApiCall;
import com.homemadebazar.network.apicall.GetPostCommentApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

public class FoodiePostCommentActivity extends BaseActivity implements View.OnClickListener {
    public static String KEY_POST_ID = "KEY_POST_ID";
    private ImageView ivBack;
    private EditText etChatMessage;
    private RecyclerView recyclerView;
    private UserModel userModel;
    private String postId;
    private PostCommentAdapter postCommentAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<FoodiePostCommentModel> foodiePostCommentModelArrayList = new ArrayList<>();

    public static Intent getCommentIntent(Context context, String postId) {
        Intent intent = new Intent(context, FoodiePostCommentActivity.class);
        intent.putExtra(KEY_POST_ID, postId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodie_post_comment);
        setupToolbar();
    }

    private void getBundleData() {
        postId = getIntent().getStringExtra(KEY_POST_ID);
    }

    @Override
    protected void initUI() {
        getBundleData();
        linearLayoutManager = new LinearLayoutManager(FoodiePostCommentActivity.this);
        userModel = SharedPreference.getUserModel(FoodiePostCommentActivity.this);
        ivBack = findViewById(R.id.iv_back);
        etChatMessage = findViewById(R.id.et_chat_message);
        recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.iv_chat_send).setOnClickListener(this);
    }

    @Override
    protected void setData() {
        recyclerView.setLayoutManager(linearLayoutManager);
        postCommentAdapter = new PostCommentAdapter(FoodiePostCommentActivity.this, foodiePostCommentModelArrayList, userModel.getUserId());
        recyclerView.setAdapter(postCommentAdapter);
        getCommentsApiCall(postId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_chat_send:
                if (isValid()) {
                    performLikeUnlikeComment(postId, Constants.PostActionTAG.COMMENTS, etChatMessage.getText().toString().trim(), userModel.getUserId());
                    etChatMessage.setText("");
                }
                break;

        }
    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("COMMENTS");

    }

    private void performLikeUnlikeComment(String postId, String actionType, String comments, String actionDoneByUserId) {
        try {

            final FoodiePostLikeCommentApiCall apiCall = new FoodiePostLikeCommentApiCall(postId, actionType, comments, actionDoneByUserId);
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                FoodiePostCommentModel foodiePostCommentModel = new FoodiePostCommentModel();
                                foodiePostCommentModel.setComments(apiCall.getComments());
                                foodiePostCommentModel.setSentTime(apiCall.getDateTime());
                                foodiePostCommentModel.setUserId(userModel.getUserId());
                                foodiePostCommentModel.setFirstName(apiCall.getFirstName());
                                foodiePostCommentModel.setLastName(apiCall.getLastName());
                                foodiePostCommentModel.setUserProfile(apiCall.getUserProfile());

                                foodiePostCommentModelArrayList.add(foodiePostCommentModel);
                                postCommentAdapter.notifyDataSetChanged();
                            } else {
                                DialogUtils.showAlert(FoodiePostCommentActivity.this, baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), FoodiePostCommentActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), FoodiePostCommentActivity.this, null);
        }
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(etChatMessage.getText().toString().trim())) {
            DialogUtils.showAlert(FoodiePostCommentActivity.this, "You cannot send empty comment.");
            return false;
        } else {
            return true;
        }
    }

    private void getCommentsApiCall(String postId) {
        try {

            final GetPostCommentApiCall apiCall = new GetPostCommentApiCall(postId);
            HttpRequestHandler.getInstance(getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                ArrayList<FoodiePostCommentModel> tempMessageModelArrayList = apiCall.getResult();
                                foodiePostCommentModelArrayList.clear();
                                foodiePostCommentModelArrayList.addAll(tempMessageModelArrayList);
                                postCommentAdapter.notifyDataSetChanged();

                            } else {
                                DialogUtils.showAlert(FoodiePostCommentActivity.this, userModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), FoodiePostCommentActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), FoodiePostCommentActivity.this, null);
        }
    }
}
