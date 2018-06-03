package com.munchmash.network.apicall;


import com.munchmash.network.api.ApiCall;
import com.munchmash.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;



public class BaseApiCall extends ApiCall {
    private String errorCode;
    private String errorDesc;


    public BaseApiCall() {
        super();
        headers = new HashMap<String, String>();

    }

    @Override
    public String getServiceURL() {
        return Constants.ServerURL.BASE_URL;
    }

    @Override
    public void populateFromResponse(Object response) throws JSONException {
        parseResponseCode(response);
    }

    public void parseResponseCode(Object response) throws JSONException {
        if (response instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) response;
            errorCode = jsonObject.optString("StatusCode");
            errorDesc = jsonObject.optString("StatusMessage");
        }
    }

    @Override
    public String getErrorCode() {
        if (errorCode.length() == 0) {
            return null;
        }
        return errorCode;
    }

    @Override
    public String getErrorDesc() {
        return errorDesc;
    }

}
