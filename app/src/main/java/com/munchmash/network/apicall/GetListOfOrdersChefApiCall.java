package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.model.HomeChefOrderModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

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
        JSONObject object = new JSONObject();
        try {
            object.put("UserId", userId);
            object.put("OderFor", orderFor);

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
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.FOODIE_GET_HOME_CHEF_ORDERS);
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
