package com.homemadebazar.network.apicall;

import android.util.Log;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChefSkillHubVideoModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sumit on 27/08/17.
 */

public class HomeChefSkillVideoApiCall extends BaseApiCall {

    private String userId;
    private ArrayList<HomeChefSkillHubVideoModel> homeChefSkillHubVideoModelArrayList;
    private BaseModel baseModel;

    public HomeChefSkillVideoApiCall(String userId) {
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
                homeChefSkillHubVideoModelArrayList = JSONParsingUtils.parseSkillHubVideoList(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<HomeChefSkillHubVideoModel> getResult() {
        return homeChefSkillHubVideoModelArrayList;
    }

    @Override
    public String getServiceURL() {
        return Constants.ServerURL.HOMECHEF_SKILL_HUB_VIDEOS;
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
