package com.homemadebazar.network.apicall;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class SaveUserRatingApiCall extends BaseApiCall {

    private String userId;
    private String ratingToUserId;
    private int rating;
    private String ratingAgainstOrderId;
    private String ratingDesc;
    private BaseModel baseModel;

    public SaveUserRatingApiCall(String userId, String ratingToUserId, int rating, String ratingAgainstOrderId, String desc) {
        this.userId = userId;
        this.ratingToUserId = ratingToUserId;
        this.rating = rating;
        this.ratingAgainstOrderId = ratingAgainstOrderId;
        this.ratingDesc = desc;
    }

//    {
//        "RatingFrom":"1801132", "RatingTo":"1801133", "RatingPoint":"5.5", "RatingAgainstId":
//        "E097AF48", "AnyFeedBack":"Test Rating"
//    }

    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("RatingFrom", userId);
            object.put("RatingTo", ratingToUserId);
            object.put("RatingPoint", rating);
            object.put("RatingAgainstId", ratingAgainstOrderId);
            object.put("AnyFeedBack", ratingDesc);

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
                baseModel = JSONParsingUtils.getOtpModel(object);
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
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.SAVE_RATING_FEEDBACK);
        return Constants.ServerURL.SAVE_RATING_FEEDBACK;
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
