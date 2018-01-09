package com.homemadebazar.network.apicall;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.DishIngredientsModel;
import com.homemadebazar.model.IngredientsRowsModel;
import com.homemadebazar.model.MarketPlaceProductModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

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
        return object;
    }

    @Override
    public void parseResponseCode(Object response) throws JSONException {
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
