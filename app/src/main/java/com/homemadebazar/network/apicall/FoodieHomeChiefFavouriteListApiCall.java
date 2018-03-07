package com.homemadebazar.network.apicall;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChiefNearByModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.JSONParsingUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sonu on 12/8/2017.
 */

public class FoodieHomeChiefFavouriteListApiCall extends BaseApiCall {
    private String userId;
    private BaseModel baseModel;
    private ArrayList<HomeChiefNearByModel> chiefDetailList;

    public FoodieHomeChiefFavouriteListApiCall(String userId) {
        this.userId = userId;
    }

    @Override
    public String getServiceURL() {
        System.out.println(Constants.ServiceTAG.URL + Constants.ServerURL.GET_LIST_OF_FAVOURITE_HOME_CHEF);
        return Constants.ServerURL.GET_LIST_OF_FAVOURITE_HOME_CHEF;
    }

    @Override
    public Object getRequest() {
        JSONObject object = new JSONObject();
        try {
            object.put("UserId", userId);

        } catch (Exception e) {
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
                homeChiefNearByModel.setFavourite(detailObj.optInt("IsFavourite") == 1 ? true : false);
                homeChiefNearByModel.setCoverPhotoArrayList(getCoverPhotoArray(detailObj.optString("CoverPhotoShow")));

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

    @Override
    public void parseResponseCode(Object response) throws JSONException {
        if (response instanceof JSONObject) {
            parseData((JSONObject) response);

        }
        super.parseResponseCode(response);
    }

    public BaseModel getBaseModel() {
        return baseModel;
    }

    @Override
    public ArrayList<HomeChiefNearByModel> getResult() {
        return chiefDetailList;
    }
}
