package com.homemadebazar.network.apicall;

import android.util.Log;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

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
        Log.d("REQUEST= ", obj + "");
        return obj;
    }

    /*
    "{
            ""StatusCode"": ""100"",
            ""StatusMessage"": ""Successful"",
            ""AccountId"": ""HMBWA00000003"",
            ""Amount"": ""100.00""
}"
*/
    private void parseData(String response) {
        Log.d("RESPONSE= ", response);

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
