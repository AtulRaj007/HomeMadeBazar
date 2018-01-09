package com.homemadebazar.network.apicall;

import android.util.Log;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class ApplyOrderForHotDealsApiCall extends BaseApiCall {

    private String userId, orderId;
    private BaseModel baseModel;

//    {
//        "UserId":"17082730",
//            "Mobile":"9999999878",
//            "Password":"222222"
//    }

//    {
//        "OrderId":"Ord0011", "UserId":"20170142", "DiscTypeId":"DISC00000003", "OfferPeriodInHr":
//        "5", "OfferDealDate":"2017-11-09"
//    }

    public ApplyOrderForHotDealsApiCall(String userId, String orderId) {
        this.userId = userId;
        this.orderId = orderId;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("UserId", userId);
            obj.put("OrderId", orderId);
            obj.put("DiscTypeId", "DISC00000003");
            obj.put("OfferPeriodInHr", "30");
            obj.put("OfferDealDate", "2017-12-27");

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
        return Constants.ServerURL.HOMECHEF_APPLY_HOT_DEALS;
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
