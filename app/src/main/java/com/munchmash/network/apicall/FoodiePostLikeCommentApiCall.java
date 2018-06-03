package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class FoodiePostLikeCommentApiCall extends BaseApiCall {

    private String postId, actionType, actionDoneByUserId;
    private BaseModel baseModel;
    private int commentsCount, likesCount;
    private String comments, dateTime;
    private String userId;
    private String firstName, lastName, userProfile;

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
                comments = object.optString("Comments");
                dateTime = object.optString("Datetime");
                userId = object.optString("UserId");
                firstName = object.optString("FirstName");
                lastName = object.optString("LastName");
                userProfile = object.optString("PosterDPUrl");

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

    public String getComments() {
        return comments;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserProfile() {
        return userProfile;
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
