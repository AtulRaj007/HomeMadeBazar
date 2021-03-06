package com.munchmash.network.apicall;

import com.munchmash.model.BaseModel;
import com.munchmash.model.HomeChiefNearByModel;
import com.munchmash.util.Constants;
import com.munchmash.util.JSONParsingUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sumit on 27/08/17.
 */

public class FoodieHomeChefSearchApiCall extends BaseApiCall {

    private String latitude, longitude, foodCategoryId, searchString;
    private BaseModel baseModel;
    private ArrayList<HomeChiefNearByModel> chiefDetailList;

    public FoodieHomeChefSearchApiCall(String latitude, String longitude, String foodCategoryId, String searchString) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.foodCategoryId = foodCategoryId;
        this.searchString = searchString;
    }

    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("Lattitude", latitude);
            object.put("Longtitude", longitude);
            object.put("FoodCatId", foodCategoryId);
            object.put("SearchString", searchString);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(Constants.ServiceTAG.REQUEST + object.toString());
        return object;
    }

    private void parseData(JSONObject object) {
        System.out.println(Constants.ServiceTAG.RESPONSE + object.toString());
        baseModel = JSONParsingUtils.parseBaseModel(object);
        if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
            JSONArray homeChiefDetailsArray = object.optJSONArray("Details");
            chiefDetailList = new ArrayList<>();

            for (int i = 0; i < homeChiefDetailsArray.length(); i++) {
                JSONObject detailObj = homeChiefDetailsArray.optJSONObject(i);
                HomeChiefNearByModel homeChiefNearByModel = new HomeChiefNearByModel();
                homeChiefNearByModel.setUserId(detailObj.optString("UserId"));
                homeChiefNearByModel.setCountryCode(detailObj.optString("CountryCode"));
                homeChiefNearByModel.setCountryName(detailObj.optString("CountryName"));
                homeChiefNearByModel.setEmail(detailObj.optString("Email"));
                homeChiefNearByModel.setMobile(detailObj.optString("Mobile"));
                homeChiefNearByModel.setFirstName(detailObj.optString("FirstName"));
                homeChiefNearByModel.setLastName(detailObj.optString("LastName"));
                homeChiefNearByModel.setPinCode(detailObj.optString("PinCode"));
                homeChiefNearByModel.setAddress(detailObj.optString("Address"));
                homeChiefNearByModel.setProfileImage(detailObj.optString("DP"));
                homeChiefNearByModel.setRating(detailObj.optString("Rating"));
                homeChiefNearByModel.setDistance(detailObj.optString("Distance"));
                homeChiefNearByModel.setLatitude(detailObj.optString("Lattitude"));
                homeChiefNearByModel.setLongitude(detailObj.optString("Longitude"));
                homeChiefNearByModel.setPriceRange(detailObj.optString("PriceRange"));
                homeChiefNearByModel.setShopName(detailObj.optString("ShopName"));
                homeChiefNearByModel.setSpeciality(detailObj.optString("Speciality"));
                homeChiefNearByModel.setCoverPhotoArrayList(getCoverPhotoArray(detailObj.optString("CoverPhotoShow")));
                homeChiefNearByModel.setFavourite(false);

                chiefDetailList.add(homeChiefNearByModel);
            }
        }

    }

    private ArrayList<String> getCoverPhotoArray(String path) {
        ArrayList<String> coverPhotoArrayList = new ArrayList<>();
        String photos[] = path.split(";");
        if (photos != null && photos.length > 0) {
            for (String photo : photos) {
                coverPhotoArrayList.add(photo);
            }
        }
        return coverPhotoArrayList;
    }

    public ArrayList<HomeChiefNearByModel> getHomeChiefDetailList() {
        return chiefDetailList;
    }

    @Override
    public void parseResponseCode(Object response) throws JSONException {
        if (response instanceof JSONObject) {
            parseData((JSONObject) response);

        }
        super.parseResponseCode(response);
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.FOODIE_HOMECHEF_SEARCH);
        return Constants.ServerURL.FOODIE_HOMECHEF_SEARCH;
    }

    @Override
    public BaseModel getResult() {
        return baseModel;
    }
}
