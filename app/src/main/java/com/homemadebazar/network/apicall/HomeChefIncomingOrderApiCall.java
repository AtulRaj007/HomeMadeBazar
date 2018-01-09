package com.homemadebazar.network.apicall;

import android.util.Log;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChefIncomingOrderModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sumit on 27/08/17.
 */

public class HomeChefIncomingOrderApiCall extends BaseApiCall {

    private String userId, tabSection;
    private BaseModel baseModel;
    private ArrayList<HomeChefIncomingOrderModel> homeChefIncomingOrderModelArrayList = new ArrayList<>();

    //    {"UserId":"17112218","TabSection":"NOW"}
    public HomeChefIncomingOrderApiCall(String userId, String tabSection) {
        this.userId = userId;
        this.tabSection = tabSection;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("UserId", userId);
            obj.put("TabSection", tabSection);

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
                homeChefIncomingOrderModelArrayList = JSONParsingUtils.parseHomeChefIncomingOrder(object.optJSONArray("Ordered"),1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<HomeChefIncomingOrderModel> getResult() {
        return homeChefIncomingOrderModelArrayList;
    }

    public BaseModel getBaseModel() {
        return baseModel;
    }

    @Override
    public String getServiceURL() {
        return Constants.ServerURL.HOMECHEF_SHOW_ORDERED_LIST;
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
