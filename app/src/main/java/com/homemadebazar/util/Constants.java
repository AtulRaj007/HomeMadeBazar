package com.homemadebazar.util;

import com.homemadebazar.model.UserModel;

/**
 * Created by Sumit on 30/07/17.
 */

public class Constants {

    public static final String YOUTUBE_KEY = "AIzaSyD1S-0ZTNEFdcDfPstALlq0wVxiNYI1dHk";
    public static String deviceType = "1";
    public static UserModel socialUserModel = null;
    public static String zipJson = "2"; // 1=true,  2=false
    public static String decode = "1"; // 1=true,  2=false
    public static boolean encode = (decode.equalsIgnoreCase("1"));
    public static boolean zip = (zipJson.equalsIgnoreCase("1"));

    public enum Role {
        HOME_CHEF(1), FOODIE(2), MARKET_PLACE(3);
        int position = -1;

        Role(int position) {
            this.position = position;
        }

        public int getRole() {
            return position;
        }
    }

    public enum MessageFetchType {
        NEW_MESSAGE("new"), OLD_MESSAGE("old");
        String value;

        MessageFetchType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public interface FoodTiming {
        int Selected = 1;
        int Unselected = 0;
    }

    public interface ServerURL {
        //        String BASE_URL = "http://103.54.24.25:200/api/";
        //        String BASE_URL = "http://35.183.8.236/api/";
        String BASE_URL = "http://18.218.139.27/api/";
        String IS_EMAIL_EXIST = BASE_URL + "RegistrationProcess/IsAccountExist";
        String SEND_OTP = BASE_URL + "RegistrationProcess/SendOtp";
        String VERIFY_OTP = BASE_URL + "RegistrationProcess/VerifyOtp";
        String SIGNUP = BASE_URL + "RegistrationProcess/SignUp";
        String SIGNIN = BASE_URL + "RegistrationProcess/SignIn";
        String SOCIAL_LOGIN = BASE_URL + "RegistrationProcess/SocialLogin";
        String CREATE_ORDER = BASE_URL + "CreateOrder/CreateOrder";
        String SHOW_HOME_CHEF_PROFILE = BASE_URL + "MyShop/ShowHomeChefProfile?UserId=";
        String GET_FOOD_CATEGORIES = BASE_URL + "Foodies/GetFoodCategoriesWelcomeScrn";
        String FOODIE_HOME_CHIEF_NEAR_BY_URL = BASE_URL + "Foodies/GetListHomeCheifNearBy";
        String MYSHOP_ADD_DETAILS = BASE_URL + "MyShop/MyShopAddDetails";
        String HOMECHEF_GET_ORDER_DETAILS = BASE_URL + "CreateOrder/GetOrderDetails";
        String HOMECHEF_SKILL_HUB_VIDEOS = BASE_URL + "SkillHub/GetShowSkillHub";
        String FOODIE_MESSAGE_INVITE_PARTICIPATE_URL = BASE_URL + "Messenger/SearchMessengerFriends";
        String FOODIE_MESSAGE_INVITE_SENT_REQUEST_URL = BASE_URL + "Messenger/SendFriendRequest";
        String FOODIE_MESSAGE_FRIENDS_URL = BASE_URL + "Messenger/ShowMessengerFriends";
        String FOODIE_MESSAGE_REQUEST_TAB_URL = BASE_URL + "Messenger/ShowMessengerReqTab";
        String FOODIE_MESSAGE_REQUEST_ACCEPT_REJECT_URL = BASE_URL + "Messenger/MessengerTabAction";
        String FOODIE_GET_FLASH_POST = BASE_URL + "Flash/ShowFlashMessage";
        String FOODIE_GET_HOME_CHEF_ORDERS = BASE_URL + "Foodies/GetLstOfOrdersByCheif";
        String FOODIE_BOOK_ORDER = BASE_URL + "Foodies/FoodOrderRequest";
        String FOODIE_GET_HOME_CHEF_SEARCH_CAT_DISH_VENDOR = BASE_URL + "Foodies/GetFoodSearchByCatDishVendors";
        String HOMECHEF_SEARCH_MARKETPLACE_INGREDIENTS = BASE_URL + "MarketPlace/SearchMarketplace";
        String HOMECHEF_SHOW_ORDERED_LIST = BASE_URL + "CreateOrder/ShowOrderedList";
        String MARKETPLACE_GET_PRODUCT_CATEGORY_LIST = BASE_URL + "MarketPlace/GetProdCategoryList?UserId=";
        String MARKETPLACE_GET_PRODUCT_BRAND_LIST = BASE_URL + "MarketPlace/GetProdBrandList?UserId=";
        String HOMECHEF_APPLY_HOT_DEALS = BASE_URL + "CreateOrder/ApplyDiscForHotDeal";
        String MARKETPLACE_ADD_BRAND = ServerURL.BASE_URL + "MarketPlace/AddProductBrand";
        String MARKETPLACE_SHOW_PRODUCT_LIST = ServerURL.BASE_URL + "MarketPlace/ShowProduct?UserId=";
        String FOODIE_DO_LIKE_COMMENTS = ServerURL.BASE_URL + "Flash/DoLikesComments";
        String GET_WALLET_BALANCE = ServerURL.BASE_URL + "Wallet/GetWalletBalance";
        String ADD_MONEY = BASE_URL + "Wallet/AddMoney";
        String PAY_MONEY = BASE_URL + "Wallet/PayMoney";
        String SEND_ADD_MONEY_STATUS = BASE_URL + "Wallet/AddMoneyStatus";
        String CREATE_PAYMENT = BASE_URL + "Checkouts/CreatePayment?";
        String SEND_MESSAGE = BASE_URL + "Chat/GetChatSend?";
        String GET_MESSAGES = BASE_URL + "Chat/GetChatReceive";
        String DEVICE_LOGIN = BASE_URL + "LoginHistory/DeviceLogin";
        String DEVICE_LOGOUT = BASE_URL + "LoginHistory/DeviceLogout";
        String GET_COMMENT_LIST = BASE_URL + "Flash/ShowCommentsList";
        String PROFILE_UPDATE = BASE_URL + "Miscellaneous/ProfileUpdate";
        String GET_CONTACT_SYNC = BASE_URL + "Miscellaneous/GetContactSync";
        String GET_NOTIFICATION = BASE_URL + "Miscellaneous/GetNotificationBell";
        String GET_TRANSACTION_REPORT = BASE_URL + "Reports/WalletTransactionReports";
        String CHANGE_PASSWORD = BASE_URL + "Miscellaneous/PasswordUpdate";
        String RESET_PASSWORD = BASE_URL + "Miscellaneous/GetResetPassowrd";
        String HOMECHEF_FOODIE_ORDER_ACCEPT_REJECT = BASE_URL + "CreateOrder/DoFoodOrderResponse";
        String FOODIE_HOMECHEF_SEARCH = BASE_URL + "Foodies/GetFoodSearchByCatDishVendors";
        String APPLY_PROMOTE_BUSINESS = BASE_URL + "Miscellaneous/ApplyPramotBussiness";
        String LIST_OF_HOT_DEALS = BASE_URL + "Foodies/GetListOfHotDeals";
        String GET_USER_PROFILE_DETAILS = BASE_URL + "Miscellaneous/GetUserProfileDetails";


//        http://localhost:14013/api/Foodies/GetListOfHotDeals
//        http://localhost:14013/api/Miscellaneous/ApplyPramotBussiness
//        http://localhost:14013/api/Foodies/GetFoodSearchByCatDishVendors
//        http://35.183.8.236/api/CreateOrder/DoFoodOrderResponse
//        http://localhost:14013/api/Miscellaneous/GetResetPassowrd
//        http://localhost:14013/api/Miscellaneous/PasswordUpdate
//        http://103.54.24.25:200/api/Reports/WalletTransactionReports

//        http://103.54.24.25:200/api/Miscellaneous/GetNotificationBell
//        http://103.54.24.25:200/api/Miscellaneous/GetContactSync
//        http://localhost:14013/api/Miscellaneous/ProfileUpdate

//                http://localhost:14013/api/Flash/ShowCommentsList

//        http://localhost:14013/api/LoginHistory/DeviceLogin
//        http://localhost:14013/api/Chat/GetChatSend?SndrId&RcrId&text&FileType&MsgType&Lati&Longi


