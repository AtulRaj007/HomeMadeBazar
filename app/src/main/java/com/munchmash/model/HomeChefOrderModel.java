package com.munchmash.model;

import java.util.ArrayList;

/**
 * Created by atulraj on 10/12/17.
 */

public class HomeChefOrderModel {

    private String OrderId;
    private String dishName;
    private String category;
    private String price;
    private String minGuest;
    private String maxGuest;
    private String discount;
    private boolean petsAllowed;
    private String drinks;
    private String vegNonVeg;
    private String rules;
    private String description;
    private ArrayList<String> foodImagesArrayList = new ArrayList<>();
    private String orderFromDate;
    private String orderValidTill;
    private String dishAvailability;
    private String breakFastTime;
    private String lunchTime;
    private String dinnerTime;
    private String orderType;
    private String orderTime;

    private String userId;
    private String firstName;
    private String lastName;
    private String profilePic;

    private ArrayList<FoodDateTimeBookModel> foodDateTimeBookModels;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderValidTill() {
        return orderValidTill;
    }

    public void setOrderValidTill(String orderValidTill) {
        this.orderValidTill = orderValidTill;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public ArrayList<FoodDateTimeBookModel> getFoodDateTimeBookModels() {
        return foodDateTimeBookModels;
    }

    public void setFoodDateTimeBookModels(ArrayList<FoodDateTimeBookModel> foodDateTimeBookModels) {
        this.foodDateTimeBookModels = foodDateTimeBookModels;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMinGuest() {
        return minGuest;
    }

    public void setMinGuest(String minGuest) {
        this.minGuest = minGuest;
    }

    public String getMaxGuest() {
        return maxGuest;
    }

    public void setMaxGuest(String maxGuest) {
        this.maxGuest = maxGuest;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public boolean isPetsAllowed() {
        return petsAllowed;
    }

    public void setPetsAllowed(boolean petsAllowed) {
        this.petsAllowed = petsAllowed;
    }

    public String getDrinks() {
        return drinks;
    }

    public void setDrinks(String drinks) {
        this.drinks = drinks;
    }

    public String getVegNonVeg() {
        return vegNonVeg;
    }

    public void setVegNonVeg(String vegNonVeg) {
        this.vegNonVeg = vegNonVeg;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getFoodImagesArrayList() {
        return foodImagesArrayList;
    }

    public void setFoodImagesArrayList(ArrayList<String> foodImagesArrayList) {
        this.foodImagesArrayList = foodImagesArrayList;
    }

    public String getOrderFromDate() {
        return orderFromDate;
    }

    public void setOrderFromDate(String orderFromDate) {
        this.orderFromDate = orderFromDate;
    }

    public String getDishAvailability() {
        return dishAvailability;
    }

    public void setDishAvailability(String dishAvailability) {
        this.dishAvailability = dishAvailability;
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