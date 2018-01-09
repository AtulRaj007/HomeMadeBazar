package com.homemadebazar.model;

/**
 * Created by atulraj on 29/11/17.
 */

public class CreateOrderModel {

    private String dishName, foodCategoryId, dishPrice, minNoGuest, maxNoGuest, discount, drinks, guestRules, description, dishAvailableDay;
    private boolean isPetsAllowed, isVeg, isDiscount;
    private String breakFastTime, lunchTime, dinnerTime;

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getFoodCategoryId() {
        return foodCategoryId;
    }

    public void setFoodCategoryId(String foodCategoryId) {
        this.foodCategoryId = foodCategoryId;
    }

    public String getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(String dishPrice) {
        this.dishPrice = dishPrice;
    }

    public String getMinNoGuest() {
        return minNoGuest;
    }

    public void setMinNoGuest(String minNoGuest) {
        this.minNoGuest = minNoGuest;
    }

    public String getMaxNoGuest() {
        return maxNoGuest;
    }

    public void setMaxNoGuest(String maxNoGuest) {
        this.maxNoGuest = maxNoGuest;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDrinks() {
        return drinks;
    }

    public void setDrinks(String drinks) {
        this.drinks = drinks;
    }

    public String getGuestRules() {
        return guestRules;
    }

    public void setGuestRules(String guestRules) {
        this.guestRules = guestRules;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDishAvailableDay() {
        return dishAvailableDay;
    }

    public void setDishAvailableDay(String dishAvailableDay) {
        this.dishAvailableDay = dishAvailableDay;
    }

    public boolean isPetsAllowed() {
        return isPetsAllowed;
    }

    public void setPetsAllowed(boolean petsAllowed) {
        isPetsAllowed = petsAllowed;
    }

    public boolean isVeg() {
        return isVeg;
    }

    public void setVeg(boolean veg) {
        isVeg = veg;
    }

    public boolean isDiscount() {
        return isDiscount;
    }

    public void setDiscount(boolean discount) {
        isDiscount = discount;
    }

    public String getBreakFastTime() {
        return breakFastTime;
    }

    public void setBreakFastTime(String breakFastTime) {
        this.breakFastTime = breakFastTime;
    }

    public String getLunchTime() {
        return lunchTime;
    }

    public void setLunchTime(String lunchTime) {
        this.lunchTime = lunchTime;
    }

    public String getDinnerTime() {
        return dinnerTime;
    }

    public void setDinnerTime(String dinnerTime) {
        this.dinnerTime = dinnerTime;
    }
}
