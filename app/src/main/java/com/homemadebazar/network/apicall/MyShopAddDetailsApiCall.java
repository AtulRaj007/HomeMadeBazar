package com.homemadebazar.network.apicall;

import android.util.Log;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class MyShopAddDetailsApiCall extends BaseApiCall {

    private String userId, shopName, priceRange, address, foodSpeciality;
    private BaseModel baseModel;

    public MyShopAddDetailsApiCall(String userId, String shopName, String priceRange, String address, String foodSpeciality) {
        this.userId = userId;
        this.shopName = shopName;
        this.priceRange = priceRange;
        this.address = address;
        this.foodSpeciality = foodSpeciality;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("UserId", userId);
            obj.put("ShopName", shopName);
            obj.put("PriceRange", priceRange);
            obj.put("Address1", address);
            obj.put("Speciality", foodSpeciality);

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
        return Constants.ServerURL.MYSHOP_ADD_DETAILS;
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
