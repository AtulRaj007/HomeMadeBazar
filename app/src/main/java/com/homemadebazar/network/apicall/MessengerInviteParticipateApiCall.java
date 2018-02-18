package com.homemadebazar.network.apicall;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.MessegeInviteParticipateModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sonu on 12/9/2017.
 */

public class MessengerInviteParticipateApiCall extends BaseApiCall {
    ArrayList<MessegeInviteParticipateModel> inviteParticipateList = new ArrayList<>();
    private String userId;
    private BaseModel baseModel;
    private double latitude, longitude;

    public MessengerInviteParticipateApiCall(String userId, double latitude, double longitude) {
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServerURL.FOODIE_MESSAGE_INVITE_PARTICIPATE_URL);
        return Constants.ServerURL.FOODIE_MESSAGE_INVITE_PARTICIPATE_URL;
    }

    @Override
    public Object getRequest() {

        JSONObject object = new JSONObject();
        try {
            object.put("UserId", userId);
            object.put("Lattitude", latitude);
            object.put("Longtitude", longitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Constants.ServiceTAG.REQUEST + object.toString());
        return object;
    }

    private void parseData(JSONObject object) {
        System.out.println(Constants.ServiceTAG.RESPONSE + object.toString());
        baseModel = JSONParsingUtils.parseBaseModel(object);

        if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
            JSONArray requestObjects = object.optJSONArray("RequestObjects");
            for (int i = 0; i < requestObjects.length(); i++) {
                JSONObject detailObj = requestObjects.optJSONObject(i);
                MessegeInviteParticipateModel messegeInvite = new MessegeInviteParticipateModel();
                messegeInvite.setEmailId(detailObj.optString("EmailId"));
                messegeInvite.setfName(detailObj.optString("FName"));
                messegeInvite.setlName(detailObj.optString("LName"));
                messegeInvite.setMobile(detailObj.optString("Mobile"));
                messegeInvite.setUserId(detailObj.optString("UserId"));
                messegeInvite.setDistance(detailObj.optString("Distance"));
                messegeInvite.setAddress(detailObj.optString("Address"));
                messegeInvite.setPinCode(detailObj.optString("Pincode"));
                messegeInvite.setProfileImage(detailObj.optString("DP"));
                messegeInvite.setStatus(detailObj.optString("Status"));
                messegeInvite.setNumericStatus(detailObj.optInt("StatusInNumeric"));

                inviteParticipateList.add(messegeInvite);
            }
        }
    }

    public ArrayList<MessegeInviteParticipateModel> getData() {
        return inviteParticipateList;
    }

    @Override
    public void parseResponseCode(Object response) throws JSONException {
        if (response instanceof JSONObject) {
            parseData((JSONObject) response);

        }
        super.parseResponseCode(response);
    }

    @Override
    public BaseModel getResult() {
        return baseModel;
    }
}
