package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class PayMoneyApiCall extends BaseApiCall {

    private String mobileNumber, accountId, amount, description;
    private BaseModel baseModel;
    private String transactionStatus;
    private double walletBalance;

    //    {"Amount":"50","MobileNumber":"9654489093","AccountId":"HMBWA00000003","Description":"Test"}
    public PayMoneyApiCall(String accountId, String mobileNumber, String amount, String description) {
        this.accountId = accountId;
        this.mobileNumber = mobileNumber;
        this.amount = amount;
        this.description = description;
    }

    //    {
//        "StatusCode": "100",
//            "StatusMessage": "Successful",
//            "AccountId": "HMBWA00000003",
//            "TransactionStatus": "Success",
//            "WalletAmount": "550.00"
//    }
    private void parseData(String response) {
        System.out.println(Constants.ServiceTAG.RESPONSE + response);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                baseModel = JSONParsingUtils.parseBaseModel(object);
                transactionStatus = object.optString("TransactionStatus");
                walletBalance = object.optDouble("WalletAmount");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    @Override
    public BaseModel getResult() {
        return baseModel;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.PAY_MONEY);
        return Constants.ServerURL.PAY_MONEY;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("AccountId", accountId);
            obj.put("MobileNumber", mobileNumber);
            obj.put("Amount", amount);
            obj.put("Description", description);

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
