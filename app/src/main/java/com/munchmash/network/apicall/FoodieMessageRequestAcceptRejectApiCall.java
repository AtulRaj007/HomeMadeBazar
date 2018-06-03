package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sonu on 12/10/2017.
 */

public class FoodieMessageRequestAcceptRejectApiCall extends BaseApiCall {
    private String userId;
    private BaseModel baseModel;
    private String requestUserId;
    private String actionType;

    public FoodieMessageRequestAcceptRejectApiCall(String userId, String requestUserId, String actionType) {
        this.userId = userId;
        this.requestUserId = requestUserId;
        this.actionType = actionType;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.FOODIE_MESSAGE_REQUEST_ACCEPT_REJECT_URL);
        return Constants.ServerURL.FOODIE_MESSAGE_REQUEST_ACCEPT_REJECT_URL;
    }

    @Override
    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("UserId", userId);
            object.put("RequesterUserId", requestUserId);
            object.put("OperateAction", actionType);
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
