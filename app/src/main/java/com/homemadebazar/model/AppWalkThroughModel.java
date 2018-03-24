package com.homemadebazar.model;

import java.io.Serializable;

/**
 * Created by atulraj on 24/3/18.
 */

public class AppWalkThroughModel implements Serializable {

    private boolean isShowToFoodie = false;
    private boolean isShowToHomeChef = false;
    private boolean isShowToMarketPlace = false;

    public boolean isShowToFoodie() {
        return isShowToFoodie;
    }

    public void setShowToFoodie(boolean showToFoodie) {
        isShowToFoodie = showToFoodie;
    }

    public boolean isShowToHomeChef() {
        return isShowToHomeChef;
    }

    public void setShowToHomeChef(boolean showToHomeChef) {
        isShowToHomeChef = showToHomeChef;
    }

    public boolean isShowToMarketPlace() {
        return isShowToMarketPlace;
    }

    public void setShowToMarketPlace(boolean showToMarketPlace) {
        isShowToMarketPlace = showToMarketPlace;
    }
}
