package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

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
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.MYSHOP_ADD_DETAILS);
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
