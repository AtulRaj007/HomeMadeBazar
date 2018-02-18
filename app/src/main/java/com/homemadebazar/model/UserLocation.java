package com.homemadebazar.model;

import java.io.Serializable;

/**
 * Created by atulraj on 19/2/18.
 */

public class UserLocation implements Serializable {
    private double latitude = 0.0;
    private double longitude = 0.0;
    private String address = "";

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
