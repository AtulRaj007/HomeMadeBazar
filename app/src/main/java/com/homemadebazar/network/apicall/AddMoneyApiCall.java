package com.homemadebazar.network.apicall;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class AddMoneyApiCall extends BaseApiCall {

    private String accountId, amount;
    private BaseModel baseModel;
    private String transactionId;
    private String token;


    public AddMoneyApiCall(String accountId, String amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

    private void parseData(String response) {
        System.out.println(Constants.ServiceTAG.RESPONSE + response);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                baseModel = JSONParsingUtils.parseBaseModel(object);
                transactionId = object.optString("TransactionId");
                token = object.optString("Token");
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
        return Constants.ServerURL.ADD_MONEY;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("AccountId", accountId);
            obj.put("Amount", amount);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(Constants.ServiceTAG.REQUEST + obj.toString());
        return obj;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getToken() {
        return token;
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
