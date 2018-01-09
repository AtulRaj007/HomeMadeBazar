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

public class PasswordLoginApiCall extends BaseApiCall {

    private String userId,mobile,password;
    private UserModel userModel;

//    {
//        "UserId":"17082730",
//            "Mobile":"9999999878",
//            "Password":"222222"
//    }

    public PasswordLoginApiCall(String userId,String mobile,String password){
        this.userId=userId;
        this.mobile=mobile;
        this.password=password;
    }

    public Object getRequest() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("UserId", userId);
            obj.put("Mobile", mobile);
            obj.put("Password", password);

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
        return Constants.ServerURL.SIGNIN;
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
