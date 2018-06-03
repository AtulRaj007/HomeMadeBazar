package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.model.MarketPlaceProductModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by atulraj on 10/12/17.
 */

public class MarketPlaceProductListApiCall extends BaseApiCall {

    private BaseModel baseModel;
    private ArrayList<MarketPlaceProductModel> marketPlaceProductModelArrayList;
    private String userId;

    public MarketPlaceProductListApiCall(String userId) {
        this.userId = userId;
    }

    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            System.out.println();
        } catch (Exception e) {
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
                marketPlaceProductModelArrayList = JSONParsingUtils.parseProductModelList(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<MarketPlaceProductModel> getResult() {
        return marketPlaceProductModelArrayList;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.MARKETPLACE_SHOW_PRODUCT_LIST + userId);
        return Constants.ServerURL.MARKETPLACE_SHOW_PRODUCT_LIST + userId;
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
