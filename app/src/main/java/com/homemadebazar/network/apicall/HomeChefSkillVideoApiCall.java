package com.homemadebazar.network.apicall;

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
                if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                    homeChefSkillHubVideoModelArrayList = JSONParsingUtils.parseSkillHubVideoList(object);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<HomeChefSkillHubVideoModel> getResult() {
        return homeChefSkillHubVideoModelArrayList;
    }

    public BaseModel getBaseModel() {
        return baseModel;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.HOMECHEF_SKILL_HUB_VIDEOS);
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
