package com.homemadebazar.network.api;


import com.homemadebazar.network.HttpRequestHandler;

import org.json.JSONException;

import java.util.HashMap;


/**
 * @author AND-09
 * 
 */
public abstract class ApiCall {
    protected HashMap<String, String> headers = null;
    public enum RequestType {
        JSON_OBJECT_REQUEST, JSON_ARRAY_REQUEST
    }

    public interface OnApiCallCompleteListener {
        void onComplete(Exception e);
    }

    /**
	 * @return the base URL
	 */

	public abstract String getServiceURL();

	/**
	 * @param params
	 *            used to add the parameter
	 */

	public void addParameters(HashMap params) {

	}
    public HashMap<String, String> getHeaders() {
        return headers;
    }

	/**
	 * Method is used to receive the response
	 * 
	 * @param response
	 * @throws JSONException
	 */
	public void populateFromResponse(Object response) throws JSONException {

	}

	/**
	 * Method to return the result got from the API call
	 * 
	 * @return result, if any problem occur then return null
	 */
    public Object getResult() {
        return null;
    }

	/**
	 * Method to return the ResponseCode got from the API call
	 * 
	 * @return result, if any problem occur then return null
	 */
	public String getErrorCode() {
		return null;
	}

    public String getErrorDesc() {
        return null;
    }

    public RequestType getRequestType() {
        return RequestType.JSON_OBJECT_REQUEST;
    }

    public Object getRequest() {
        return null;
    }

    public Object getTag() {
        return HttpRequestHandler.DEFAULT_REQUEST_TAG;
    }
}