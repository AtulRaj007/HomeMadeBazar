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
        JSONObject object = new JSONObject();
        try {
            object.put("UserId", userId);
            object.put("Name", brandName);
            object.put("Description", description);

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
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.MARKETPLACE_ADD_BRAND);
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
