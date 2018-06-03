package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.model.CreateOrderModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by atulraj on 29/11/17.
 */

public class HomeChefCreateOrderApiCall extends BaseApiCall {
    private String userId;
    private CreateOrderModel createOrderModel;
    private BaseModel baseModel;
    private String orderId;

    public HomeChefCreateOrderApiCall(String userId, CreateOrderModel createOrderModel) {
        this.userId = userId;
        this.createOrderModel = createOrderModel;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.CREATE_ORDER);
        return Constants.ServerURL.CREATE_ORDER;
    }

    @Override
    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("UserId", userId);

            object.put("DishName", createOrderModel.getDishName());
            object.put("FoodCategoryId", createOrderModel.getFoodCategoryId());
            object.put("Price", createOrderModel.getDishPrice());
            object.put("MinGuestNo", createOrderModel.getMinNoGuest());
            object.put("MaxGuestNo", createOrderModel.getMaxNoGuest());
            object.put("DiscAmount", createOrderModel.getDiscount());
            object.put("IsDiscApply", createOrderModel.isDiscount());
            object.put("IsPetAllow", createOrderModel.isPetsAllowed());
            object.put("Drink", createOrderModel.getDrinks());
            object.put("IsVeg", createOrderModel.isVeg());

            object.put("RuleDescption", createOrderModel.getGuestRules());
            object.put("Description", createOrderModel.getDescription());

            object.put("DishAvailableDay", createOrderModel.getDishAvailableDay());
            object.put("BreakFastTime", createOrderModel.getBreakFastTime());
            object.put("LunchTime", createOrderModel.getLunchTime());
            object.put("DinnerTime", createOrderModel.getDinnerTime());


        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(Constants.ServiceTAG.RESPONSE + object.toString());
        return object;
    }

    private void parseData(JSONObject object) {
        System.out.println(Constants.ServiceTAG.RESPONSE + object.toString());
        baseModel = JSONParsingUtils.parseBaseModel(object);
        orderId = object.optString("OrderId");
    }

    public String getOrderId() {
        return orderId;
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
