package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.model.UserModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sonu on 12/9/2017.
 */

public class MessengerFriendApiCall extends BaseApiCall {
    private String userId;
    private BaseModel baseModel;
    private ArrayList<UserModel> friendList;

    public MessengerFriendApiCall(String userId) {
        this.userId = userId;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.FOODIE_MESSAGE_FRIENDS_URL);
        return Constants.ServerURL.FOODIE_MESSAGE_FRIENDS_URL;
    }

    @Override
    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("UserId", userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Constants.ServiceTAG.REQUEST + object.toString());
        return object;
    }

    private void parseData(JSONObject object) {
        baseModel = JSONParsingUtils.parseBaseModel(object);
        if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
            friendList = JSONParsingUtils.parseUserFriendsAndRequests(object);
        }

    }

    @Override
    public void parseResponseCode(Object response) throws JSONException {
        System.out.println(Constants.ServiceTAG.RESPONSE + response.toString());
        if (response instanceof JSONObject) {
            parseData((JSONObject) response);
        }
        super.parseResponseCode(response);
    }

    public BaseModel getBaseModel() {
        return baseModel;
    }

    @Override
    public ArrayList<UserModel> getResult() {
        return friendList;
    }
}
