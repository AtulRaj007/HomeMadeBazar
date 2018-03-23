package com.homemadebazar.network.apicall;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.OtherUserProfileDetailsModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class GetUserProfileDetailsApiCall extends BaseApiCall {

    private String userId, forUserId;
    private OtherUserProfileDetailsModel userModel;
    private BaseModel baseModel;

    public GetUserProfileDetailsApiCall(String userId, String forUserId) {
        this.userId = userId;
        this.forUserId = forUserId;
    }

    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("UserId", userId);
            object.put("FromUserId", forUserId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(Constants.ServiceTAG.REQUEST + object.toString());
        return object;
    }

    private void parseData(String response) {
        System.out.println(Constants.ServiceTAG.RESPONSE + response);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                baseModel = JSONParsingUtils.parseBaseModel(object);
                if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS)
                    userModel = JSONParsingUtils.parseOtherUserModel(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public OtherUserProfileDetailsModel getResult() {
        return userModel;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.GET_USER_PROFILE_DETAILS);
        return Constants.ServerURL.GET_USER_PROFILE_DETAILS;
    }

    public BaseModel getBaseModel() {
        return baseModel;
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
