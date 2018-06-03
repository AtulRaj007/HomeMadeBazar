package com.munchmash.network.apicall;

import com.munchmash.model.UserModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class SocialLoginApiCall extends BaseApiCall {

    private String uniqueId, emailId, firstName, lastName, deviceToken, latitude, longitude, pincode, signInType, profilePic;
    private boolean isSignupRequired = true;
    private UserModel userModel;

    public SocialLoginApiCall(String uniqueId, String emailId, String firstName, String lastName,
                              String deviceToken, String latitude, String longitude,
                              String pinCode, String signInType, String profilePic) {
        this.uniqueId = uniqueId;
        this.emailId = emailId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.deviceToken = deviceToken;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pincode = pinCode;
        this.signInType = signInType;
        this.profilePic = profilePic;
    }

    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("EmailId", emailId);
            object.put("UniqueId", uniqueId);
            object.put("Name", firstName + " " + lastName);
            object.put("ProfilePic", "");
            object.put("Latitude", latitude);
            object.put("Longitude", longitude);
            object.put("DeviceType", Constants.deviceType);
            object.put("DeviceToken", deviceToken);
            object.put("SignInType", signInType);
            object.put("PinCode", profilePic);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(Constants.ServiceTAG.REQUEST + object.toString());
        return object;
    }

    private void parseData(String response) {
        System.out.println(Constants.ServiceTAG.RESPONSE + response);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                isSignupRequired = (object.optString("SignUpRequired")).equals("1");
                userModel = JSONParsingUtils.getUserModel(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isSignUpRequired() {
        return isSignupRequired;
    }

    @Override
    public UserModel getResult() {
        return userModel;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.SOCIAL_LOGIN);
        return Constants.ServerURL.SOCIAL_LOGIN;
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
