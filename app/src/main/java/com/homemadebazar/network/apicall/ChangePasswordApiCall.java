package com.homemadebazar.network.apicall;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Atul on 1/11/18.
 */

public class ChangePasswordApiCall extends BaseApiCall {

    private String userId;
    private String oldPassword;
    private String newPassword;
    private BaseModel baseModel;

    public ChangePasswordApiCall(String userId, String oldPassword, String newPassword) {
        this.userId = userId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    private void parseData(String response) {
        System.out.println(Constants.ServiceTAG.RESPONSE + response);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                baseModel = JSONParsingUtils.parseBaseModel(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public BaseModel getResult() {
        return baseModel;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.CHANGE_PASSWORD);
        return Constants.ServerURL.CHANGE_PASSWORD;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("UserId", userId);
            obj.put("OldPassword", oldPassword);
            obj.put("NewPassword", newPassword);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(Constants.ServiceTAG.REQUEST + obj.toString());
        return obj;
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
