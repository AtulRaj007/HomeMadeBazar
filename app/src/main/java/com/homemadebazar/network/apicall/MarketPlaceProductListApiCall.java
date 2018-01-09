package com.homemadebazar.network.apicall;

import android.util.Log;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.MarketPlaceProductModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

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
        JSONObject obj = new JSONObject();
        try {
            System.out.println();
        } catch (Exception e) {
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
        return Constants.ServerURL.MARKETPLACE_SHOW_PRODUCT_LIST +userId;
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
