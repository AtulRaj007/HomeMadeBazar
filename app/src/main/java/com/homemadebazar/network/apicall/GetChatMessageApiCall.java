package com.homemadebazar.network.apicall;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.ChatMessageModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by atulraj on 5/1/18.
 */

public class GetChatMessageApiCall extends BaseApiCall {
    private String userId, targetUserId, sequenceOrder, timeStamp;
    private ArrayList<ChatMessageModel> chatMessageModelArrayList;
    private BaseModel baseModel;

    public GetChatMessageApiCall(String userId, String targetUserId, String sequenceOrder, String timeStamp) {
        this.userId = userId;
        this.targetUserId = targetUserId;
        this.sequenceOrder = sequenceOrder;
        this.timeStamp = timeStamp;
    }

    @Override
    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("UserId", userId);
            object.put("ChatWithUserId", targetUserId);
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
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.GET_MESSAGES);
        return Constants.ServerURL.GET_MESSAGES;
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
    public ArrayList<ChatMessageModel> getResult() {
        return chatMessageModelArrayList;
    }

    private void parseData(String response) {
        System.out.println(Constants.ServiceTAG.RESPONSE + response);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                baseModel = JSONParsingUtils.parseBaseModel(object);
                if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS)
                    chatMessageModelArrayList = JSONParsingUtils.parseChatModelList(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
