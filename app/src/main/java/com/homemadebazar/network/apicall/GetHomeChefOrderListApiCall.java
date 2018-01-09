package com.homemadebazar.network.apicall;

import android.util.Log;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChefOrderModel;
import com.homemadebazar.model.IsAccountExistModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by atulraj on 10/12/17.
 */

public class GetHomeChefOrderListApiCall extends BaseApiCall {

    private String userId;
    private ArrayList<HomeChefOrderModel> homeChefOrderModelArrayList;

    public GetHomeChefOrderListApiCall(String userId) {
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
                homeChefOrderModelArrayList = JSONParsingUtils.parseHomeChefOrderList1(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<HomeChefOrderModel> getResult() {
        return homeChefOrderModelArrayList;
    }

    @Override
    public String getServiceURL() {
        return Constants.ServerURL.HOMECHEF_GET_ORDER_DETAILS;
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
