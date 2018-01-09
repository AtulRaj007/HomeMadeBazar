package com.homemadebazar.network.apicall;

import android.util.Log;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.MessegeInviteParticipateModel;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sonu on 12/9/2017.
 */

public class MessengerJoinSendRequestApiCall extends BaseApiCall {
    private String userId;
    private BaseModel baseModel;
    private String requestUserId;
    public MessengerJoinSendRequestApiCall(String userId, String requestUserId) {
        this.userId = userId;
       this.requestUserId=requestUserId;
    }

    @Override
    public String getServiceURL() {
        return Constants.ServerURL.FOODIE_MESSAGE_INVITE_SENT_REQUEST_URL;
    }

    @Override
    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("ReqFromUserId", userId);
            object.put("ReqToUserIdInCommaSeparate",requestUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("REQUEST= ", object + "");
        return object;
    }

    private void parseData(JSONObject object) {
        System.out.println("Response:-" + object);
        baseModel = JSONParsingUtils.parseBaseModel(object);
    }

    public String getStatus() {
        return "";
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
