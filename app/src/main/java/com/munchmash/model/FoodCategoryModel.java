package com.munchmash.model;

/**
 * Created by atulraj on 9/12/17.
 */

public class FoodCategoryModel {
    private String foodCategoryId;
    private String name;
    private String thumbnail;

    public String getFoodCategoryId() {
        return foodCategoryId;
    }

    public void setFoodCategoryId(String foodCategoryId) {
        this.foodCategoryId = foodCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return name;
    }
}
