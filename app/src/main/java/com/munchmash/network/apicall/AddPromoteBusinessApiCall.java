package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class AddPromoteBusinessApiCall extends BaseApiCall {

    private String orderId;
    private BaseModel baseModel;
    private String promoteBusinessId;

    public AddPromoteBusinessApiCall(String orderId) {
        this.orderId = orderId;
    }

    private void parseData(String response) {
        System.out.println(Constants.ServiceTAG.RESPONSE + response);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                baseModel = JSONParsingUtils.parseBaseModel(object);
                promoteBusinessId = object.optString("PromotBussinessId");
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
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.APPLY_PROMOTE_BUSINESS);
        return Constants.ServerURL.APPLY_PROMOTE_BUSINESS;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("OrderID", orderId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(Constants.ServiceTAG.REQUEST + obj.toString());
        return obj;
    }

    public String getPromoteBusinessId() {
        return promoteBusinessId;
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
