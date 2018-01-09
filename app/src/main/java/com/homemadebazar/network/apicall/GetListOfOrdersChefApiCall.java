package com.homemadebazar.network.apicall;

import android.util.Log;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChefOrderModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sumit on 27/08/17.
 */

public class GetListOfOrdersChefApiCall extends BaseApiCall {

    private String userId;
    private String orderFor;
    private BaseModel baseModel;
    private ArrayList<HomeChefOrderModel> homeChefOrderModelArrayList;

    public GetListOfOrdersChefApiCall(String userId, String orderFor) {
        this.userId = userId;
        this.orderFor = orderFor;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("UserId", userId);
            obj.put("OderFor", orderFor);

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
                if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS)
                    homeChefOrderModelArrayList = JSONParsingUtils.parseHomeChefOrderList(object.optJSONArray("Details").getJSONObject(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<HomeChefOrderModel> getResult() {
        return homeChefOrderModelArrayList;
    }

    @Override
    public String getServiceURL() {
        return Constants.ServerURL.FOODIE_GET_HOME_CHEF_ORDERS;
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
