package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class SendAddMoneyStatusToServerApiCall extends BaseApiCall {

    private String transactionId, accountId, status, amount;
    private BaseModel baseModel;
    private Double walletBalance;

    public SendAddMoneyStatusToServerApiCall(String transactionId, String status, String accountId) {
        this.transactionId = transactionId;
        this.status = status;
        this.accountId = accountId;
    }

    private void parseData(String response) {
        System.out.println(Constants.ServiceTAG.RESPONSE + response);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                baseModel = JSONParsingUtils.parseBaseModel(object);
                walletBalance = object.optDouble("NewWalletAmount");
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
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.SEND_ADD_MONEY_STATUS);
        return Constants.ServerURL.SEND_ADD_MONEY_STATUS;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("TransactionNumber", transactionId);
            obj.put("Status", status);
            obj.put("AccountId", accountId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(Constants.ServiceTAG.REQUEST + obj.toString());
        return obj;
    }

    public Double getWalletBalance() {
        return walletBalance;
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
