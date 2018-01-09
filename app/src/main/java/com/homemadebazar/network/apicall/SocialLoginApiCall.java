package com.homemadebazar.network.apicall;

import android.util.Log;

import com.homemadebazar.model.UserModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class SocialLoginApiCall extends BaseApiCall {

    private String uniqueId,emailId,firstName,lastName,deviceToken
            ,latitude,longitude,pincode,signInType,profilePic;
    private boolean isSignupRequired=true;
    private UserModel userModel;

    public SocialLoginApiCall(String uniqueId, String emailId,String firstName, String lastName,
                               String deviceToken, String latitude, String longitude,
                              String pinCode,String signInType,String profilePic){
        this.uniqueId=uniqueId;
        this.emailId=emailId;
        this.firstName=firstName;
        this.lastName=lastName;
        this.deviceToken=deviceToken;
        this.latitude=latitude;
        this.longitude=longitude;
        this.pincode=pinCode;
        this.signInType=signInType;
        this.profilePic=profilePic;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("EmailId", emailId);
            obj.put("UniqueId", uniqueId);
            obj.put("Name", firstName+" "+lastName);
            obj.put("ProfilePic", "");
            obj.put("Latitude", latitude);
            obj.put("Longitude", longitude);
            obj.put("DeviceType", Constants.deviceType);
            obj.put("DeviceToken", deviceToken);
            obj.put("SignInType", signInType);
            obj.put("PinCode", profilePic);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("REQUEST= ", obj+"");
        return obj;
    }

    private void parseData(String response) {
        Log.d("RESPONSE= ", response);

        if (response != null && !response.isEmpty()) {
            try {
                JSONObject object = new JSONObject(response);
                isSignupRequired=(object.optString("SignUpRequired")).equals("1");
                userModel= JSONParsingUtils.getUserModel(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isSignUpRequired(){
        return isSignupRequired;
    }

    @Override
    public UserModel getResult() {
        return userModel;
    }

    @Override
    public String getServiceURL() {
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
