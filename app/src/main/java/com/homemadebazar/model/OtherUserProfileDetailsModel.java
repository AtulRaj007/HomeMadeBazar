package com.homemadebazar.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Atul on 1/29/18.
 */

public class OtherUserProfileDetailsModel implements Serializable {
    private String professionType;
    private String professionName;
    private String interest;
    private String userId;
    private String firstName;
    private String lastName;
    private String countryCode;
    private String countryName;
    private String emailId;
    private String address;
    private String dpStatus;
    private String isMobileVerified;
    private String latitude;
    private String longitude;
    private String mobile;
    private String pinCode;
    private int friendRequestNumeric;
    private String profilePic;
    private String accountType;
    private String rating;
    private ArrayList<RatingModel> ratingModelArrayList;

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

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDpStatus() {
        return dpStatus;
    }

    public void setDpStatus(String dpStatus) {
        this.dpStatus = dpStatus;
    }

    public String getProfessionType() {
        return professionType;
    }

    public void setProfessionType(String professionType) {
        this.professionType = professionType;
    }

    public String getProfessionName() {
        return professionName;
    }

    public void setProfessionName(String professionName) {
        this.professionName = professionName;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getIsMobileVerified() {
        return isMobileVerified;
    }

    public void setIsMobileVerified(String isMobileVerified) {
        this.isMobileVerified = isMobileVerified;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public int getFriendRequestNumeric() {
        return friendRequestNumeric;
    }

    public void setFriendRequestNumeric(int friendRequestNumeric) {
        this.friendRequestNumeric = friendRequestNumeric;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public ArrayList<RatingModel> getRatingModelArrayList() {
        return ratingModelArrayList;
    }

    public void setRatingModelArrayList(ArrayList<RatingModel> ratingModelArrayList) {
        this.ratingModelArrayList = ratingModelArrayList;
    }
}
