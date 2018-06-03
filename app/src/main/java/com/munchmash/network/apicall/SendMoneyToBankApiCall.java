package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class SendMoneyToBankApiCall extends BaseApiCall {

    private String userId, accountNumber, accountHolderName, ifscCode, amount;
    private BaseModel baseModel;


    public SendMoneyToBankApiCall(String userId, String accountNumber, String accountHolderName, String ifscCode, String amount) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.ifscCode = ifscCode;
        this.amount = amount;
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

    @Override
    public BaseModel getResult() {
        return baseModel;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.REQUEST + Constants.ServerURL.SEND_MONEY_TO_BANK);
        return Constants.ServerURL.SEND_MONEY_TO_BANK;
    }


    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("UserId", userId);
            obj.put("Account", accountNumber);
            obj.put("HolderName", accountHolderName);
            obj.put("IFSC", ifscCode);
            obj.put("Amount", amount);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(Constants.ServiceTAG.REQUEST + obj.toString());
        return obj;
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
