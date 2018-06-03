package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by atulraj on 5/1/18.
 */

public class GetContactSyncApiCall extends BaseApiCall {
    private String userId;
    private String contactCSVList;
    private BaseModel baseModel;

    //    {"PostId":"PM00000002"}
//    {"ParentUserId":"1801062","ContactSinkCSV":"8709646364,8882540094,9453333930,9555872016,9910364363"}
    public GetContactSyncApiCall(String userId, String contactsCSVList) {
        this.userId = userId;
        this.contactCSVList = contactsCSVList;
    }

    @Override
    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("ParentUserId", userId);
            object.put("ContactSinkCSV", contactCSVList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(Constants.ServiceTAG.REQUEST + object.toString());
        return object;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.GET_CONTACT_SYNC);
        return Constants.ServerURL.GET_CONTACT_SYNC;
    }

    @Override
    public void parseResponseCode(Object response) throws JSONException {
        if (response instanceof JSONObject) {
            parseData(response.toString());
        }
    }


    @Override
    public BaseModel getResult() {
        return baseModel;
    }

    private void parseData(String response) {
        System.out.println(Constants.ServiceTAG.RESPONSE + response);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                baseModel = JSONParsingUtils.parseBaseModel(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
