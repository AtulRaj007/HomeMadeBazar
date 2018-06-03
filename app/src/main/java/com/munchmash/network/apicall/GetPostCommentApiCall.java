package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.model.FoodiePostCommentModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by atulraj on 5/1/18.
 */

public class GetPostCommentApiCall extends BaseApiCall {
    private String postId;
    private ArrayList<FoodiePostCommentModel> foodiePostCommentModels;
    private BaseModel baseModel;

    //    {"PostId":"PM00000002"}
    public GetPostCommentApiCall(String postId) {
        this.postId = postId;
    }

    @Override
    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("PostId", postId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(Constants.ServiceTAG.REQUEST + object.toString());
        return object;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.GET_COMMENT_LIST);
        return Constants.ServerURL.GET_COMMENT_LIST;
    }

    @Override
    public void parseResponseCode(Object response) throws JSONException {
        if (response instanceof JSONObject) {
            parseData(response.toString());
        }
    }

    public BaseModel getBaseModel() {
        return baseModel;
    }

    @Override
    public ArrayList<FoodiePostCommentModel> getResult() {
        return foodiePostCommentModels;
    }

    private void parseData(String response) {
        System.out.println(Constants.ServiceTAG.RESPONSE + response);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                baseModel = JSONParsingUtils.parseBaseModel(object);
                if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS)
                    foodiePostCommentModels = JSONParsingUtils.parseFoodiePostCommentList(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
