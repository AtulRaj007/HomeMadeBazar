package com.homemadebazar.network.apicall;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.DishIngredientsModel;
import com.homemadebazar.model.IngredientsRowsModel;
import com.homemadebazar.model.MarketPlaceProductModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by atulraj on 23/12/17.
 */

public class SearchMarketPlaceProductsApiCall extends BaseApiCall {

    private String searchString;
    private BaseModel baseModel;
    private ArrayList<MarketPlaceProductModel> marketPlaceProductModelArrayList;

    public SearchMarketPlaceProductsApiCall(String searchString) {
        this.searchString = searchString;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.MARKET_PLACE_PRODUCT_SEARCH);
        return Constants.ServerURL.MARKET_PLACE_PRODUCT_SEARCH;
    }

    @Override
    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("SearchWord", searchString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Constants.ServiceTAG.REQUEST + object.toString());
        return object;
    }

    @Override
    public void parseResponseCode(Object response) throws JSONException {
        System.out.println(Constants.ServiceTAG.RESPONSE + response.toString());
        if (response instanceof JSONObject) {
            baseModel = JSONParsingUtils.parseBaseModel((JSONObject) response);
            marketPlaceProductModelArrayList = JSONParsingUtils.parseMarketPlaceProductModelList((JSONObject) response);
        }
        super.parseResponseCode(response);
    }

    @Override
    public ArrayList<MarketPlaceProductModel> getResult() {
        return marketPlaceProductModelArrayList;
    }

    public BaseModel getBaseModel() {
        return baseModel;
    }

}
