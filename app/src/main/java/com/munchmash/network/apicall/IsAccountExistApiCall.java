package com.munchmash.network.apicall;

import com.munchmash.model.IsAccountExistModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class IsAccountExistApiCall extends BaseApiCall {

    private String countryCode, countryName, mobileNumber, userId;
    private IsAccountExistModel isAccountExistModel;

    public IsAccountExistApiCall(String countryCode, String countryName, String mobileNumber, String userId) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.mobileNumber = mobileNumber;
        this.userId = userId;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("CountryCode", countryCode);
            obj.put("CountryName", countryName);
            obj.put("Mobile", mobileNumber);
            obj.put("UserId", userId);

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
                isAccountExistModel = JSONParsingUtils.getAccountExistsModel(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public IsAccountExistModel getResult() {
        return isAccountExistModel;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.IS_EMAIL_EXIST);
        return Constants.ServerURL.IS_EMAIL_EXIST;
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
