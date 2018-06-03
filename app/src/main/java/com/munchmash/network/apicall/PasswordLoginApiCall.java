package com.munchmash.network.apicall;

import com.munchmash.model.UserModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class PasswordLoginApiCall extends BaseApiCall {

    private String userId, mobile, password;
    private UserModel userModel;

    public PasswordLoginApiCall(String userId, String mobile, String password) {
        this.userId = userId;
        this.mobile = mobile;
        this.password = password;
    }

    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("UserId", userId);
            object.put("Mobile", mobile);
            object.put("Password", password);

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
                userModel = JSONParsingUtils.getUserModel(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public UserModel getResult() {
        return userModel;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.SIGNIN);
        return Constants.ServerURL.SIGNIN;
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
