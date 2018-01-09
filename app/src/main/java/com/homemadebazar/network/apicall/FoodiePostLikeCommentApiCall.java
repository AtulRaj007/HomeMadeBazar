package com.homemadebazar.network.apicall;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class FoodiePostLikeCommentApiCall extends BaseApiCall {

    private String postId, actionType, comments, actionDoneByUserId;
    private BaseModel baseModel;
    private int commentsCount, likesCount;

    public FoodiePostLikeCommentApiCall(String postId, String actionType, String comments, String actionDoneByUserId) {
        this.postId = postId;
        this.actionType = actionType;
        this.comments = comments;
        this.actionDoneByUserId = actionDoneByUserId;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("PostId", postId);
            obj.put("ActionType", actionType);
            obj.put("Comments", comments);
            obj.put("UserById", actionDoneByUserId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(Constants.ServiceTAG.REQUEST + obj.toString());
        return obj;
    }

    private void parseData(String response) {
        System.out.println(Constants.ServiceTAG.RESPONSE + response);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                baseModel = JSONParsingUtils.parseBaseModel(object);
                commentsCount = object.optInt("CommentsCount");
                likesCount = object.optInt("LikesCount");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public BaseModel getResult() {
        return baseModel;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    @Override
    public String getServiceURL() {
        String url = Constants.ServerURL.FOODIE_DO_LIKE_COMMENTS;
        System.out.println(Constants.ServiceTAG.REQUEST + url);
        return url;
    }

    @Override
    public void populateFromResponse(Object response) throws JSONException {
        if (response instanceof JSONObject) {
            parseResponseCode(response);
            parseData(response.toString());
        }
        super.populateFromResponse(response);
    }
}
