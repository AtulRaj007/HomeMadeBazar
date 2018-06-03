package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 28/08/17.
 */


public class DeviceLoginLogoutApiCall extends BaseApiCall {
    private String userId;
    private String token;
    private int loginHistory;
    private BaseModel baseModel;

    //    {"DeviceType":"","Token":"","UserId":""}
    public DeviceLoginLogoutApiCall(String userId, String token, int loginHistory) {
        this.userId = userId;
        this.token = token;
        this.loginHistory = loginHistory;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("UserId", userId);
            obj.put("Token", token);
            obj.put("DeviceType", Constants.deviceType);

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
        if (loginHistory == Constants.LoginHistory.LOGIN) {
            System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.DEVICE_LOGIN);
            return Constants.ServerURL.DEVICE_LOGIN;
        } else {
            System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.DEVICE_LOGOUT);
            return Constants.ServerURL.DEVICE_LOGOUT;
        }

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
