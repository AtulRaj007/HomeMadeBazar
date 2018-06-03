package com.munchmash.util;

import com.munchmash.R;
import com.munchmash.model.UserModel;

/**
 * Created by Sumit on 30/07/17.
 */

public class Constants {

    public static final String YOUTUBE_KEY = "AIzaSyD1S-0ZTNEFdcDfPstALlq0wVxiNYI1dHk";
    public static boolean isBalanceRefresh = false;
    public static boolean isMarketPlaceOrderRefresh = false;
    public static boolean isProfileUpdate = false;
    public static boolean isFlashDataChanges = false;
    public static boolean isFavouritesChange = false;

    public static String activeChatUserId = "";

    public static String deviceType = "1";
    public static UserModel socialUserModel = null;
    public static String zipJson = "2"; // 1=true,  2=false
    public static String decode = "1"; // 1=true,  2=false
    public static boolean encode = (decode.equalsIgnoreCase("1"));
    public static boolean zip = (zipJson.equalsIgnoreCase("1"));

    public static String[][] profileInterests = {
            {String.valueOf(R.drawable.profile_art_museum), "Art & Museum"},
            {String.valueOf(R.drawable.profile_camping), "Camping"},
            {String.valueOf(R.drawable.profile_cooking), "Cooking"},
            {String.valueOf(R.drawable.profile_dining_out), "Dining Out"},
            {String.valueOf(R.drawable.profile_handicrafts), "Handicrafts"},
            {String.valueOf(R.drawable.profile_movies), "Movies"},
            {String.valueOf(R.drawable.profile_music_concerts), "Music & Concerts"},
            {String.valueOf(R.drawable.profile_party), "Party"},
            {String.valueOf(R.drawable.profile_politics), "Politics"},
            {String.valueOf(R.drawable.profile_religious), "Religious"},
            {String.valueOf(R.drawable.profile_shopping), "Shopping"},
            {String.valueOf(R.drawable.profile_startup), "Startup"},
            {String.valueOf(R.drawable.profile_video_games), "Video Games"},
            {String.valueOf(R.drawable.profile_watching_sports), "Watching Sports"},
            {String.valueOf(R.drawable.profile_wine_tasting), "Wine Tasting"}
    };

    public static String[] RequestString =
            {
                    "Add Friend",     // NO Status
                    "Request Sent",     // Request Sent
                    "Request Received", // Request Received
                    "Friend",           // Friend
                    "Request Rejected", // Reject
                    "Unknown",
                    "Add Friend"        // UnFriend
            };

    public enum Role {
        HOME_CHEF(1), FOODIE(2), MARKET_PLACE(3);
        int position = -1;

        Role(int position) {
            this.position = position;
        }

        public int getRole() {
            return position;
        }

        public String getStringRole() {
            return String.valueOf(position);
        }
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

    public interface AppDebug {
        boolean isPaymentDebug = false; // Should be false in production
    }

    public interface FoodTiming {
        int Selected = 1;
        int Unselected = 0;
    }

    public interface ServerURL {

        String BASE_URL = "http://18.219.188.20/api/";
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
        String SAVE_FAVOURITE = BASE_URL + "Miscellaneous/SaveFavourite";
        String DELETE_FAVOURITE = BASE_URL + "Foodies/DeleteFavouritesHomeChif";
        String GET_LIST_OF_FAVOURITE_HOME_CHEF = BASE_URL + "Foodies/GetLstOfFavouritesHomeChif";
        String MARKET_PLACE_PRODUCT_SEARCH = BASE_URL + "MarketPlace/MarketplaceSearch";
        String HOMECHEF_BOOK_PRODUCT = BASE_URL + "MarketPlace/BuyProduct";
        String SEND_MONEY_TO_BANK = BASE_URL + "Miscellaneous/BnkTransfer";
        String FOODIE_CHECK_IN = BASE_URL + "Miscellaneous/FoodieCheckIn";
        String HOMECHEF_ORDER_DELETE = BASE_URL + "Miscellaneous/OrdersDelete";
        String HOMECHEF_UNFRIEND_USER = BASE_URL + "Messenger/Unfriends";
        String SAVE_RATING_FEEDBACK = BASE_URL + "Miscellaneous/SaveRatingFeedback";
        String MARKETPLACE_INCOMING_OUTGOING_ORDERS = BASE_URL + "MarketPlace/ShowIncomingOrders";
        String ACTION_BY_MARKET_PLACE_USERS = BASE_URL + "MarketPlace/ActionByMarketPlaceUsers";
        String MARKETPLACE_ORDERS = BASE_URL + "marketplace/MyOrders";
        String MARKETPLACE_GET_TOTAL_SALE_OF_DAY = BASE_URL + "MarketPlace/GetTotalSaleOftheDay";

    }


