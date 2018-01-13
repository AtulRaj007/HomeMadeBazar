package com.homemadebazar.network.apicall;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.FoodieFlashPostModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sumit on 27/08/17.
 */

public class FoodieGetPostsApiCall extends BaseApiCall {

    private String userId;
    private ArrayList<FoodieFlashPostModel> foodieFlashPostModelArrayList;
    private BaseModel baseModel;

    public FoodieGetPostsApiCall(String userId) {
        this.userId = userId;
    }

    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("UserId", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(Constants.ServiceTAG.REQUEST + object.toString());
        return object;
    }

    private void parseData(String response) {
        System.out.println(Constants.ServiceTAG.RESPONSE + response);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                baseModel = JSONParsingUtils.parseBaseModel(object);
                if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS)
                    foodieFlashPostModelArrayList = JSONParsingUtils.parseFoodieFlashPostsList(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<FoodieFlashPostModel> getResult() {
        return foodieFlashPostModelArrayList;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.FOODIE_GET_FLASH_POST);
        return Constants.ServerURL.FOODIE_GET_FLASH_POST;
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