        //    http://localhost:14013/api/Checkouts/CreatePayment?AMOUNT=5&PAYMENT_METHOD_NONCE=ba5effa2-9240-0af1-2157-7b9bf85b7576&ACCOUNT_ID=HMBWA00000008&TXN_NO=Trans00000020

//                http://localhost:14013/api/Flash/DoLikesComments
//        http://35.183.8.236/api/MarketPlace/ShowProduct?UserId=1712265
//        http://localhost:14013/api/MarketPlace/AddProductBrand
        //    http://localhost:14013/api/MarketPlace/GetProdBrandList?UserId=

//        http://localhost:14013/api/Foodies/GetLstOfOrdersByCheif
//        http://localhost:14013/api/Foodies/GetFoodSearchByCatDishVendors
//        http://35.183.8.236/api/CreateOrder/ShowOrderedList
//        http://localhost:14013/api/MarketPlace/GetProdCategoryList?UserId=
    }


    public interface uploadImageURL {
        String PROFILE_IMAGE_UPLOAD = ServerURL.BASE_URL + "Profile/DPProfileImage?UserId=";
        String COVER_PHOTO_IMAGE_UPLOAD = ServerURL.BASE_URL + "MyShop/AddHomeChefCoverPhoto?UserId=";
        String FOOD_PHOTO_IMAGE_UPLOAD = ServerURL.BASE_URL + "CreateOrder/FoodImage?UserId="; //UserId=17082712&OrderId=6DE9F4E8
        String FOOD_POST_UPLOAD_URL = ServerURL.BASE_URL + "Flash/PostMessage?Option=";
        String MARKETPLACE_ADD_CATEGORY = ServerURL.BASE_URL + "MarketPlace/AddProductCategory?UserId=";
        String MARKETPLACE_ADD_EDIT_PRODUCT = ServerURL.BASE_URL + "MarketPlace/AddEditProduct?UserId=";

//        http://localhost:14013/api/MarketPlace/AddEditProduct?
    }

