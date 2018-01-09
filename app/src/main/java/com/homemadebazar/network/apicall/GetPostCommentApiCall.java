package com.homemadebazar.network.apicall;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.ChatMessageModel;
import com.homemadebazar.model.FoodiePostCommentModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by atulraj on 5/1/18.
 */

public class GetPostCommentApiCall extends BaseApiCall {
    private String postId, sequenceOrder, timeStamp;
    private ArrayList<FoodiePostCommentModel> foodiePostCommentModels;
    private BaseModel baseModel;

    //    {"PostId":"PM00000002"}
    public GetPostCommentApiCall(String postId, String sequenceOrder, String timeStamp) {
        this.postId = postId;
        this.sequenceOrder = sequenceOrder;
        this.timeStamp = timeStamp;
    }

    @Override
    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("PostId", postId);
            object.put("SquenceOrder", sequenceOrder);
            object.put("TimeStamp", timeStamp);

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
