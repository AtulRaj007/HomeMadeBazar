package com.homemadebazar.network.apicall;

import android.util.Log;

import com.homemadebazar.model.HomeChefProfileModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 28/08/17.
 */


public class ShowHomeChefProfileApiCall extends BaseApiCall {
    private String userId;
    private HomeChefProfileModel homeChefProfileModel;

    public ShowHomeChefProfileApiCall(String userId) {
        this.userId = userId;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("UserId", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("REQUEST= ", obj + "");
        return obj;
    }

    private void parseData(String response) {
        Log.d("RESPONSE= ", response);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                homeChefProfileModel = JSONParsingUtils.parseHomeChefProfileModel(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public HomeChefProfileModel getResult() {
        return homeChefProfileModel;
    }

    @Override
    public String getServiceURL() {
        return Constants.ServerURL.SHOW_HOME_CHEF_PROFILE+userId;
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
