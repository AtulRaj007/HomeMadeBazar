package com.homemadebazar.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by atulraj on 24/3/18.
 */

public class MarketPlaceOrderModel implements Serializable {

    private String orderId;
    private String userId;
    private String name;
    private String countryCode;
    private String countryName;
    private String mobileNumber;
    private String email;
    private String latitude;
    private String longitude;
    private String address;
    private String profileImage;
    private String pinCode;
    private ArrayList<MarketPlaceOrderProductModel> marketPlaceOrderProductModelArrayList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public ArrayList<MarketPlaceOrderProductModel> getMarketPlaceOrderProductModelArrayList() {
        return marketPlaceOrderProductModelArrayList;
    }

    public void setMarketPlaceOrderProductModelArrayList(ArrayList<MarketPlaceOrderProductModel> marketPlaceOrderProductModelArrayList) {
        this.marketPlaceOrderProductModelArrayList = marketPlaceOrderProductModelArrayList;
    }
}
