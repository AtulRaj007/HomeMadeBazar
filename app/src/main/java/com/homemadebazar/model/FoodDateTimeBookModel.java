package com.homemadebazar.model;

/**
 * Created by Atul on 1/12/18.
 */

public class FoodDateTimeBookModel {

    private String date;
    private boolean isBreakFast;
    private boolean isLunch;
    private boolean isDinner;

    private String selectedDate;
    private String selectedDinnerTime;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isBreakFast() {
        return isBreakFast;
    }

    public void setBreakFast(boolean breakFast) {
        isBreakFast = breakFast;
    }

    public boolean isLunch() {
        return isLunch;
    }

    public void setLunch(boolean lunch) {
        isLunch = lunch;
    }

    public boolean isDinner() {
        return isDinner;
    }

    public void setDinner(boolean dinner) {
        isDinner = dinner;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getSelectedDinnerTime() {
        return selectedDinnerTime;
    }

    public void setSelectedDinnerTime(String selectedDinnerTime) {
        this.selectedDinnerTime = selectedDinnerTime;
    }
}
