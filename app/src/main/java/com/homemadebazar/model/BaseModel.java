package com.homemadebazar.model;

import java.io.Serializable;

/**
 * Created by Sumit on 27/08/17.
 */

public class BaseModel implements Serializable{
    protected int statusCode;
    protected String statusMessage;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
