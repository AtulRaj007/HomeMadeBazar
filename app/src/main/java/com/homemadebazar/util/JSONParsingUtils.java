package com.homemadebazar.util;


import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.ChatMessageModel;
import com.homemadebazar.model.DishIngredientsModel;
import com.homemadebazar.model.FoodCategoryModel;
import com.homemadebazar.model.FoodieCheckInModel;
import com.homemadebazar.model.FoodieFlashPostModel;
import com.homemadebazar.model.FoodiePostCommentModel;
import com.homemadebazar.model.HomeChefIncomingOrderModel;
import com.homemadebazar.model.HomeChefOrderModel;
import com.homemadebazar.model.HomeChefProfileModel;
import com.homemadebazar.model.HomeChefSkillHubVideoModel;
import com.homemadebazar.model.IsAccountExistModel;
import com.homemadebazar.model.MarketPlaceMyOrdersModel;
import com.homemadebazar.model.MarketPlaceOrderModel;
import com.homemadebazar.model.MarketPlaceOrderProductModel;
import com.homemadebazar.model.MarketPlaceProductBrandModel;
import com.homemadebazar.model.MarketPlaceProductCategoryModel;
import com.homemadebazar.model.MarketPlaceProductModel;
import com.homemadebazar.model.NotificationModel;
import com.homemadebazar.model.OtherUserProfileDetailsModel;
import com.homemadebazar.model.RatingModel;
import com.homemadebazar.model.TransactionModel;
import com.homemadebazar.model.UserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sumit on 27/08/17.
 */

public class JSONParsingUtils {

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
        userModel.setCountryName(object.optString("CountryName"));
        userModel.setInterest(object.optString("Interests"));
        userModel.setProfilePic(object.optString("Url"));
        userModel.setProfessionName(object.optString("ProfessionName"));
        userModel.setProfessionType(object.optString("ProfessionType"));
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
            homeChefOrderModel.setPrice(object.optString("Price"));
            homeChefOrderModel.setMinGuest(object.optString("MinGuestNo"));
            homeChefOrderModel.setMaxGuest(object.optString("MaxGuestNo"));
            homeChefOrderModel.setRules(object.optString("RuleDescription"));
            homeChefOrderModel.setDescription(object.optString("Description"));
            homeChefOrderModel.setOrderFromDate(object.optString("OrderFromDT"));
            homeChefOrderModel.setOrderValidTill(object.optString("OrderValid"));
            homeChefOrderModel.setDiscount(object.optString("Discount"));

            homeChefOrderModel.setDishAvailability(object.optString("DishAvailableDay"));
            homeChefOrderModel.setFoodDateTimeBookModels(Utils.parseFoodBookDateTime(object.optString("DishAvailableDay")));

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


