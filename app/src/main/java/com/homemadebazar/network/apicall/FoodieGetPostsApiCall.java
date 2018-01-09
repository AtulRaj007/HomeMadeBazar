package com.homemadebazar.network.apicall;

import android.util.Log;

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
        JSONObject obj = new JSONObject();
        try {
            obj.put("UserId", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("REQUEST= ", obj + "");
        return obj;
    }

    private void parseData(String response) {
        Log.d("RESPONSE= ", response);

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
