package com.homemadebazar.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.homemadebazar.model.UserModel;

import java.io.IOException;


/**
 * Created by and-02 on 22/6/17.
 */

public class SharedPreference {

    public static String PREF_NAME = "RipplePreference";
    public static SharedPreferences sSharedPreference;
    public static String USER_MODEL = "USER_MODEL";

    public static void setStringPreference(Context context, String key, String value) {
        sSharedPreference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sSharedPreference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringPreference(Context context, String key) {
        sSharedPreference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sSharedPreference.getString(key, "");
    }

    public static void saveUserModel(Context context, UserModel userModel) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString(USER_MODEL, ObjectSerializer.serialize(userModel));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }

    public static UserModel getUserModel(Context context) {
        UserModel userModel=new UserModel();
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        try {
            userModel = (UserModel) ObjectSerializer.deserialize(prefs.getString(USER_MODEL, ObjectSerializer.serialize(new UserModel())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userModel;
    }

    public static void clearSharedPreference(Context context){
        SharedPreferences prefs=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        try{
            prefs.edit().clear().commit();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
