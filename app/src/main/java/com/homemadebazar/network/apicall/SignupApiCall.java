package com.homemadebazar.network.apicall;

import com.homemadebazar.model.UserModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class SignupApiCall extends BaseApiCall {

    private String userId, firstName, lastName, emailId, password, accountType, deviceToken, latitude, longitude, pincode;
    private UserModel userModel;

    public SignupApiCall(String userId, String firstName, String lastName, String emailId, String password,
                         String accountType, String deviceToken, String latitude, String longitude, String pinCode) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.password = password;
        this.accountType = accountType;
        this.deviceToken = deviceToken;
        this.deviceToken = deviceToken;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pincode = pinCode;
    }

    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("UserId", userId);
            object.put("FirstName", firstName);
            object.put("LastName", lastName);
            object.put("EmailId", emailId);
            object.put("Password", password);
            object.put("AccountType", accountType);
            object.put("DeviceToken", deviceToken);
            object.put("DeviceType", Constants.deviceType);
            object.put("Latitude", latitude);
            object.put("Longitude", longitude);
            object.put("PinCode", pincode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(Constants.ServiceTAG.REQUEST + object.toString());
        return object;
    }

    private void parseData(String response) {
        System.out.println(Constants.ServiceTAG.RESPONSE);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                userModel = JSONParsingUtils.getUserModel(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public UserModel getResult() {
        return userModel;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.SIGNUP);
        return Constants.ServerURL.SIGNUP;
    }

    @Override
    public void populateFromResponse(Object response) throws JSONException {
        if (response instanceof JSONObject) {
            parseResponseCode(response);
            parseData(response.toString());
        }
        super.populateFromResponse(response);
    }
}
