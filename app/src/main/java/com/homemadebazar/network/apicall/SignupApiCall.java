package com.homemadebazar.network.apicall;

import android.util.Log;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit on 27/08/17.
 */

public class SignupApiCall extends BaseApiCall {

    private String userId,firstName,lastName,emailId,password,accountType,deviceToken,latitude,longitude,pincode;
    private UserModel userModel;

    public SignupApiCall(String userId,String firstName,String lastName,String emailId,String password,
                         String accountType,String deviceToken,String latitude,String longitude,String pinCode){
        this.userId=userId;
        this.firstName=firstName;
        this.lastName=lastName;
        this.emailId=emailId;
        this.password=password;
        this.accountType=accountType;
        this.deviceToken=deviceToken;
        this.deviceToken=deviceToken;
        this.latitude=latitude;
        this.longitude=longitude;
        this.pincode=pinCode;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("UserId", userId);
            obj.put("FirstName", firstName);
            obj.put("LastName", lastName);
            obj.put("EmailId", emailId);
            obj.put("Password", password);
            obj.put("AccountType", accountType);
            obj.put("DeviceToken", deviceToken);
            obj.put("DeviceType", Constants.deviceType);
            obj.put("Latitude", latitude);
            obj.put("Longitude", longitude);
            obj.put("PinCode", pincode);

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
                userModel= JSONParsingUtils.getUserModel(object);
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
