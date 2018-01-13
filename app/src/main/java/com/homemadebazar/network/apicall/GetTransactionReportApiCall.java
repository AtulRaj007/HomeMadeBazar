package com.homemadebazar.network.apicall;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.TransactionModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Atul on 1/11/18.
 */

public class GetTransactionReportApiCall extends BaseApiCall {

    private String userId;
    private BaseModel baseModel;
    private ArrayList<TransactionModel> transactionModelArrayList;

    //    {"UserId":"1801062"}
    public GetTransactionReportApiCall(String userId) {
        this.userId = userId;
    }

    private void parseData(String response) {
        System.out.println(Constants.ServiceTAG.RESPONSE + response);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                baseModel = JSONParsingUtils.parseBaseModel(object);
                transactionModelArrayList = JSONParsingUtils.parseTransactionReportList(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<TransactionModel> getResult() {
        return transactionModelArrayList;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.GET_TRANSACTION_REPORT);
        return Constants.ServerURL.GET_TRANSACTION_REPORT;
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
