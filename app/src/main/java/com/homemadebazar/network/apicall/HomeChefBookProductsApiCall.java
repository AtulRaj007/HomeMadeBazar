package com.homemadebazar.network.apicall;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class HomeChefBookProductsApiCall extends BaseApiCall {

    private String userId;
    private String productIds;
    private String quanity;
    private BaseModel baseModel;
    private String referenceId;

    public HomeChefBookProductsApiCall(String userId, String productIds, String quanity) {
        this.userId = userId;
        this.productIds = productIds;
        this.quanity = quanity;
    }

    private void parseData(String response) {
        System.out.println(Constants.ServiceTAG.RESPONSE + response);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                baseModel = JSONParsingUtils.parseBaseModel(object);
                if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                    referenceId = object.optString("Ref.Id");
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

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.HOMECHEF_BOOK_PRODUCT);
        return Constants.ServerURL.HOMECHEF_BOOK_PRODUCT;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("UserId", userId);
            obj.put("ProductId", productIds);
            obj.put("Unit", quanity);

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

    public String getReferenceId() {
        return referenceId;
    }
}
