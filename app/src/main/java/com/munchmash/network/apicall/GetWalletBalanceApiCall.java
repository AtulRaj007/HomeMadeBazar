package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class GetWalletBalanceApiCall extends BaseApiCall {

    private String userId;
    private BaseModel baseModel;
    private String accountId;
    private Double walletBalance;

    public GetWalletBalanceApiCall(String userId) {
        this.userId = userId;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("UserId", userId);

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
                accountId = object.optString("AccountId");
                walletBalance = object.optDouble("Amount");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object getResult() {
        return baseModel;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.GET_WALLET_BALANCE);
        return Constants.ServerURL.GET_WALLET_BALANCE;
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
