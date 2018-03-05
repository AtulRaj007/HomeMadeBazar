package com.homemadebazar.model;

import android.text.TextUtils;

/**
 * Created by atulraj on 5/3/18.
 */

public class CustomAddress {
    private static String SYMBOL = "@@";
    private static String SPACE = ", ";
    private String completeAddress = "";
    private String apartmentNumber = "";
    private String streetNumber = "";
    private String area = "";
    private String city = "";
    private String state = "";

    public CustomAddress(String completeAddress) {
        this.completeAddress = completeAddress;
        try {
            String address[] = completeAddress.split(SYMBOL);
            apartmentNumber = address[0];
            streetNumber = address[1];
            area = address[2];
            city = address[3];
            state = address[4];
        } catch (IndexOutOfBoundsException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCompleteAddress(String apartmentNumber, String streetNumber, String area, String city, String state) {
        return apartmentNumber + SYMBOL + streetNumber + SYMBOL + area + SYMBOL + city + SYMBOL + state;
    }

    public static String getCompleteAddress(String completeAddress) {
        try {
            if (!TextUtils.isEmpty(completeAddress)) {
                return completeAddress.replaceAll(SYMBOL, SPACE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getCompleteAddress() {
        return completeAddress;
    }

    public void setCompleteAddress(String completeAddress) {
        this.completeAddress = completeAddress;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