    public interface ServerResponseCode {
        int SUCCESS = 100;
        int NO_RECORD_FOUND = 151;
    }

    public interface SignInType {
        String GOOGLE = "1";
        String FACEBOOK = "2";
    }

    public interface Keys {
        int REQUEST_PERMISSION_CAMERA = 100;
        int REQUEST_PERMISSION_GALLERY = 101;
        int REQUEST_CAMERA = 103;
        int REQUEST_GALLERY = 104;
        int LOCATION_REQUEST = 105;
        int UPDATE_SHOP_DETAILS = 106;
        int CREATE_ORDER = 107;
        int FOODIE_POST = 108;
        int REQUEST_CALL_PHONE = 108;
        int REQUEST_ADD_CATEGORY = 109;
        int REQUEST_ADD_BRAND = 110;
        int REQUEST_ADD_PRODUCT = 111;
        int REQUEST_ADD_MONEY = 112;
        int READ_CONTACTS_REQUEST = 113;
    }

    public interface AccountType {
        String HOME_CHEF = "1";
        String FOODIE = "2";
        String MARKET_PLACE = "3";
    }

//    file_type(response) : 1 - image, 2 - video, 3 - audio
//    message_type(response) : 1 - text, 2 - file, 3 - location

    public interface FoodType {
        String BREAKFAST = "1";
        String LUNCH = "2";
        String DINNER = "3";
    }

