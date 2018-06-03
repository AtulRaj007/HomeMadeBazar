package com.munchmash.model;

/**
 * Created by atulraj on 4/3/18.
 */

public class ProfileInterestsModel {
    private int iconId;
    private String interestName;
    private boolean selected;

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getInterestName() {
        return interestName;
    }

    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
