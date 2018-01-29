package com.homemadebazar.util;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.ChatMessageModel;
import com.homemadebazar.model.DishIngredientsModel;
import com.homemadebazar.model.FoodCategoryModel;
import com.homemadebazar.model.FoodieFlashPostModel;
import com.homemadebazar.model.FoodiePostCommentModel;
import com.homemadebazar.model.HomeChefIncomingOrderModel;
import com.homemadebazar.model.HomeChefOrderModel;
import com.homemadebazar.model.HomeChefProfileModel;
import com.homemadebazar.model.HomeChefSkillHubVideoModel;
import com.homemadebazar.model.IsAccountExistModel;
import com.homemadebazar.model.MarketPlaceProductBrandModel;
import com.homemadebazar.model.MarketPlaceProductCategoryModel;
import com.homemadebazar.model.MarketPlaceProductModel;
import com.homemadebazar.model.NotificationModel;
import com.homemadebazar.model.OtherUserProfileDetailsModel;
import com.homemadebazar.model.TransactionModel;
import com.homemadebazar.model.UserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sumit on 27/08/17.
 */

public class JSONParsingUtils {

    {
//        "Band": "kashmiri mirch",
//            "Description": "Test",
//            "Price": "30",
//            "ProductCategory": "Milk",
//            "ProductId": "POID00000001",
//            "ProductName": "Curd",
//        "ProductList":
//        "http://35.183.8.236/api/Profile/GetImage?Source=ImageGallary%5C%5C20170217%5CProduct%5CDSC_1386.JPG"
    }


    public static IsAccountExistModel getAccountExistsModel(JSONObject object) {
        IsAccountExistModel isAccountExistModel = new IsAccountExistModel();
        if (object != null) {
            isAccountExistModel.setStatusCode(Integer.parseInt(object.optString("StatusCode")));
            isAccountExistModel.setStatusMessage(object.optString("StatusMessage"));
            isAccountExistModel.setUserId(object.optString("UserId"));
            isAccountExistModel.setCountryCode(object.optString("CountryCode"));
            isAccountExistModel.setMobile(object.optString("Mobile"));
            isAccountExistModel.setMobileVerified((object.optString("IsMobileVerified").trim()).equals("1") ? true : false);
            isAccountExistModel.setSignUpRequired((object.optString("SignupRequired").trim()).equals("1") ? true : false);
        }
        return isAccountExistModel;
    }

