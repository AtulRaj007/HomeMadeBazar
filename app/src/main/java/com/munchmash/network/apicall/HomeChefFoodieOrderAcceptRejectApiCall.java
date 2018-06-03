package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by atulraj on 5/1/18.
 */

public class HomeChefFoodieOrderAcceptRejectApiCall extends BaseApiCall {
    private String orderReferenceId, responseType;
    private BaseModel baseModel;
    private String userId;
    private String otp;

    public HomeChefFoodieOrderAcceptRejectApiCall(String userId, String orderReferenceId, String responseType, String otp) {
        this.orderReferenceId = orderReferenceId;
        this.responseType = responseType;
        this.otp = otp;
        this.userId = userId;
    }

    //    {"OrderRefId":"HMB00000001","ReponseType":"2"}
    @Override
    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("UserId", userId);
            object.put("OrderRefId", orderReferenceId);
            object.put("ReponseType", responseType);
            object.put("Otp", otp);

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
