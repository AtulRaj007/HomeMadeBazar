package com.homemadebazar.network.apicall;

import android.util.Log;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class MarketPlaceProductAddBrandApiCall extends BaseApiCall {

    private String userId, brandName, description;
    private BaseModel baseModel;

    public MarketPlaceProductAddBrandApiCall(String userId, String brandName, String description) {
        this.userId = userId;
        this.brandName = brandName;
        this.description = description;

    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("UserId", userId);
            obj.put("Name", brandName);
            obj.put("Description", description);

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
        return Constants.ServerURL.MARKETPLACE_ADD_BRAND;
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