    public static ArrayList<FoodCategoryModel> parseFoodCategoryModel(String response) {
        ArrayList<FoodCategoryModel> foodCategoryModelArrayList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(response);
            JSONArray jsonArray = object.getJSONArray("FoodCategoriesList");
            for (int i = 0; i < jsonArray.length(); i++) {
                FoodCategoryModel foodCategoryModel = new FoodCategoryModel();
                foodCategoryModel.setFoodCategoryId(jsonArray.getJSONObject(i).optString("FoodCatId"));
                foodCategoryModel.setName(jsonArray.getJSONObject(i).optString("Name"));
                foodCategoryModel.setThumbnail(jsonArray.getJSONObject(i).optString("Thumbnail"));
                foodCategoryModelArrayList.add(foodCategoryModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foodCategoryModelArrayList;
    }

    public static BaseModel getOtpModel(JSONObject object) {
        BaseModel baseModel = new BaseModel();
        baseModel.setStatusCode(Integer.parseInt(object.optString("StatusCode")));
        baseModel.setStatusMessage(object.optString("StatusMessage"));
        return baseModel;
    }

    public static BaseModel parseBaseModel(JSONObject object) {
        BaseModel baseModel = new BaseModel();
        baseModel.setStatusCode(Integer.parseInt(object.optString("StatusCode").trim()));
        baseModel.setStatusMessage(object.optString("StatusMessage"));
        return baseModel;
    }

    public static UserModel getUserModel(JSONObject object) {
        UserModel userModel = new UserModel();
        userModel.setStatusCode(Integer.parseInt(object.optString("StatusCode")));
        userModel.setStatusMessage(object.optString("StatusMessage"));
        userModel.setUserId(object.optString("UserId"));
        userModel.setCountryCode(object.optString("CountryCode"));
        userModel.setMobile(object.optString("Mobile"));
        userModel.setFirstName(object.optString("FirstName"));
        userModel.setLastName(object.optString("LastName"));
        userModel.setEmailId(object.optString("EmailId"));
        userModel.setAccountType(object.optString("AccountType"));
        userModel.setProfilePic(object.optString("ProfilePic"));
        return userModel;
    }

    public static HomeChefProfileModel parseHomeChefProfileModel(JSONObject object) {
        HomeChefProfileModel homeChefProfileModel = new HomeChefProfileModel();
        try {
            homeChefProfileModel.setStatusCode(Integer.parseInt(object.optString("StatusCode")));
            homeChefProfileModel.setStatusMessage(object.optString("StatusMessage"));
            homeChefProfileModel.setUserId(object.optString("UserId"));
            homeChefProfileModel.setShopName(object.optString("ShopName"));
            homeChefProfileModel.setPriceRange(object.optString("PriceRange"));
            homeChefProfileModel.setAddress(object.optString("Address1"));
            homeChefProfileModel.setSpeciality(object.optString("Speciality"));
            homeChefProfileModel.setProfilePicture(object.optString("ProfilePicture"));
            JSONArray coverPhotoArray = object.getJSONArray("Url");
            homeChefProfileModel.setCoverPhotoUrl(getCoverPhotoArrayList(coverPhotoArray));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return homeChefProfileModel;
    }

    private static ArrayList<String> getCoverPhotoArrayList(JSONArray jsonArray) {
        ArrayList<String> coverPhotoArray = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                coverPhotoArray.add((String) jsonArray.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coverPhotoArray;
    }

    public static ArrayList<HomeChefOrderModel> parseHomeChefOrderList1(JSONObject object) {
        ArrayList<HomeChefOrderModel> homeChefOrderModelArrayList = new ArrayList<>();
        try {
            JSONArray OrdersArray = object.optJSONArray("Orders");
            for (int i = 0; i < OrdersArray.length(); i++) {
                HomeChefOrderModel homeChefOrderModel = parseHomeChefOrder1(OrdersArray.getJSONObject(i));
                homeChefOrderModelArrayList.add(homeChefOrderModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return homeChefOrderModelArrayList;
    }

    private static HomeChefOrderModel parseHomeChefOrder1(JSONObject object) {
        HomeChefOrderModel homeChefOrderModel = new HomeChefOrderModel();
        try {

            homeChefOrderModel.setOrderId(object.optString("OrderId"));
            homeChefOrderModel.setDishName(object.optString("Dish"));
//            homeChefOrderModel.setCategory(object.optString(""));
            homeChefOrderModel.setPrice(object.optString("Price"));
            homeChefOrderModel.setMinGuest(object.optString("MinGuestNo"));
            homeChefOrderModel.setMaxGuest(object.optString("MaxGuestNo"));
//            homeChefOrderModel.setDiscount(object.optString(""));
//            homeChefOrderModel.setPetsAllowed(object.optBoolean());
//            homeChefOrderModel.setDrinks(object.optString());
//            homeChefOrderModel.setVegNonVeg(object.optString());
            homeChefOrderModel.setRules(object.optString("RuleDescription"));
            homeChefOrderModel.setDescription(object.optString("Description"));
            homeChefOrderModel.setOrderFromDate(object.optString("OrderFromDT"));
            homeChefOrderModel.setOrderValidTill(object.optString("OrderValid"));

            homeChefOrderModel.setDishAvailability(object.optString("DishAvailableDay"));
            homeChefOrderModel.setBreakFastTime(object.optString("Bearkfast"));
            homeChefOrderModel.setLunchTime(object.optString("Lunch"));
            homeChefOrderModel.setDinnerTime(object.optString("DinnerTime"));

            JSONArray array = object.optJSONArray("FoodImage");
            homeChefOrderModel.setFoodImagesArrayList(getCoverPhotoArrayList(array));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return homeChefOrderModel;

    }

    public static ArrayList<HomeChefOrderModel> parseHomeChefOrderList(JSONObject object) {
        ArrayList<HomeChefOrderModel> homeChefOrderModelArrayList = new ArrayList<>();
        try {
            JSONArray OrdersArray = object.optJSONArray("OrderDetails");
            for (int i = 0; i < OrdersArray.length(); i++) {
                HomeChefOrderModel homeChefOrderModel = parseHomeChefOrder(OrdersArray.getJSONObject(i));
                homeChefOrderModel.setUserId(object.optString("UserId"));
                homeChefOrderModel.setFirstName(object.optString("FirstName"));
                homeChefOrderModel.setLastName(object.optString("LastName"));
                homeChefOrderModel.setProfilePic(object.optString("DP"));
                homeChefOrderModelArrayList.add(homeChefOrderModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return homeChefOrderModelArrayList;
    }

    /*
        {
    //        "DishName": "ffg",
    //            "Price": 55,
    //            "MinGuest": 22,
    //            "MaxGuest": 258,
    //            "DishDescription": "xx",
    //            "RuleDescription": "dcn",
    //            "DishAvailable": "13-01-2018,0,0,0;14-01-2018,0,0,0;15-01-2018,0,0,0;16-01-2018,0,0,0;17-01-2018,0,0,0;18-01-2018,0,0,0;19-01-2018,0,0,0;20-01-2018,0,0,0",
                "DinnerTime": "54021",
                "LunchTime": "4521",
                "BreakFastTime": "1720",
    //            "OrderId": "E4C01AAE",
                "Drink": "water",
                "IsPetAllow": "False",
                "VegNonType": "True",
                "CoverImage": [
            "http://18.218.139.27/api/Profile/GetImage?Source=ImageGallary%5C%5C1801132%5CAddCoverPhoto%5CIMG-20180113-WA0000.jpg",
                    "http://18.218.139.27/api/Profile/GetImage?Source=ImageGallary%5C%5C1801132%5CAddCoverPhoto%5CIMG-20180112-WA0009.jpg",
                    "http://18.218.139.27/api/Profile/GetImage?Source=ImageGallary%5C%5C1801132%5CAddCoverPhoto%5CIMG-20180113-WA0000.jpg",
                    "http://18.218.139.27/api/Profile/GetImage?Source=ImageGallary%5C%5C1801132%5CAddCoverPhoto%5CIMG-20180112-WA0009.jpg"
                        ]
        }*/
    private static HomeChefOrderModel parseHomeChefOrder(JSONObject object) {
        HomeChefOrderModel homeChefOrderModel = new HomeChefOrderModel();
        try {

            homeChefOrderModel.setOrderId(object.optString("OrderId"));
            homeChefOrderModel.setDishName(object.optString("DishName"));
//            homeChefOrderModel.setCategory(object.optString(""));
            homeChefOrderModel.setPrice(object.optString("Price"));
            homeChefOrderModel.setMinGuest(object.optString("MinGuest"));
            homeChefOrderModel.setMaxGuest(object.optString("MaxGuest"));
//            homeChefOrderModel.setDiscount(object.optString(""));
//            homeChefOrderModel.setPetsAllowed(object.optBoolean());
//            homeChefOrderModel.setDrinks(object.optString());
//            homeChefOrderModel.setVegNonVeg(object.optString());

            homeChefOrderModel.setOrderType(object.optString("OrderType"));
            homeChefOrderModel.setRules(object.optString("RuleDescription"));
            homeChefOrderModel.setDescription(object.optString("DishDescription"));
//            homeChefOrderModel.setOrderFromDate(object.optString("OrderFromDT"));
//            homeChefOrderModel.setOrderValidTill(object.optString("OrderValid"));

            homeChefOrderModel.setDishAvailability(object.optString("DishAvailable"));
//            homeChefOrderModel.setBreakFastTime(object.optString("Bearkfast"));
//            homeChefOrderModel.setLunchTime(object.optString("Lunch"));
//            homeChefOrderModel.setDinnerTime(object.optString("DinnerTime"));

            homeChefOrderModel.setOrderTime(object.optString("OrderTime"));

            JSONArray array = object.optJSONArray("FoodImage");
            homeChefOrderModel.setFoodImagesArrayList(getCoverPhotoArrayList(array));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return homeChefOrderModel;

    }

    public static ArrayList<HomeChefOrderModel> parseHotDealList(JSONObject object) {
        ArrayList<HomeChefOrderModel> homeChefOrderModelArrayList = new ArrayList<>();
        try {
            JSONArray OrdersArray = object.optJSONArray("Details");
            for (int i = 0; i < OrdersArray.length(); i++) {
                HomeChefOrderModel homeChefOrderModel = parseHotDealModel(OrdersArray.getJSONObject(i));
                homeChefOrderModel.setUserId(object.optString("UserId"));
                homeChefOrderModel.setFirstName(object.optString("FirstName"));
                homeChefOrderModel.setLastName(object.optString("LastName"));
                homeChefOrderModel.setProfilePic(object.optString("DP"));
                homeChefOrderModelArrayList.add(homeChefOrderModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return homeChefOrderModelArrayList;
    }


    private static HomeChefOrderModel parseHotDealModel(JSONObject object) {
        HomeChefOrderModel homeChefOrderModel = new HomeChefOrderModel();
        try {

            homeChefOrderModel.setOrderId(object.optString("OrderId"));
            homeChefOrderModel.setDishName(object.optString("DishName"));
//            homeChefOrderModel.setCategory(object.optString(""));
            homeChefOrderModel.setPrice(object.optString("Price"));
            homeChefOrderModel.setMinGuest(object.optString("MinGuest"));
            homeChefOrderModel.setMaxGuest(object.optString("MaxGuest"));
//            homeChefOrderModel.setDiscount(object.optString(""));
//            homeChefOrderModel.setPetsAllowed(object.optBoolean());
//            homeChefOrderModel.setDrinks(object.optString());
//            homeChefOrderModel.setVegNonVeg(object.optString());

            homeChefOrderModel.setOrderType(object.optString("OrderType"));
            homeChefOrderModel.setRules(object.optString("RuleDescription"));
            homeChefOrderModel.setDescription(object.optString("DishDescription"));
//            homeChefOrderModel.setOrderFromDate(object.optString("OrderFromDT"));
//            homeChefOrderModel.setOrderValidTill(object.optString("OrderValid"));

            homeChefOrderModel.setDishAvailability(object.optString("DishAvailable"));
//            homeChefOrderModel.setBreakFastTime(object.optString("Bearkfast"));
//            homeChefOrderModel.setLunchTime(object.optString("Lunch"));
//            homeChefOrderModel.setDinnerTime(object.optString("DinnerTime"));

//            homeChefOrderModel.setOrderTime(object.optString("OrderTime"));

            JSONArray array = object.getJSONArray("FoodImage");
            homeChefOrderModel.setFoodImagesArrayList(getCoverPhotoArrayList(array));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return homeChefOrderModel;

    }

    private static HomeChefSkillHubVideoModel parseSkillHubVideoModel(JSONObject object) {
        HomeChefSkillHubVideoModel homeChefSkillHubVideoModel = new HomeChefSkillHubVideoModel();
        try {
            homeChefSkillHubVideoModel.setId(object.optString("Id"));
            homeChefSkillHubVideoModel.setTitle(object.optString("Title"));
            homeChefSkillHubVideoModel.setDescription(object.optString("Description"));
            homeChefSkillHubVideoModel.setThumbNailUrl(object.optString("ThumbnailUrl"));
            homeChefSkillHubVideoModel.setYoutubeUrl(object.optString("YoutubeUrl"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return homeChefSkillHubVideoModel;
    }

    public static ArrayList<HomeChefSkillHubVideoModel> parseSkillHubVideoList(JSONObject object) {
        ArrayList<HomeChefSkillHubVideoModel> homeChefSkillHubVideoModelArrayList = new ArrayList<>();
        try {
            JSONArray array = object.optJSONArray("SkillHub");
            for (int i = 0; i < array.length(); i++) {
                HomeChefSkillHubVideoModel homeChefSkillHubVideoModel = parseSkillHubVideoModel(array.getJSONObject(i));
                homeChefSkillHubVideoModelArrayList.add(homeChefSkillHubVideoModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return homeChefSkillHubVideoModelArrayList;
    }

    public static ArrayList<FoodieFlashPostModel> parseFoodieFlashPostsList(JSONObject object) {
        ArrayList<FoodieFlashPostModel> foodieFlashPostModelArrayList = new ArrayList<>();
        try {
            JSONArray postsArray = object.optJSONArray("Posts");
            for (int i = 0; i < postsArray.length(); i++) {
                FoodieFlashPostModel foodieFlashPostModel = parseFoodieFlashPost(postsArray.getJSONObject(i));
                foodieFlashPostModelArrayList.add(foodieFlashPostModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return foodieFlashPostModelArrayList;
    }

    private static FoodieFlashPostModel parseFoodieFlashPost(JSONObject object) {
        FoodieFlashPostModel foodieFlashPostModel = new FoodieFlashPostModel();
        try {
            foodieFlashPostModel.setNoOfComments(Utils.parseInteger(object.optString("nCommentsCount")));
            foodieFlashPostModel.setNoOfLikes(Utils.parseInteger(object.optString("nLikesCount")));
            foodieFlashPostModel.setPostDateTime(object.optString("PostDateTime"));
            foodieFlashPostModel.setPostId(object.optString("PostId"));
            foodieFlashPostModel.setPostMessage(object.optString("PostMessage"));
            foodieFlashPostModel.setPostRatings(object.optString("PostRating"));
            foodieFlashPostModel.setPostImageUrl(object.optString("PostImageUrl"));
            foodieFlashPostModel.setLike(object.optBoolean("IsLike"));

            foodieFlashPostModel.setUserProfileUrl(object.optString("PosterDPUrl"));
            foodieFlashPostModel.setUserFirstName(object.optString("FirstName"));
            foodieFlashPostModel.setUserLastName(object.optString("LastName"));
            foodieFlashPostModel.setUserId(object.optString("UserId"));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return foodieFlashPostModel;

    }

    public static ArrayList<DishIngredientsModel>
    parseMarketPlaceIngredientsList(JSONObject object) {
        ArrayList<DishIngredientsModel> dishIngredientsModelArrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = object.optJSONArray("SearchItems");
            for (int i = 0; i < jsonArray.length(); i++) {
                DishIngredientsModel dishIngredientsModel = new DishIngredientsModel();
                dishIngredientsModel.setDishName(jsonArray.getJSONObject(i).optString("DishName"));
                dishIngredientsModel.setDescDesc(jsonArray.getJSONObject(i).optString("DishDesc"));
                dishIngredientsModel.setCaloriesPerServing(jsonArray.getJSONObject(i).optString("PreparationTime"));
                dishIngredientsModel.setMarketPlaceProductModelArrayList(getProducts(jsonArray.getJSONObject(i).optJSONArray("Ingrd")));
                dishIngredientsModelArrayList.add(dishIngredientsModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dishIngredientsModelArrayList;
    }

    private static ArrayList<MarketPlaceProductModel> getProducts(JSONArray jsonArray) {
        ArrayList<MarketPlaceProductModel> marketPlaceProductModels = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            MarketPlaceProductModel marketPlaceProductModel = new MarketPlaceProductModel();
            marketPlaceProductModel.setProductName(jsonArray.optJSONObject(i).optString("ProductName"));
            marketPlaceProductModel.setProductName(jsonArray.optJSONObject(i).optString("Ingrd"));
            marketPlaceProductModel.setCategory(jsonArray.optJSONObject(i).optString("ProductCategory"));
            marketPlaceProductModel.setBrand(jsonArray.optJSONObject(i).optString("Brand"));
            marketPlaceProductModel.setPrice(jsonArray.optJSONObject(i).optString("Price"));
            marketPlaceProductModel.setDescription(jsonArray.optJSONObject(i).optString("Description"));
            marketPlaceProductModel.setImageUrl(jsonArray.optJSONObject(i).optString("ProductImage"));
            marketPlaceProductModels.add(marketPlaceProductModel);
        }
        return marketPlaceProductModels;
    }

    public static ArrayList<HomeChefIncomingOrderModel> parseHomeChefIncomingOrder(JSONArray jsonArray, int tab) {
        ArrayList<HomeChefIncomingOrderModel> homeChefIncomingOrderModelArrayList = new ArrayList<>();


        try {
            String lastDate = "";
            if (tab == 1) {
                HomeChefIncomingOrderModel homeChefIncomingOrderModel = new HomeChefIncomingOrderModel();
                lastDate = jsonArray.getJSONObject(0).optString("BookedDate");

                homeChefIncomingOrderModel.setType(1);
                homeChefIncomingOrderModel.setDateTitle(jsonArray.getJSONObject(0).optString("BookedDate"));
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                if (tab == 1) {
                    if (!lastDate.equalsIgnoreCase(object.optString("BookedDate"))) {
                        HomeChefIncomingOrderModel homeChefIncomingOrderModel = new HomeChefIncomingOrderModel();
                        lastDate = object.optString("BookedDate");

                        homeChefIncomingOrderModel.setType(1);
                        homeChefIncomingOrderModel.setDateTitle(object.optString("BookedDate"));
                    }
                }


                HomeChefIncomingOrderModel homeChefIncomingOrderModel = new HomeChefIncomingOrderModel();
                homeChefIncomingOrderModel.setFoodiesDp(object.optString("FoodiesDP"));
                homeChefIncomingOrderModel.setCoverPhoto(object.optString("CoverPhoto"));
                homeChefIncomingOrderModel.setFoodiesFirstName(object.optString("FoodiesFirstName"));
                homeChefIncomingOrderModel.setFoodiesLastName(object.optString("FoodiesLastName"));
                homeChefIncomingOrderModel.setFoodiesUserId(object.optString("FoodiesUserId"));
                homeChefIncomingOrderModel.setOrderFor(object.optString("OrderFor"));
                homeChefIncomingOrderModel.setOrderId(object.optString("OrderId"));
                homeChefIncomingOrderModel.setOrderReqDate(object.optString("OrderReqDT"));
                homeChefIncomingOrderModel.setBookedDate(object.optString("BookedDate"));
                homeChefIncomingOrderModel.setOrderRequestId(object.optString("OrderReqId"));
                homeChefIncomingOrderModel.setRequestStatus(object.optString("ReqStatus"));
                homeChefIncomingOrderModel.setDishName(object.optString("DishName"));
                homeChefIncomingOrderModel.setEatingTime(object.optString("EattingTime"));
                homeChefIncomingOrderModel.setOrderType(object.optString("OrderType"));
                homeChefIncomingOrderModelArrayList.add(homeChefIncomingOrderModel);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return homeChefIncomingOrderModelArrayList;

    }

//    "ImageUrl":[

    //    {"ProductCategoryList":[{"ProCatId":"PCID00000006","Name":"Category1","Description":"Categorhh"}]}
    public static ArrayList<MarketPlaceProductCategoryModel> parseProductCategoryModel(JSONObject object) {
        ArrayList<MarketPlaceProductCategoryModel> marketPlaceProductCategoryModelArrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = object.optJSONArray("ProductCategoryList");
            for (int i = 0; i < jsonArray.length(); i++) {
                MarketPlaceProductCategoryModel marketPlaceProductCategoryModel = new MarketPlaceProductCategoryModel();
                marketPlaceProductCategoryModel.setProductCategoryId(jsonArray.getJSONObject(i).optString("Id"));
                marketPlaceProductCategoryModel.setName(jsonArray.getJSONObject(i).optString("Name"));
                marketPlaceProductCategoryModel.setDescription(jsonArray.getJSONObject(i).optString("Description"));
                marketPlaceProductCategoryModel.setCategoryUrl(jsonArray.getJSONObject(i).optString("CategoryUrl"));

                marketPlaceProductCategoryModelArrayList.add(marketPlaceProductCategoryModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return marketPlaceProductCategoryModelArrayList;
    }

    //    {"StatusCode":"100","StatusMessage":"Success",
//            "ProductBrandList":[{"Description":"Rich wine red color, shriveled appearance and not so spicy, that's the definition of kashmiri chilli.","Name":"kashmiri mirch","Id":"PBID00000001"}]}
    public static ArrayList<MarketPlaceProductBrandModel> parseProductBrandModel(JSONObject object) {
        ArrayList<MarketPlaceProductBrandModel> marketPlaceProductBrandModelArrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = object.optJSONArray("ProductBrandList");
            for (int i = 0; i < jsonArray.length(); i++) {
                MarketPlaceProductBrandModel marketPlaceProductBrandModel = new MarketPlaceProductBrandModel();
                marketPlaceProductBrandModel.setBrandName(jsonArray.getJSONObject(i).optString("Name"));
                marketPlaceProductBrandModel.setBrandDesc(jsonArray.getJSONObject(i).optString("Description"));
                marketPlaceProductBrandModel.setBrandId(jsonArray.getJSONObject(i).optString("Id"));
                marketPlaceProductBrandModelArrayList.add(marketPlaceProductBrandModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return marketPlaceProductBrandModelArrayList;
    }

    public static ArrayList<MarketPlaceProductModel> parseProductModelList(JSONObject object) {
        ArrayList<MarketPlaceProductModel> marketPlaceProductModelArrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = object.optJSONArray("ImageUrl");
            for (int i = 0; i < jsonArray.length(); i++) {
                MarketPlaceProductModel marketPlaceProductModel = parseMarketPlaceProductModel(jsonArray.getJSONObject(i));
                marketPlaceProductModelArrayList.add(marketPlaceProductModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return marketPlaceProductModelArrayList;
    }

    private static MarketPlaceProductModel parseMarketPlaceProductModel(JSONObject object) {
        MarketPlaceProductModel marketPlaceProductModel = new MarketPlaceProductModel();
        try {
            marketPlaceProductModel.setProductName(object.optString("ProductName"));
            marketPlaceProductModel.setProductId(object.optString("ProductId"));
            marketPlaceProductModel.setCategory(object.optString("ProductCategory"));
            marketPlaceProductModel.setPrice(object.optString("Price"));
            marketPlaceProductModel.setDescription(object.optString("Description"));
            marketPlaceProductModel.setBrand(object.optString("Band"));
            marketPlaceProductModel.setImageUrl(object.optString("ProductList"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return marketPlaceProductModel;
    }

    public static ArrayList<ChatMessageModel> parseChatModelList(JSONObject object) {
        ArrayList<ChatMessageModel> chatMessageModelArrayList = new ArrayList<>();
        try {
            JSONArray messageArray = object.optJSONArray("Message");
            for (int i = 0; i < messageArray.length(); i++) {
                ChatMessageModel chatMessageModel = parseChatModel(messageArray.getJSONObject(i));
                chatMessageModelArrayList.add(chatMessageModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return chatMessageModelArrayList;
    }

    public static ArrayList<UserModel> parseUserFriendsAndRequests(JSONObject object) {
        ArrayList<UserModel> userModelArrayList = new ArrayList<>();
        try {
            JSONArray reqObj = object.optJSONArray("RequestObjects");
            for (int i = 0; i < reqObj.length(); i++) {
                JSONObject obj = reqObj.optJSONObject(i);
                UserModel userModel = new UserModel();
                userModel.setEmailId(obj.optString("EmailId"));
                userModel.setFirstName(obj.optString("FName"));
                userModel.setLastName(obj.optString("LName"));
                userModel.setUserId(obj.optString("UserId"));
                userModel.setProfilePic(obj.optString("DP"));
                userModelArrayList.add(userModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userModelArrayList;
    }

    private static ChatMessageModel parseChatModel(JSONObject object) {
        ChatMessageModel chatMessageModel = new ChatMessageModel();
        try {
            chatMessageModel.setChatId(object.optString("ChatId"));
            chatMessageModel.setSenderId(object.optString("SenderId"));
            chatMessageModel.setReceiverId(object.optString("ReceiverId"));
            chatMessageModel.setFileType(object.optString("FileType"));
            chatMessageModel.setMessageType(object.optString("MessageType"));
            chatMessageModel.setChatMessage(object.optString("ChatText"));
            chatMessageModel.setLatitude(object.optString("Latitude"));
            chatMessageModel.setLongitude(object.optString("Longtitude"));
            chatMessageModel.setReadStatus(object.optString("ReadStatus"));
            chatMessageModel.setSentTime(object.optString("SentTime"));
            chatMessageModel.setShareFile(object.optString("ShareFile"));
            chatMessageModel.setTimeStamp(object.optString("ChatTimeStamp"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return chatMessageModel;
    }

    public static ArrayList<FoodiePostCommentModel> parseFoodiePostCommentList(JSONObject object) {
        ArrayList<FoodiePostCommentModel> foodiePostCommentModelArrayList = new ArrayList<>();
        try {
            JSONArray commentsArray = object.optJSONArray("Posts");
            for (int i = 0; i < commentsArray.length(); i++) {
                FoodiePostCommentModel foodiePostCommentModel = parseFoodiePostCommentModel(commentsArray.getJSONObject(i));
                foodiePostCommentModelArrayList.add(foodiePostCommentModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foodiePostCommentModelArrayList;
    }

    private static FoodiePostCommentModel parseFoodiePostCommentModel(JSONObject object) {
        FoodiePostCommentModel foodiePostCommentModel = new FoodiePostCommentModel();
        try {
            foodiePostCommentModel.setUserId(object.optString("UserId"));
            foodiePostCommentModel.setFirstName(object.optString("FirstName"));
            foodiePostCommentModel.setLastName(object.optString("LastName"));
            foodiePostCommentModel.setComments(object.optString("Comments"));
            foodiePostCommentModel.setUserProfile(object.optString("PosterDPUrl"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return foodiePostCommentModel;
    }

    public static ArrayList<NotificationModel> parseNotificationList(JSONObject object) {
        ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
        try {
            JSONArray notificationArray = object.optJSONArray("Notification");
            for (int i = 0; i < notificationArray.length(); i++) {
                NotificationModel notificationModel = parseNotificationModel(notificationArray.getJSONObject(i));
                notificationModelArrayList.add(notificationModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notificationModelArrayList;
    }

    public static NotificationModel parseNotificationModel(JSONObject object) {
        NotificationModel notificationModel = new NotificationModel();
        try {
            notificationModel.setNotificationType(object.optString("NotificationType"));
            notificationModel.setTitle(object.optString("Title"));
            notificationModel.setMessage(object.optString("Message"));
            notificationModel.setSenderUserId(object.optString("SenderUserId"));
            notificationModel.setSenderName(object.optString("SenderName"));
            notificationModel.setSenderDp(object.optString("SenderDp"));
            notificationModel.setReceiverUserId(object.optString("ReceiverUserId"));
            notificationModel.setReceiverName(object.optString("ReceiverName"));
            notificationModel.setReceiverDp(object.optString("ReceiverDp"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notificationModel;
    }

    public static ArrayList<TransactionModel> parseTransactionReportList(JSONObject object) {
        ArrayList<TransactionModel> transactionModelArrayList = new ArrayList<>();
        try {
            JSONArray reportsArray = object.optJSONArray("Reports");
            for (int i = 0; i < reportsArray.length(); i++) {
                TransactionModel transactionModel = parseTransactionModel(reportsArray.getJSONObject(i));
                transactionModelArrayList.add(transactionModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactionModelArrayList;
    }

    public static TransactionModel parseTransactionModel(JSONObject object) {
        TransactionModel transactionModel = new TransactionModel();
        try {
//            transactionModel.setTitle(object.optString(""));
//            transactionModel.setTransactionId(object.optString(""));
            transactionModel.setTransactionAmount(object.optString("Amount"));
            transactionModel.setTransactionMode(object.optString("TrnMode"));
            transactionModel.setDescription(object.optString("Remarks"));
            transactionModel.setName(object.optString("Name"));
            transactionModel.setDateTime(object.optString("DateTime"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactionModel;
    }


    public static OtherUserProfileDetailsModel parseOtherUserModel(JSONObject object) {
        OtherUserProfileDetailsModel userModel = new OtherUserProfileDetailsModel();
        userModel.setUserId(object.optString("UserId"));
        userModel.setCountryCode(object.optString("CountryCode"));
        userModel.setCountryName(object.optString("CountryName"));
        userModel.setMobile(object.optString("Mobile"));
        userModel.setFirstName(object.optString("Fname"));
        userModel.setLastName(object.optString("Lname"));
        userModel.setEmailId(object.optString("EmailId"));
        userModel.setAccountType(object.optString("AccountType"));
        userModel.setProfilePic(object.optString("Url"));
        userModel.setAddress(object.optString("Address"));
        userModel.setCompanyName(object.optString("CompanyName"));
        userModel.setDpStatus(object.optString("DPStatus"));
        userModel.setIsMobileVerified(object.optString("IsMobileVarified"));
        userModel.setLatitude(object.optString("Latitude"));
        userModel.setLongitude(object.optString("Longitude"));
        userModel.setPinCode(object.optString("Pincode"));
        userModel.setUniversityName(object.optString("UniversityName"));
        userModel.setFriendRequestStatus(object.optString("FrndRequestStatus"));
        return userModel;
    }


}