package com.homemadebazar.network.apicall;

import android.content.Context;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class GetTotalSalesOfTheDayApiCall extends BaseApiCall {

    private Context context;
    private String userId;
    private BaseModel baseModel;
    private double productPrice;
    private double totalSale;

    public GetTotalSalesOfTheDayApiCall(Context context, String userId) {
        this.context = context;
        this.userId = userId;
    }

    //    {"StatusCode":"100","StatusMessage":"Successful","ProductPrice":"1973.00","DaySalePrice":"0.00"}
    private void parseData(String response) {
        System.out.println(Constants.ServiceTAG.RESPONSE + response);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                baseModel = JSONParsingUtils.parseBaseModel(object);
                if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                    try {
                        productPrice = Double.parseDouble(object.optString("ProductPrice"));
                        totalSale = Double.parseDouble(object.optString("DaySalePrice"));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    DialogUtils.showAlert(context, baseModel.getStatusMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public BaseModel getResult() {
        return baseModel;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public double getTotalSale() {
        return totalSale;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.MARKETPLACE_GET_TOTAL_SALE_OF_DAY);
        return Constants.ServerURL.MARKETPLACE_GET_TOTAL_SALE_OF_DAY;
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
