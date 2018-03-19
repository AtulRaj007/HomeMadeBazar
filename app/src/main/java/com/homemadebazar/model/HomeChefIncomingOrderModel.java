package com.homemadebazar.model;

/**
 * Created by atulraj on 24/12/17.
 */

public class HomeChefIncomingOrderModel {

    private String foodiesDp;
    private String coverPhoto;
    private String foodiesFirstName;
    private String foodiesLastName;
    private String foodiesUserId;
    private String orderFor; // Breakfast Lunch Time
    private String orderId;     // Order Id
    private String orderRequestId;
    private String orderRequestDate;
    private String requestStatus;
    private String bookedDate;
    private String dishName;
    private String eatingTime;
    private String orderType;
    private String foodieEmailId;
    private String foodieMobileNumber;
    private String noOfGuest;
    private String price;
    private String discAmount;
    private String otp;
    private String dateTitle;
    private int type; // Now, Scheduled, Completed

    public String getOrderRequestDate() {
        return orderRequestDate;
    }

    public void setOrderRequestDate(String orderRequestDate) {
        this.orderRequestDate = orderRequestDate;
    }

    public String getFoodieEmailId() {
        return foodieEmailId;
    }

    public void setFoodieEmailId(String foodieEmailId) {
        this.foodieEmailId = foodieEmailId;
    }

    public String getFoodieMobileNumber() {
        return foodieMobileNumber;
    }

    public void setFoodieMobileNumber(String foodieMobileNumber) {
        this.foodieMobileNumber = foodieMobileNumber;
    }

    public String getNoOfGuest() {
        return noOfGuest;
    }

    public void setNoOfGuest(String noOfGuest) {
        this.noOfGuest = noOfGuest;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscAmount() {
        return discAmount;
    }

    public void setDiscAmount(String discAmount) {
        this.discAmount = discAmount;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getDateTitle() {
        return dateTitle;
    }

    public void setDateTitle(String dateTitle) {
        this.dateTitle = dateTitle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFoodiesDp() {
        return foodiesDp;
    }

    public void setFoodiesDp(String foodiesDp) {
        this.foodiesDp = foodiesDp;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getFoodiesFirstName() {
        return foodiesFirstName;
    }

    public void setFoodiesFirstName(String foodiesFirstName) {
        this.foodiesFirstName = foodiesFirstName;
    }

    public String getFoodiesLastName() {
        return foodiesLastName;
    }

    public void setFoodiesLastName(String foodiesLastName) {
        this.foodiesLastName = foodiesLastName;
    }

    public String getFoodiesUserId() {
        return foodiesUserId;
    }

    public void setFoodiesUserId(String foodiesUserId) {
        this.foodiesUserId = foodiesUserId;
    }

    public String getOrderFor() {
        return orderFor;
    }

    public void setOrderFor(String orderFor) {
        this.orderFor = orderFor;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderReqDate() {
        return orderRequestDate;
    }

    public void setOrderReqDate(String orderRequestDate) {
        this.orderRequestDate = orderRequestId;
    }

    public String getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(String bookedDate) {
        this.bookedDate = bookedDate;
    }

    public String getOrderRequestId() {
        return orderRequestId;
    }

    public void setOrderRequestId(String orderRequestId) {
        this.orderRequestId = orderRequestId;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getEatingTime() {
        return eatingTime;
    }

    public void setEatingTime(String eatingTime) {
        this.eatingTime = eatingTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
