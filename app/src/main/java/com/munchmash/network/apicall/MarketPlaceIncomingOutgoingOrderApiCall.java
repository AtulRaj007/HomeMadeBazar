package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.model.MarketPlaceOrderModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by atulraj on 10/12/17.
 */

public class MarketPlaceIncomingOutgoingOrderApiCall extends BaseApiCall {

    private String userId;
    private String tabId;
    private BaseModel baseModel;
    private ArrayList<MarketPlaceOrderModel> marketPlaceOrderModelArrayList;

    public MarketPlaceIncomingOutgoingOrderApiCall(String userId, String tabId) {
        this.userId = userId;
        this.tabId = tabId;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("UserId", userId);
            obj.put("TabId", tabId);

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
                if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS)
                    marketPlaceOrderModelArrayList = JSONParsingUtils.parseMarketPlaceOrders(object.optJSONArray("IncomingOrders"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<MarketPlaceOrderModel> getResult() {
        return marketPlaceOrderModelArrayList;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.MARKETPLACE_INCOMING_OUTGOING_ORDERS);
        return Constants.ServerURL.MARKETPLACE_INCOMING_OUTGOING_ORDERS;
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
