package com.homemadebazar.model;

import java.io.Serializable;

/**
 * Created by Atul on 3/19/18.
 */

public class FoodTimingModel implements Serializable {

    private String breakFastStartTime = "9:30 AM";
    private String breakFastEndTime = "11:00 AM";
    private String lunchStartTime = "1:00 PM";
    private String lunchEndTime = "3:00 PM";
    private String dinnerStartTime = "7:30 AM";
    private String dinnerEndTime = "11:30 AM";

    public String getBreakFastStartTime() {
        return breakFastStartTime;
    }

    public void setBreakFastStartTime(String breakFastStartTime) {
        this.breakFastStartTime = breakFastStartTime;
    }

    public String getBreakFastEndTime() {
        return breakFastEndTime;
    }

    public void setBreakFastEndTime(String breakFastEndTime) {
        this.breakFastEndTime = breakFastEndTime;
    }

    public String getLunchStartTime() {
        return lunchStartTime;
    }

    public void setLunchStartTime(String lunchStartTime) {
        this.lunchStartTime = lunchStartTime;
    }

    public String getLunchEndTime() {
        return lunchEndTime;
    }

    public void setLunchEndTime(String lunchEndTime) {
        this.lunchEndTime = lunchEndTime;
    }

    public String getDinnerStartTime() {
        return dinnerStartTime;
    }

    public void setDinnerStartTime(String dinnerStartTime) {
        this.dinnerStartTime = dinnerStartTime;
    }

    public String getDinnerEndTime() {
        return dinnerEndTime;
    }

    public void setDinnerEndTime(String dinnerEndTime) {
        this.dinnerEndTime = dinnerEndTime;
    }
}
