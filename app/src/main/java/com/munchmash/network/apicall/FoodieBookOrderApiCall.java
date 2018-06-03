package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class FoodieBookOrderApiCall extends BaseApiCall {

    private String userId, homeChefUserId, orderId, bookedDate, orderBookedFor;
    private BaseModel baseModel;
    private String bookingId;
    private int noOfPerson;

    public FoodieBookOrderApiCall(String userId, String homeChefUserId, String orderId, String bookedDate, String orderBookedFor, int noOfPerson) {
        this.userId = userId;
        this.homeChefUserId = homeChefUserId;
        this.orderId = orderId;
        this.bookedDate = bookedDate;
        this.orderBookedFor = orderBookedFor;
        this.noOfPerson = noOfPerson;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("FoodieUserId", userId);
            obj.put("CheifUserId", homeChefUserId);
            obj.put("OderId", orderId);
            obj.put("BookedDate", bookedDate);
            obj.put("OrderBookedFor", orderBookedFor);  // orderBookedFor ::::: 1 -> Breakfast 2 -> Lunch 3 -> Dinner
            obj.put("NosOfPerson", noOfPerson);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(Constants.ServiceTAG.REQUEST + obj.toString());
        return obj;
    }

    private void parseData(String response) {
        System.out.println(Constants.ServiceTAG.RESPONSE + response);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                baseModel = JSONParsingUtils.parseBaseModel(object);
                if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                    bookingId = object.optString("ReqeustRefNo");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getBookingId() {
        return bookingId;
    }

    @Override
    public BaseModel getResult() {
        return baseModel;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.FOODIE_BOOK_ORDER);
        return Constants.ServerURL.FOODIE_BOOK_ORDER;
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
