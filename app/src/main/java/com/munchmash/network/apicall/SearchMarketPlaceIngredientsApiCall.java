package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.model.DishIngredientsModel;
import com.munchmash.model.IngredientsRowsModel;
import com.munchmash.model.MarketPlaceProductModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by atulraj on 23/12/17.
 */

public class SearchMarketPlaceIngredientsApiCall extends BaseApiCall {

    private String searchString;
    private BaseModel baseModel;
    private ArrayList<DishIngredientsModel> dishIngredientsModelArrayList;
    private ArrayList<IngredientsRowsModel> ingredientsRowsModelArrayList;
    private int type;
    private String dishName;
    private String descDesc;
    private String caloriesPerServing;
    private String preparationTime;
    private MarketPlaceProductModel marketPlaceProductModel;

    public SearchMarketPlaceIngredientsApiCall(String searchString) {
        this.searchString = searchString;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.HOMECHEF_SEARCH_MARKETPLACE_INGREDIENTS);
        return Constants.ServerURL.HOMECHEF_SEARCH_MARKETPLACE_INGREDIENTS;
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
            dishIngredientsModelArrayList = JSONParsingUtils.parseMarketPlaceIngredientsList((JSONObject) response);
            ingredientsRowsModelArrayList = parseIngredientsRowsList();
        }
        super.parseResponseCode(response);
    }

    @Override
    public Object getResult() {
        return dishIngredientsModelArrayList;
    }

    public BaseModel getBaseModel() {
        return baseModel;
    }

    public ArrayList<IngredientsRowsModel> getIngredientsRowsModelArrayList() {
        return ingredientsRowsModelArrayList;
    }

    private ArrayList<IngredientsRowsModel> parseIngredientsRowsList() {
        ArrayList<IngredientsRowsModel> ingredientsRowsModelArrayList = new ArrayList<>();
        for (int i = 0; i < dishIngredientsModelArrayList.size(); i++) {
            IngredientsRowsModel ingredientsRowsModel = new IngredientsRowsModel();
            ingredientsRowsModel.setType(IngredientsRowsModel.DISH);
            ingredientsRowsModel.setDishName(dishIngredientsModelArrayList.get(i).getDishName());
            ingredientsRowsModel.setDiscDesc(dishIngredientsModelArrayList.get(i).getDescDesc());
            ingredientsRowsModel.setPreparationTime(dishIngredientsModelArrayList.get(i).getPreparationTime());
            ingredientsRowsModel.setCaloriesPerServing(dishIngredientsModelArrayList.get(i).getCaloriesPerServing());
            ingredientsRowsModel.setMarketPlaceProductModel(null);

            ingredientsRowsModelArrayList.add(ingredientsRowsModel);

            for (int j = 0; j < dishIngredientsModelArrayList.get(i).getMarketPlaceProductModelArrayList().size(); j++) {
                IngredientsRowsModel tempIngredientsRowsModel = new IngredientsRowsModel();
                tempIngredientsRowsModel.setType(IngredientsRowsModel.INGREDIENT);
                tempIngredientsRowsModel.setMarketPlaceProductModel(dishIngredientsModelArrayList.get(i).getMarketPlaceProductModelArrayList().get(j));
                ingredientsRowsModelArrayList.add(tempIngredientsRowsModel);
            }
        }
        return ingredientsRowsModelArrayList;
    }
}