    public interface uploadImageURL {
        String PROFILE_IMAGE_UPLOAD = ServerURL.BASE_URL + "Profile/DPProfileImage?UserId=";
        String COVER_PHOTO_IMAGE_UPLOAD = ServerURL.BASE_URL + "MyShop/AddHomeChefCoverPhoto?UserId=";
        String FOOD_PHOTO_IMAGE_UPLOAD = ServerURL.BASE_URL + "CreateOrder/FoodImage?UserId="; //UserId=17082712&OrderId=6DE9F4E8
        String FOOD_POST_UPLOAD_URL = ServerURL.BASE_URL + "Flash/PostMessage?Option=";
        String MARKETPLACE_ADD_CATEGORY = ServerURL.BASE_URL + "MarketPlace/AddProductCategory?UserId=";
        String MARKETPLACE_ADD_EDIT_PRODUCT = ServerURL.BASE_URL + "MarketPlace/AddEditProduct?UserId=";

    }

    public interface ServerResponseCode {
        int SUCCESS = 100;
        int NO_RECORD_FOUND = 151;
        int INSUFFICIENT_MONEY = 123;
        int NO_INFORMATION_USER = 120;
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
        int REQUEST_NOT_SENT = 0;   //  Add Friend
        int REQUEST_SENT = 1;       //  Request Sent
        int REQUEST_RECEIVED = 2;   //  Request Received
        int FRIEND = 3;             // Friends
        int REJECT = 4;             // Reject
        int UNKNOWN = 5; // Already Request Sent
        int UNFRIEND = 6;           // UnFriend
    }

    public interface DinnerTime {
        int BREAKFAST = 1;
        int LUNCH = 2;
        int DINNER = 3;
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
        int MARKETPLACE_INCOMING_ORDER = 6;
        int MARKETPLACE_ACCEPT_REJECT_DISPATCH_ORDER = 7;
        int RECEIVE_MONEY = 8;
    }

    public interface BundleKeys {
        String RECEIVER_ID = "RECEIVER_ID";
        String SENDER_ID = "SENDER_ID";
        String MESSAGE = "MESSAGE";
        String FIRST_NAME = "FIRST_NAME";
        String LAST_NAME = "LAST_NAME";
        String PROFILE_PIC = "PROFILE_PIC";
        String CHECK_IN_MODEL = "CHECK_IN_MODEL";
    }

    public interface HomeChefOrder {
        String NowOrder = "NOW";
        String SCHEDULED = "SCHEDULED";
        String HOMECHEF_ORDER = "HomeChef_Order";
        String FOODIE_BOOKED_ORDER = "FOODIE_ORDER";
    }

    public interface DiscoverTab {
        String TopDeals = "1";
        String MealsUnderPrice = "2";
        String TopChefs = "3";
    }

    public interface SkillHubViewType {
        int TITLE = 0;
        int VIDEO = 1;
    }

    //    1 For Request from foodies,2 Accepted from HC ,3 Rejected from HC,4 Rejected from Foodies,5 Completed by HC and Foodies,6 Pending after due date passed
    public interface OrderActionType {
        String FOODIE_BOOKED_ORDER = "1";
        String HC_ACCEPTED_ORDER = "2";
        String HC_REJECTED_ORDER = "3";
        String FOODIE_CANCELLED_ORDER = "4";
        String HC_COMPLETED_ORDER = "5";
        String PENDING_ORDER = "6";
    }

    public interface MarketPlaceOrder {
        String INCOMING_ORDER = "0";
        String OUTGOING_ORDER = "1";
    }

    public interface MarketPlaceOrderACtionType {
        String PRODUCT_BOOK = "0";
        String ACCEPT = "1";
        String REJECT = "2";
        String DISPATCH = "3";
    }

}
