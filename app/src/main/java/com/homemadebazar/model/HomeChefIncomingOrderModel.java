package com.homemadebazar.model;

/**
 * Created by atulraj on 24/12/17.
 */

public class HomeChefIncomingOrderModel {
//    {
//        "FoodiesDP": "http://35.183.8.236/api/CreateOrder/GetImage?Source=ImageGallary%5C%5C17120827%5CDP%5Ccropped1936886421.jpg",
//            "CoverPhoto": "http://35.183.8.236/api/CreateOrder/GetImage?Source=ImageGallary%5C%5C17112218%5C62A54065%5CDishImages%5CScreenshot_20171215-022632.png",
//            "FoodiesFirstName": "Foodie",
//            "FoodiesLastName": "foodie",
//            "FoodiesUserId": "17120827",
//            "OrderFor": "Breakfast",
//            "OrderId": "62A54065",
//            "OrderReqDT": "Dec 24 2017  7:00:30:660AM",
//            "BookedDate": "2017-12-25",
//            "OrderReqId": "HMB00000023",
//            "ReqStatus": "Request",
//            "DishName": "CHICKEN BIRYANI",
//            "EattingTime": "1720",
//            "OrderType": "SCHEDULE"
//    },

    private String foodiesDp;
    private String coverPhoto;
    private String foodiesFirstName;
    private String foodiesLastName;
    private String foodiesUserId;
    private String orderFor;
    private String orderId;
    private String orderReqDate;
    private String bookedDate;
    private String orderRequestId;
    private String requestStatus;
    private String dishName;
    private String eatingTime;
    private String orderType;

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

    private String dateTitle;
    private int type=0; //0-> Now, 1-> Scheduled

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
        return orderReqDate;
    }

    public void setOrderReqDate(String orderReqDate) {
        this.orderReqDate = orderReqDate;
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