    private static HomeChefOrderModel parseHomeChefOrder(JSONObject object) {
        HomeChefOrderModel homeChefOrderModel = new HomeChefOrderModel();
        try {

            homeChefOrderModel.setOrderId(object.optString("OrderId"));
            homeChefOrderModel.setDishName(object.optString("DishName"));
            homeChefOrderModel.setCategory(object.optString("FoodCategories"));
            homeChefOrderModel.setPrice(object.optString("Price"));
            homeChefOrderModel.setMinGuest(object.optString("MinGuest"));
            homeChefOrderModel.setMaxGuest(object.optString("MaxGuest"));
            homeChefOrderModel.setDiscount(object.optString("DiscountAmount"));
            if (object.optString("IsPetAllow").equalsIgnoreCase("Allow"))
                homeChefOrderModel.setPetsAllowed(true);
            else
                homeChefOrderModel.setPetsAllowed(false);

            homeChefOrderModel.setDrinks(object.optString("Drink"));
            homeChefOrderModel.setVegNonVeg(object.optString("VegNonType"));
            homeChefOrderModel.setOrderType(object.optString("OrderType"));
            homeChefOrderModel.setRules(object.optString("RuleDescription"));
            homeChefOrderModel.setDescription(object.optString("DishDescription"));


            homeChefOrderModel.setDishAvailability(object.optString("DishAvailable"));

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
            JSONArray detailsArray = object.optJSONArray("Details");
            for (int i = 0; i < detailsArray.length(); i++) {
                JSONObject hotDealsObject = detailsArray.getJSONObject(i);
                String userId = hotDealsObject.optString("UserId");
                String firstName = hotDealsObject.optString("FirstName");
                String lastName = hotDealsObject.optString("LastName");
                String profilePic = hotDealsObject.optString("DP");
                String mobile = hotDealsObject.optString("Mobile");
                String email = hotDealsObject.optString("Email");

                JSONArray orderArray = hotDealsObject.optJSONArray("OrderDetails");
                for (int j = 0; j < orderArray.length(); j++) {
                    HomeChefOrderModel homeChefOrderModel = parseHotDealModel(orderArray.getJSONObject(i));
                    homeChefOrderModel.setUserId(userId);
                    homeChefOrderModel.setFirstName(firstName);
                    homeChefOrderModel.setLastName(lastName);
                    homeChefOrderModel.setProfilePic(profilePic);
                    homeChefOrderModelArrayList.add(homeChefOrderModel);
                }
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
            homeChefOrderModel.setDiscount(object.optString("Discount"));
            homeChefOrderModel.setPetsAllowed(object.optBoolean("IsPetAllow", false));
            homeChefOrderModel.setDrinks(object.optString("Drink"));
            homeChefOrderModel.setVegNonVeg(object.optString("VegNonType"));

            homeChefOrderModel.setOrderType(object.optString("OrderType"));
            homeChefOrderModel.setRules(object.optString("RuleDescription"));
            homeChefOrderModel.setDescription(object.optString("DishDescription"));
//            homeChefOrderModel.setOrderFromDate(object.optString("OrderFromDT"));
//            homeChefOrderModel.setOrderValidTill(object.optString("OrderValid"));

            homeChefOrderModel.setDishAvailability(object.optString("DishAvailable"));
            homeChefOrderModel.setBreakFastTime(object.optString("BreakFastTime"));
            homeChefOrderModel.setLunchTime(object.optString("LunchTime"));
            homeChefOrderModel.setDinnerTime(object.optString("DinnerTime"));

            homeChefOrderModel.setFoodDateTimeBookModels(Utils.parseFoodBookDateTime(object.optString("DishAvailable")));
//            homeChefOrderModel.setOrderTime(object.optString("OrderTime"));

            JSONArray array = object.getJSONArray("CoverImage");
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
            homeChefSkillHubVideoModel.setCategoryId(object.optString("VCategoryId"));
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
                JSONObject obj = array.getJSONObject(i);
                HomeChefSkillHubVideoModel titleVideoModel = new HomeChefSkillHubVideoModel();
                titleVideoModel.setViewType(Constants.SkillHubViewType.TITLE);
                String categoryName = obj.getString("VCategory");
                titleVideoModel.setCategoryName(categoryName);
                homeChefSkillHubVideoModelArrayList.add(titleVideoModel);
                JSONArray listArray = obj.optJSONArray("List");
                for (int j = 0; j < listArray.length(); j++) {
                    HomeChefSkillHubVideoModel homeChefSkillHubVideoModel = parseSkillHubVideoModel(listArray.getJSONObject(j));
                    homeChefSkillHubVideoModel.setViewType(Constants.SkillHubViewType.VIDEO);
                    homeChefSkillHubVideoModel.setCategoryName(categoryName);
                    homeChefSkillHubVideoModelArrayList.add(homeChefSkillHubVideoModel);
                }
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
                homeChefIncomingOrderModel.setFoodieEmailId(object.optString("FoodiesEmail"));
                homeChefIncomingOrderModel.setFoodieMobileNumber(object.optString("FoodiesMobile"));
                homeChefIncomingOrderModel.setNoOfGuest(object.optString("NosOfPerson"));
                homeChefIncomingOrderModel.setPrice(object.optString("Price"));
                homeChefIncomingOrderModel.setDiscAmount(object.optString("DiscAmount"));
                homeChefIncomingOrderModel.setOtp(object.optString("Otp"));

                homeChefIncomingOrderModel.setFoodieProfession(object.optString("FoodiesProfession"));
                homeChefIncomingOrderModel.setFoodieLatitude(object.optString("FoodiesLatitude"));
                homeChefIncomingOrderModel.setFoodieLongitude(object.optString("FoodiesLongtitude"));

                homeChefIncomingOrderModel.setHcUserId(object.optString("HCUserId"));
                homeChefIncomingOrderModel.setHcDp(object.optString("HCDP"));
                homeChefIncomingOrderModel.setHcEmail(object.optString("HCEmail"));
                homeChefIncomingOrderModel.setHcMobileNumber(object.optString("HCMobile"));
                homeChefIncomingOrderModel.setHcFirstName(object.optString("HCFirstName"));
                homeChefIncomingOrderModel.setHcLastName(object.optString("HCLastName"));
                homeChefIncomingOrderModel.setHcLatitude(object.optString("HCLatitude"));
                homeChefIncomingOrderModel.setHcLongitude(object.optString("HCLongtitude"));
                homeChefIncomingOrderModel.setHcProfession(object.optString("HCProfession"));

                homeChefIncomingOrderModelArrayList.add(homeChefIncomingOrderModel);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return homeChefIncomingOrderModelArrayList;

    }


//    {
//            "EmailId":"developer.atulraj@gmail.com","Mobile":"8709646364","NosOfPerson":2,"Price":"40","DiscAmount":5,"Otp":""}

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

    public static ArrayList<MarketPlaceProductModel> parseMarketPlaceProductModelList(JSONObject object) {
        ArrayList<MarketPlaceProductModel> marketPlaceProductModelArrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = object.optJSONArray("SearchItems");
            for (int i = 0; i < jsonArray.length(); i++) {
                MarketPlaceProductModel marketPlaceProductModel = parseMarketPlaceSearchProductModel(jsonArray.getJSONObject(i));
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

    private static MarketPlaceProductModel parseMarketPlaceSearchProductModel(JSONObject object) {
        MarketPlaceProductModel marketPlaceProductModel = new MarketPlaceProductModel();
        try {
            marketPlaceProductModel.setProductName(object.optString("ProductName"));
            marketPlaceProductModel.setProductId(object.optString("ProductId"));
            marketPlaceProductModel.setCategory(object.optString("ProductCategory"));
            marketPlaceProductModel.setPrice(object.optString("Price"));
            marketPlaceProductModel.setDescription(object.optString("Description"));
            marketPlaceProductModel.setBrand(object.optString("Brand"));
            marketPlaceProductModel.setImageUrl(object.optString("ProductUrl"));
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
            foodiePostCommentModel.setSentTime(object.optString("Datetime"));

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

            transactionModel.setTransactionAmount(object.optString("Amount"));
            transactionModel.setTransactionMode(object.optString("TrnMode"));
            transactionModel.setDescription(object.optString("Remarks"));
            transactionModel.setName(object.optString("Name"));
            transactionModel.setDateTime(object.optString("DateTime"));
            transactionModel.setTransactionId(object.optString("TransactionId"));

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
        userModel.setDpStatus(object.optString("DPStatus"));
        userModel.setIsMobileVerified(object.optString("IsMobileVarified"));
        userModel.setLatitude(object.optString("Latitude"));
        userModel.setLongitude(object.optString("Longitude"));
        userModel.setPinCode(object.optString("Pincode"));
        userModel.setFriendRequestStatus(object.optString("FrndRequestStatus"));
        userModel.setInterest(object.optString("Interest"));
        userModel.setProfessionType(object.optString("ProfessionType"));
        userModel.setProfessionName(object.optString("ProfessionName"));
        userModel.setRating(object.optString("Rating"));
        userModel.setRatingModelArrayList(parseRatingList(object.optJSONArray("Feedback")));
        return userModel;
    }

    private static ArrayList<RatingModel> parseRatingList(JSONArray ratingArray) {
        ArrayList<RatingModel> ratingArrayList = new ArrayList<>();
        try {
            if (ratingArray != null) {
                for (int i = 0; i < ratingArray.length(); i++) {
                    JSONObject object = ratingArray.optJSONObject(i);
                    RatingModel ratingModel = new RatingModel();
                    ratingModel.setFirstName(object.optString("FName"));
                    ratingModel.setLastName(object.optString("LName"));
                    ratingModel.setRating(object.optString("Rating"));
                    ratingModel.setUserId(object.optString("FeedBackFromUserId"));
                    ratingModel.setDpUrl(object.optString("DPUrl"));
                    ratingModel.setDescription(object.optString("Feedback"));
                    ratingArrayList.add(ratingModel);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ratingArrayList;
    }

    public static ArrayList<FoodieCheckInModel> parseFoodieCheckInList(JSONObject object) {
        ArrayList<FoodieCheckInModel> foodieCheckInModelArrayList = new ArrayList<>();
        try {
            JSONArray checkInArray = object.optJSONArray("CheckIn");
            for (int i = 0; i < checkInArray.length(); i++) {
                FoodieCheckInModel checkInModel = parseFoodieCheckInModel(checkInArray.getJSONObject(i));
                foodieCheckInModelArrayList.add(checkInModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return foodieCheckInModelArrayList;
    }

    private static FoodieCheckInModel parseFoodieCheckInModel(JSONObject object) {
        FoodieCheckInModel foodieCheckInModel = new FoodieCheckInModel();
        try {
            foodieCheckInModel.setUserId(object.optString("UserId", ""));
            foodieCheckInModel.setFirstName(object.optString("FName", ""));
            foodieCheckInModel.setLastName(object.optString("LName", ""));
            foodieCheckInModel.setShopName(object.optString("ShopName", ""));
            foodieCheckInModel.setAddress(object.optString("Address", ""));
            foodieCheckInModel.setImageUrl(object.optString("ImageUrl", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foodieCheckInModel;
    }

    public static ArrayList<MarketPlaceOrderModel> parseMarketPlaceOrders(JSONArray jsonArray) {
        ArrayList<MarketPlaceOrderModel> marketPlaceProductModelArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                MarketPlaceOrderModel marketPlaceOrderModel = new MarketPlaceOrderModel();
                marketPlaceOrderModel.setOrderId(jsonArray.getJSONObject(i).optString("OrderId"));
                marketPlaceOrderModel.setUserId(jsonArray.getJSONObject(i).optString("UserId"));
                marketPlaceOrderModel.setName(jsonArray.getJSONObject(i).optString("Name"));
                marketPlaceOrderModel.setCountryCode(jsonArray.getJSONObject(i).optString("CountryCode"));
                marketPlaceOrderModel.setCountryName(jsonArray.getJSONObject(i).optString("CountryName"));
                marketPlaceOrderModel.setMobileNumber(jsonArray.getJSONObject(i).optString("Mobile"));
                marketPlaceOrderModel.setEmail(jsonArray.getJSONObject(i).optString("Email"));
                marketPlaceOrderModel.setLatitude(jsonArray.getJSONObject(i).optString("Latitude"));
                marketPlaceOrderModel.setLongitude(jsonArray.getJSONObject(i).optString("Longitude"));
                marketPlaceOrderModel.setAddress(jsonArray.getJSONObject(i).optString("Address"));
                marketPlaceOrderModel.setProfileImage(jsonArray.getJSONObject(i).optString("UserDPUrl"));
                marketPlaceOrderModel.setPinCode(jsonArray.getJSONObject(i).optString("PinCode"));
                marketPlaceOrderModel.setMarketPlaceOrderProductModelArrayList(parseOrderProductModel(jsonArray.getJSONObject(i).getJSONArray("ProductDetails")));
                marketPlaceProductModelArrayList.add(marketPlaceOrderModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return marketPlaceProductModelArrayList;
    }

    private static ArrayList<MarketPlaceOrderProductModel> parseOrderProductModel(JSONArray jsonArray) {
        ArrayList<MarketPlaceOrderProductModel> marketPlaceOrderProductModels = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                MarketPlaceOrderProductModel marketPlaceOrderProductModel = new MarketPlaceOrderProductModel();
                marketPlaceOrderProductModel.setProductId(object.optString("ProductId"));
                marketPlaceOrderProductModel.setProductName(object.optString("ProductName"));
                marketPlaceOrderProductModel.setProductUrl(object.optString("ProductUrl"));
                marketPlaceOrderProductModel.setUnit(object.optInt("Unit"));
                marketPlaceOrderProductModel.setPrice(object.optDouble("Price"));
                marketPlaceOrderProductModel.setTotalSum(object.optDouble("TotSum"));
                marketPlaceOrderProductModel.setRowId(object.optString("RowsId"));
                marketPlaceOrderProductModels.add(marketPlaceOrderProductModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return marketPlaceOrderProductModels;

    }

    public static ArrayList<MarketPlaceMyOrdersModel> parseMarketPlaceMyOrders(JSONArray jsonArray) {
        ArrayList<MarketPlaceMyOrdersModel> marketPlaceMyOrdersModelArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                MarketPlaceMyOrdersModel marketPlaceMyOrdersModel = new MarketPlaceMyOrdersModel();
                marketPlaceMyOrdersModel.setBookedDate(object.optString("BookedDate"));
                marketPlaceMyOrdersModel.setBrandName(object.optString("BrandName"));
                marketPlaceMyOrdersModel.setCategoryName(object.optString("CategoryName"));
                marketPlaceMyOrdersModel.setDescription(object.optString("Description"));
                marketPlaceMyOrdersModel.setActionDate(object.optString("ActionDate"));
                marketPlaceMyOrdersModel.setOrderId(object.optString("OrderId"));
                marketPlaceMyOrdersModel.setPrice(object.optString("Price"));
                marketPlaceMyOrdersModel.setProductId(object.optString("ProductId"));
                marketPlaceMyOrdersModel.setProductName(object.optString("ProductName"));
                marketPlaceMyOrdersModel.setActionId(object.optString("ActionId"));
                marketPlaceMyOrdersModel.setProductImageUrl(object.optString("ProductImageUrl"));

                marketPlaceMyOrdersModelArrayList.add(marketPlaceMyOrdersModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return marketPlaceMyOrdersModelArrayList;
    }
}