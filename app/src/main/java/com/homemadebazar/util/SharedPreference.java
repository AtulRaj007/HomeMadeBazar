package com.homemadebazar.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.homemadebazar.model.AppWalkThroughModel;
import com.homemadebazar.model.HomeChefProfileModel;
import com.homemadebazar.model.MarketPlaceProductModel;
import com.homemadebazar.model.UserLocation;
import com.homemadebazar.model.UserModel;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by and-02 on 22/6/17.
 */

public class SharedPreference {

    public static String PREF_NAME = "RipplePreference";
    public static SharedPreferences sSharedPreference;
    public static String USER_MODEL = "USER_MODEL";
    public static String PROFILE_MODEL = "PROFILE_MODEL";
    private static String PRODUCT_MODEL = "PRODUCT_MODEL";
    private static String USER_LOCATION = "USER_LOCATION";
    private static String WALKTHROUGH_MODEL = "WALKTHROUGH_MODEL";

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
        UserModel userModel = new UserModel();
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

    public static void saveProfileModel(Context context, HomeChefProfileModel homeChefProfileModel) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString(PROFILE_MODEL, ObjectSerializer.serialize(homeChefProfileModel));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }

    public static HomeChefProfileModel getProfileModel(Context context) {
        HomeChefProfileModel homeChefProfileModel = new HomeChefProfileModel();
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        try {
            homeChefProfileModel = (HomeChefProfileModel) ObjectSerializer.deserialize(prefs.getString(PROFILE_MODEL, ObjectSerializer.serialize(new HomeChefProfileModel())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return homeChefProfileModel;
    }


    public static void clearSharedPreference(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        try {
            prefs.edit().clear().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveShoppingCart(Context context, ArrayList<MarketPlaceProductModel> marketPlaceProductModel) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString(PRODUCT_MODEL, ObjectSerializer.serialize(marketPlaceProductModel));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }

    public static ArrayList<MarketPlaceProductModel> getShoppingCart(Context context) {
        ArrayList<MarketPlaceProductModel> marketPlaceProductModelArrayList = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        try {
            marketPlaceProductModelArrayList = (ArrayList<MarketPlaceProductModel>) ObjectSerializer.deserialize(prefs.getString(PRODUCT_MODEL, ObjectSerializer.serialize(new ArrayList<MarketPlaceProductModel>())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return marketPlaceProductModelArrayList;
    }

    public static void clearShoppingCart(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        try {
            prefs.edit().remove(PROFILE_MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveUserLocation(Context context, UserLocation userLocation) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            try {
                editor.putString(USER_LOCATION, ObjectSerializer.serialize(userLocation));
            } catch (IOException e) {
                e.printStackTrace();
            }
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UserLocation getUserLocation(Context context) {
        UserLocation userLocation;
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            userLocation = (UserLocation) ObjectSerializer.deserialize(prefs.getString(USER_LOCATION, ObjectSerializer.serialize(new UserLocation())));

        } catch (Exception e) {
            e.printStackTrace();
            userLocation = new UserLocation();
        }
        return userLocation;
    }

    public static void saveWalkThroughModel(Context context, AppWalkThroughModel appWalkThroughModel) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            try {
                editor.putString(WALKTHROUGH_MODEL, ObjectSerializer.serialize(appWalkThroughModel));
            } catch (Exception e) {
                e.printStackTrace();
            }
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AppWalkThroughModel getWalkThroughModel(Context context) {
        AppWalkThroughModel appWalkThroughModel;
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            appWalkThroughModel = (AppWalkThroughModel) ObjectSerializer.deserialize(prefs.getString(WALKTHROUGH_MODEL, ObjectSerializer.serialize(new AppWalkThroughModel())));

        } catch (Exception e) {
            e.printStackTrace();
            appWalkThroughModel = new AppWalkThroughModel();
        }
        return appWalkThroughModel;
    }
}
