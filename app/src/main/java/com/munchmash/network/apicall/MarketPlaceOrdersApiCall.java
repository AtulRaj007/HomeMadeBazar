package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.model.MarketPlaceMyOrdersModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sumit on 27/08/17.
 */

public class MarketPlaceOrdersApiCall extends BaseApiCall {

    private String userId, viewFor;
    private BaseModel baseModel;
    private ArrayList<MarketPlaceMyOrdersModel> marketPlaceMyOrdersModels;


    public MarketPlaceOrdersApiCall(String userId, String viewFor) {
        this.userId = userId;
        this.viewFor = viewFor;
    }

    private void parseData(String response) {
        System.out.println(Constants.ServiceTAG.RESPONSE + response);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                baseModel = JSONParsingUtils.parseBaseModel(object);
                if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                    marketPlaceMyOrdersModels = JSONParsingUtils.parseMarketPlaceMyOrders(object.optJSONArray("ProductList"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<MarketPlaceMyOrdersModel> getResult() {
        return marketPlaceMyOrdersModels;
    }


    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.MARKETPLACE_ORDERS);
        return Constants.ServerURL.MARKETPLACE_ORDERS;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("UserId", userId);
            obj.put("ViewFor", viewFor);

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
