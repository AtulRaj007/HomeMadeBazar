package com.munchmash.model;

/**
 * Created by atulraj on 23/12/17.
 */

public class IngredientsRowsModel {

    public static int DISH = 0, INGREDIENT = 1;
    private int type;
    private String dishName;
    private String discDesc;
    private String caloriesPerServing;
    private String preparationTime;
    private MarketPlaceProductModel marketPlaceProductModel;

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDiscDesc() {
        return discDesc;
    }

    public void setDiscDesc(String discDesc) {
        this.discDesc = discDesc;
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

    public MarketPlaceProductModel getMarketPlaceProductModel() {
        return marketPlaceProductModel;
    }

    public void setMarketPlaceProductModel(MarketPlaceProductModel marketPlaceProductModel) {
        this.marketPlaceProductModel = marketPlaceProductModel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
