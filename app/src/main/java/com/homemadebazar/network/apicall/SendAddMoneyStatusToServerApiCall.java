package com.homemadebazar.network.apicall;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class SendAddMoneyStatusToServerApiCall extends BaseApiCall {

    private String transactionId, accountId, status, amount;
    private BaseModel baseModel;
    private Double walletBalance;

    //    {"TransactionNumber":"Trans00000004","Status":"Success","AccountId":"HMBWA00000003"}
    public SendAddMoneyStatusToServerApiCall(String transactionId, String status, String accountId) {
        this.transactionId = transactionId;
        this.status = status;
        this.accountId = accountId;
    }
//    {
//        "StatusCode": "100",
//            "StatusMessage": "Successful",
//            "AccountId": "HMBWA00000003",
//            "NewWalletAmount": "600.00"
//    }

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
        return Constants.ServerURL.SEND_ADD_MONEY_STATUS;
    }

    //    {"TransactionNumber":"Trans00000004","Status":"Success","AccountId":"HMBWA00000003"}
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
