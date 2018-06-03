package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sonu on 12/9/2017.
 */

public class MessengerJoinSendRequestApiCall extends BaseApiCall {
    private String userId;
    private BaseModel baseModel;
    private String requestUserId;

    public MessengerJoinSendRequestApiCall(String userId, String requestUserId) {
        this.userId = userId;
        this.requestUserId = requestUserId;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.FOODIE_MESSAGE_INVITE_SENT_REQUEST_URL);
        return Constants.ServerURL.FOODIE_MESSAGE_INVITE_SENT_REQUEST_URL;
    }

    @Override
    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("ReqFromUserId", userId);
            object.put("ReqToUserIdInCommaSeparate", requestUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(Constants.ServiceTAG.REQUEST + object.toString());
        return object;
    }

    private void parseData(JSONObject object) {
        System.out.println(Constants.ServiceTAG.RESPONSE + object.toString());
        baseModel = JSONParsingUtils.parseBaseModel(object);
    }

    @Override
    public void parseResponseCode(Object response) throws JSONException {
        if (response instanceof JSONObject) {
            parseData((JSONObject) response);

        }
        super.parseResponseCode(response);
    }

    @Override
    public BaseModel getResult() {
        return baseModel;
    }
}
