package com.homemadebazar.model;

import java.util.ArrayList;

/**
 * Created by atulraj on 9/12/17.
 */

public class HomeChefProfileModel extends BaseModel {

    private String UserId;
    private String ShopName;
    private String PriceRange;
    private String Address;
    private String Speciality;
    private String ProfilePicture;
    private ArrayList<String> CoverPhotoUrl;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getSpeciality() {
        return Speciality;
    }

    public void setSpeciality(String speciality) {
        Speciality = speciality;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }

    public ArrayList<String> getCoverPhotoUrl() {
        return CoverPhotoUrl;
    }

    public void setCoverPhotoUrl(ArrayList<String> coverPhotoUrl) {
        CoverPhotoUrl = coverPhotoUrl;
    }

    public String getPriceRange() {
        return PriceRange;
    }

    public void setPriceRange(String priceRange) {
        PriceRange = priceRange;
    }
}