    public interface FileType {
        String IMAGE = "1";
        String VIDEO = "2";
        String AUDIO = "3";
        String NONE = "0";
    }

    public interface MessageType {
        String TEXT = "1";
        String FILE = "2";
        String LOCATION = "3";
    }

    public interface ServiceTAG {
        String URL = ">>>>>URL##";
        String REQUEST = ">>>>>REQUEST##";
        String RESPONSE = ">>>>>RESPONSE##";
        String NOTIFICATION = ">>>>>PUSH##";
        String CONTACTS = ">>>>>CONTACTS##";
    }

    public interface MessageSequeceOrder {
        String OLD = "OLD";
        String NEW = "NEW";
        String CURRENT = "";
    }

    public interface MessageViewType {
        int MESSAGE_TEXT_OWN = 1;
        int MESSAGE_TEXT_OTHER = 2;
        int MESSAGE_IMAGE_OWN = 3;
        int MESSAGE_IMAGE_OTHER = 4;
        int MESSAGE_LOCATION_OWN = 5;
        int MESSAGE_LOCATION_OTHER = 6;
    }

    public interface CommentViewType {
        int MESSAGE_TEXT_OWN = 1;
        int MESSAGE_TEXT_OTHER = 2;
    }

    //    --0 Req Not Sent,1 Req Sent ,2 Req Received,3 Frnd ,4 Reject,5 All Ready Req.
    public interface RequestType {
        int REQUEST_NOT_SENT = 0;
        int REQUEST_SENT = 1;
        int REQUEST_RECEIVED = 2;
        int FRIEND = 3;
        int REJECT = 4;
    }

    public interface DinnerTime {
        int BREAKFAST = 1;
        int LUNCH = 2;
        int DINNER = 3;
    }

    public enum WebViewTitleUrl {
        TERMS_OF_USE("Terms Of Use", "https://www.google.co.in"),
        PRIVACY_POLICY("Privacy Policy", "https://www.google.co.in/"),
        ABOUT_US("About", "https://www.google.co.in/");

        String title;
        String url;

        WebViewTitleUrl(String title, String url) {
            this.title = title;
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }
    }

    public interface PostActionTAG {
        String COMMENTS = "Comments";
        String LIKES = "Likes";
        String UNLIKE = "Unlike";
        String ALL = "All";
    }

    public interface LoginHistory {
        int LOGIN = 1;
        int LOGOUT = 2;
    }

    public interface BroadCastFilter {
        String INCOMING_MESSAGE = "INCOMING_MESSAGE";
    }

    public interface NotificationType {
        int FOODIE_ORDER_BOOKED = 1;
        int INCOMING_MESSAGE = 2;
        int FOODIE_ORDER_ACCEPT_REJECT = 3;
        int FRIEND_REQUEST_RECEIVED = 4;
        int FRIEND_ACCEPTED = 5;

    }

    public interface BundleKeys {
        String RECEIVER_ID = "RECEIVER_ID";
        String SENDER_ID = "SENDER_ID";
        String MESSAGE = "MESSAGE";
        String FIRST_NAME = "FIRST_NAME";
        String LAST_NAME = "LAST_NAME";
        String PROFILE_PIC = "PROFILE_PIC";
    }

    public interface HomeChefOrder {
        String NowOrder = "NOW";
        String SCHEDULED = "SCHEDULED";
        String COMPLETED = "COMPLETED";
    }

//    {"PostId":"PM00000002","ActionType":"Comments/Likes/All/Unlike","Comments":"Superb","UserById":"17110910"}

//    {Message=Foodie foodie has booked an order., Bookingid=HMB00000022, Title=HomeMadeBazar, NotificationType=1}
}
