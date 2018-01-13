package com.homemadebazar.network.apicall;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.ChatMessageModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by atulraj on 5/1/18.
 */

public class HomeChefFoodieOrderAcceptRejectApiCall extends BaseApiCall {
    private String orderReferenceId, responseType;
    private BaseModel baseModel;

    public HomeChefFoodieOrderAcceptRejectApiCall(String orderReferenceId, String responseType) {
        this.orderReferenceId = orderReferenceId;
        this.responseType = responseType;
    }

    //    {"OrderRefId":"HMB00000001","ReponseType":"2"}
    @Override
    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("OrderRefId", orderReferenceId);
            object.put("ReponseType", responseType);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(Constants.ServiceTAG.REQUEST + object.toString());
        return object;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.HOMECHEF_FOODIE_ORDER_ACCEPT_REJECT);
        return Constants.ServerURL.HOMECHEF_FOODIE_ORDER_ACCEPT_REJECT;
    }

    @Override
    public void parseResponseCode(Object response) throws JSONException {
        if (response instanceof JSONObject) {
            parseData(response.toString());
        }
    }

    public BaseModel getBaseModel() {
        return baseModel;
    }

    @Override
    public BaseModel getResult() {
        return baseModel;
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
}
