package com.munchmash.model;

import java.io.Serializable;

/**
 * Created by Sumit on 27/08/17.
 */

public class IsAccountExistModel extends BaseModel implements Serializable {
    private String countryCode;
    private String Mobile;
    private boolean isMobileVerified;
    private String userId;
    private boolean isSignUpRequired;

    public String getCountryCode() {
        return countryCode;
    }

    public String getMobile() {
        return Mobile;
    }

    public boolean isMobileVerified() {
        return isMobileVerified;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isSignUpRequired() {
        return isSignUpRequired;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public void setMobileVerified(boolean mobileVerified) {
        isMobileVerified = mobileVerified;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSignUpRequired(boolean signUpRequired) {
        isSignUpRequired = signUpRequired;
    }
}
