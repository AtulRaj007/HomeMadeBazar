package com.homemadebazar.model;

import java.util.ArrayList;

/**
 * Created by atulraj on 23/12/17.
 */

public class DishIngredientsModel {

    private String dishName;
    private String descDesc;
    private String caloriesPerServing;
    private String preparationTime;
    private ArrayList<MarketPlaceProductModel> marketPlaceProductModelArrayList;

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDescDesc() {
        return descDesc;
    }

    public void setDescDesc(String descDesc) {
        this.descDesc = descDesc;
    }

    public String getCaloriesPerServing() {
        return caloriesPerServing;
    }

    public void setCaloriesPerServing(String caloriesPerServing) {
        this.caloriesPerServing = caloriesPerServing;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }

    public ArrayList<MarketPlaceProductModel> getMarketPlaceProductModelArrayList() {
        return marketPlaceProductModelArrayList;
    }

    public void setMarketPlaceProductModelArrayList(ArrayList<MarketPlaceProductModel> marketPlaceProductModelArrayList) {
        this.marketPlaceProductModelArrayList = marketPlaceProductModelArrayList;
    }
}
