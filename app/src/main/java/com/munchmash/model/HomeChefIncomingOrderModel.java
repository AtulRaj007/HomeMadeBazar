package com.munchmash.model;

/**
 * Created by atulraj on 24/12/17.
 */

public class HomeChefIncomingOrderModel {

    /* Foodie */
    private String foodiesUserId;
    private String foodiesDp;
    private String coverPhoto;
    private String foodiesFirstName;
    private String foodiesLastName;
    private String foodieEmailId;
    private String foodieMobileNumber;
    private String foodieProfession;
    private String foodieLatitude;
    private String foodieLongitude;

    private String orderFor; // Breakfast Lunch Time
    private String orderId;     // Order Id
    private String orderRequestId;
    private String orderRequestDate;
    private String requestStatus;
    private String bookedDate;
    private String dishName;
    private String eatingTime;
    private String orderType;
    private String noOfGuest;
    private String price;
    private String discAmount;
    private String otp;
    private String dateTitle;
    private int type; // Now, Scheduled, Completed Foodies Order

    /* Home Chef */
    private String hcUserId;
    private String hcDp;
    private String hcFirstName;
    private String hcLastName;
    private String hcMobileNumber;
    private String hcEmail;
    private String hcProfession;
    private String hcLatitude;
    private String hcLongitude;



    /*
//    "HCDP": "http://18.219.188.20/api/CreateOrder/GetImage?Source=ImageGallary%5C%5C1801132%5CDP%5Ccropped703112258.jpg",
            "HCFirstName": "Atul",
            "HCLastName": "Raj",
            "HCUserId": "1801132",
            "HCMobile": "9654489093",
            "FoodiesProfession": "RST",
            "HCLatitude": "28.4880097",
            "HCLongtitude": "77.0694491",
            "FoodiesLatitude": "28.4868546",
            "FoodiesLongtitude": "77.0667495"*/

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
        this.orderRequestDate = orderRequestDate;
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

    public String getFoodieProfession() {
        return foodieProfession;
    }

    public void setFoodieProfession(String foodieProfession) {
        this.foodieProfession = foodieProfession;
    }

    public String getFoodieLatitude() {
        return foodieLatitude;
    }

    public void setFoodieLatitude(String foodieLatitude) {
        this.foodieLatitude = foodieLatitude;
    }

    public String getFoodieLongitude() {
        return foodieLongitude;
    }

    public void setFoodieLongitude(String foodieLongitude) {
        this.foodieLongitude = foodieLongitude;
    }

    public String getHcUserId() {
        return hcUserId;
    }

    public void setHcUserId(String hcUserId) {
        this.hcUserId = hcUserId;
    }

    public String getHcDp() {
        return hcDp;
    }

    public void setHcDp(String hcDp) {
        this.hcDp = hcDp;
    }

    public String getHcFirstName() {
        return hcFirstName;
    }

    public void setHcFirstName(String hcFirstName) {
        this.hcFirstName = hcFirstName;
    }

    public String getHcLastName() {
        return hcLastName;
    }

    public void setHcLastName(String hcLastName) {
        this.hcLastName = hcLastName;
    }

    public String getHcMobileNumber() {
        return hcMobileNumber;
    }

    public void setHcMobileNumber(String hcMobileNumber) {
        this.hcMobileNumber = hcMobileNumber;
    }

    public String getHcEmail() {
        return hcEmail;
    }

    public void setHcEmail(String hcEmail) {
        this.hcEmail = hcEmail;
    }

    public String getHcProfession() {
        return hcProfession;
    }

    public void setHcProfession(String hcProfession) {
        this.hcProfession = hcProfession;
    }

    public String getHcLatitude() {
        return hcLatitude;
    }

    public void setHcLatitude(String hcLatitude) {
        this.hcLatitude = hcLatitude;
    }

    public String getHcLongitude() {
        return hcLongitude;
    }

    public void setHcLongitude(String hcLongitude) {
        this.hcLongitude = hcLongitude;
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
