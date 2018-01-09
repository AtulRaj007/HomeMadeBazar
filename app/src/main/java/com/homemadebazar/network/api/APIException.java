package com.homemadebazar.network.api;

import java.io.Serializable;

/**
 * @author AND-09
 * 
 */
public class APIException extends Exception implements Serializable {
	/** */
	private static final long serialVersionUID = 1L;
	/**
	 * error code
	 */
	private String mCode;

	private String mErrorType;

    private String responseBody;

	/**
	 * @return the mErrorType
	 */
	public String getmErrorType() {
		return mErrorType;
	}

	/**
	 * @param mErrorType
	 *            the mErrorType to set
	 */
	public void setmErrorType(String mErrorType) {
		this.mErrorType = mErrorType;
	}

	/**
	 * @param code
	 * @param message
	 */
	public APIException(String code, String message) {
		super(message);
		this.mCode = code;
	}

    /**
     * @param code
     * @param message
     */
    public APIException(String code, String message, String responseBody) {
        super(message);
        this.mCode = code;
        this.responseBody = responseBody;
    }

	/**
	 * @param message
	 */
	public APIException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public APIException(Throwable cause) {
		super("Caused by: " + cause.toString());
	}

	/**
	 * @return error code
	 */
	public String getCode() {
		return mCode;
	}

    /**
     * Get responseBody
     * @return responseBody
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * Set responseBody
     * @param responseBody the new responseBody
     */
    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}