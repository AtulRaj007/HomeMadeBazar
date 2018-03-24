package com.homemadebazar.util;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.homemadebazar.R;
import com.homemadebazar.activity.ChangePasswordActivity;
import com.homemadebazar.activity.FoodieHomeActivity;
import com.homemadebazar.activity.HomeActivity;
import com.homemadebazar.activity.LoginActivity;
import com.homemadebazar.activity.MarketPlaceHomeActivity;
import com.homemadebazar.activity.MyOrdersActivity;
import com.homemadebazar.activity.MyProfileActivity;
import com.homemadebazar.activity.ProfileViewActivity;
import com.homemadebazar.activity.TransactionHistoryActivity;
import com.homemadebazar.activity.WalletActivity;
import com.homemadebazar.activity.WebViewActivity;
import com.homemadebazar.fragment.AppWalkthroughFragment;
import com.homemadebazar.fragment.UserProfileFragment;
import com.homemadebazar.model.AppWalkThroughModel;
import com.homemadebazar.model.FoodDateTimeBookModel;
import com.homemadebazar.model.UserLocation;
import com.homemadebazar.model.UserModel;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sumit on 27/08/17.
 */

public class Utils {

    public static final String TYPE_PHONE_NUMBER = "phone_number";
    public static final String TYPE_EMAIL = "email";
    public static Uri cameraUri;
    static Pattern letter = Pattern.compile("[a-zA-z]");
    static Pattern digit = Pattern.compile("[0-9]");
    static Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
    static Pattern eight = Pattern.compile(".{8}");

    public static void generateKeyHash(Context mContext) {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo("com.homemadebazar", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public static void handleError(String message, final Context context, Runnable runnable) {
//        DialogUtils.showAlert(context, message);
    }

    public static boolean isValid(String input, String type) {
        boolean valid = false;
        try {
            if (type.equals(TYPE_EMAIL)) {
                if (!TextUtils.isEmpty(input.trim())) {
                    valid = android.util.Patterns.EMAIL_ADDRESS.matcher(input)
                            .matches();
                }
            } else if (type.equals(TYPE_PHONE_NUMBER) && !TextUtils.isEmpty(input.trim())) {
                String regEx = "^[0-9]{10}$";
                valid = input.matches(regEx);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return valid;
        }
    }

    public static void noRecordFound(TextView textView, boolean status) {
        if (status)
            textView.setVisibility(View.VISIBLE);
    }

    public static void cameraIntent(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.Keys.REQUEST_PERMISSION_CAMERA);

        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".png");
            cameraUri = Uri.fromFile(file);
//            cameraUri = FileProvider.getUriForFile(activity,
//                    BuildConfig.APPLICATION_ID + ".provider",
//                    file);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            activity.startActivityForResult(cameraIntent, Constants.Keys.REQUEST_CAMERA);
        }
    }

    public static void cameraIntent(Activity activity, android.support.v4.app.Fragment fragment) {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.Keys.REQUEST_PERMISSION_CAMERA);

        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".png");
            cameraUri = Uri.fromFile(file);
//            cameraUri = FileProvider.getUriForFile(activity,
//                    BuildConfig.APPLICATION_ID + ".provider",
//                    file);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            fragment.startActivityForResult(cameraIntent, Constants.Keys.REQUEST_CAMERA);
        }
    }

    public static Uri getCameraUri() {
        if (cameraUri != null)
            return cameraUri;
        return null;
    }

    public static void gallaryIntent(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.Keys.REQUEST_PERMISSION_GALLERY);
        } else {
            Intent gallaryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            gallaryIntent.setType("image/*");
            activity.startActivityForResult(Intent.createChooser(gallaryIntent, "Select Picture"), Constants.Keys.REQUEST_GALLERY);
        }
    }

    public static void gallaryIntent(Activity activity, android.support.v4.app.Fragment fragment) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.Keys.REQUEST_PERMISSION_GALLERY);
        } else {
            Intent gallaryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            gallaryIntent.setType("image/*");
            fragment.startActivityForResult(Intent.createChooser(gallaryIntent, "Select Picture"), Constants.Keys.REQUEST_GALLERY);
        }
    }

    public static String getRealPathFromURI(Uri contentURI, Context context) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file
            // path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                System.out.println("Coulumn name :" + cursor.getColumnName(i));
                System.out.println("Coulumn type :" + cursor.getType(i));
                try {
                    System.out.println("Coulumn value :" + cursor.getString(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static void openAccountTypeHomeScreen(Context context, String accountType) {
        if (accountType.equals(Constants.AccountType.HOME_CHEF)) {
            Intent intent = new Intent(context, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        } else if (accountType.equals(Constants.AccountType.FOODIE)) {
            Intent intent = new Intent(context, FoodieHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        } else if (accountType.equals(Constants.AccountType.MARKET_PLACE)) {
            Intent intent = new Intent(context, MarketPlaceHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }
    }

    public static void runAppWalkthrough(Context context, FragmentManager fragmentManager, String accountType) {
        boolean isWalkThroughShow = false;
        AppWalkThroughModel appWalkThroughModel = SharedPreference.getWalkThroughModel(context);
        if (accountType.equals(Constants.Role.HOME_CHEF.getStringRole())) {
            // HomeChef
            if (!appWalkThroughModel.isShowToHomeChef()) {
                isWalkThroughShow = true;
                appWalkThroughModel.setShowToHomeChef(true);
            }
        } else if (accountType.equals(Constants.Role.FOODIE.getStringRole())) {
            // Foodie
            if (!appWalkThroughModel.isShowToFoodie()) {
                isWalkThroughShow = true;
                appWalkThroughModel.setShowToFoodie(true);
            }

        } else {
            // MarketPlace
            if (!appWalkThroughModel.isShowToMarketPlace()) {
                isWalkThroughShow = true;
                appWalkThroughModel.setShowToMarketPlace(true);
            }

        }
        if (isWalkThroughShow) {
            AppWalkthroughFragment appWalkthroughFragment = new AppWalkthroughFragment();
            appWalkthroughFragment.setStyle(R.style.CustomDialog, android.R.style.Theme);
            appWalkthroughFragment.show(fragmentManager, "WalkThrough");
            SharedPreference.saveWalkThroughModel(context, appWalkThroughModel);
        }
    }

    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        Log.i("URI", uri + "");
        String result = uri + "";
        // DocumentProvider
        //  if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        if (isKitKat && (result.contains("media.documents"))) {
            String[] ary = result.split("/");
            int length = ary.length;
            String imgary = ary[length - 1];
            final String[] dat = imgary.split("%3A");
            final String docId = dat[1];
            final String type = dat[0];
            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
            } else if ("audio".equals(type)) {
            }
            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{
                    dat[1]
            };
            return getDataColumn(context, contentUri, selection, selectionArgs);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static void setupUserToCrashAnalytics(UserModel userModel) {
        try {
            String userIdentifier = userModel.getFirstName() + " " + userModel.getLastName() + " " + userModel.getUserId();
            Crashlytics.setUserIdentifier(userIdentifier);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getAppStoreUrl(Context context) {

        try {
            String url = "https://play.google.com/store/apps/details?id=" + context.getApplicationContext().getPackageName();
            System.out.println("Store Url:-" + url);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void hideSoftKeyboard(Activity context) {
        if (context.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static boolean checkLocationPermission(Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.Keys.LOCATION_REQUEST);
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.Keys.LOCATION_REQUEST);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static int parseInteger(String value) {
        try {
            int a = Integer.parseInt(value);
            return a;
        } catch (Exception e) {
            return 0;
        }
    }

    public static Bitmap takeScreenshot(View rootView) {
        Bitmap b = Bitmap.createBitmap(rootView.getWidth(), rootView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        rootView.draw(c);
        return b;

    }

    public static ArrayList<FoodDateTimeBookModel> parseFoodBookDateTime(String parseFoodDateTime) {
//        String temp = "12-01-2018,0,0,0;13-01-2018,0,0,0;14-01-2018,0,0,0;15-01-2018,0,0,0;16-01-2018,0,0,0;17-01-2018,0,0,0;18-01-2018,1,0,0;19-01-2018,1,1,1";
        ArrayList<FoodDateTimeBookModel> foodDateTimeBookModels = new ArrayList<>();
        try {
            String bookTiming[] = parseFoodDateTime.split(";");
            for (int i = 0; i < bookTiming.length; i++) {
                String details[] = bookTiming[i].split(",");
                FoodDateTimeBookModel foodDateTimeBookModel = new FoodDateTimeBookModel();
                foodDateTimeBookModel.setDate(details[0]);
                foodDateTimeBookModel.setBreakFast(details[1].equals("1") ? true : false);
                foodDateTimeBookModel.setLunch(details[2].equals("1") ? true : false);
                foodDateTimeBookModel.setDinner(details[3].equals("1") ? true : false);

                foodDateTimeBookModels.add(foodDateTimeBookModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foodDateTimeBookModels;
    }

    public static void onNavItemClick(final Context context, View v, final String userId) {

        switch (v.getId()) {
            case R.id.rl_header:
                context.startActivity(new Intent(context, MyProfileActivity.class));
                break;
            case R.id.tv_my_orders:
                context.startActivity(new Intent(context, MyOrdersActivity.class));
                break;
            case R.id.tv_my_wallet:
                context.startActivity(new Intent(context, WalletActivity.class));
                break;
            case R.id.tv_sales_report:
                Toast.makeText(context, "Development Mode", Toast.LENGTH_LONG).show();
                break;
            case R.id.tv_transaction_history:
                context.startActivity(new Intent(context, TransactionHistoryActivity.class));
                break;
            case R.id.tv_change_password:
                context.startActivity(new Intent(context, ChangePasswordActivity.class));
                break;
            case R.id.tv_terms_of_use:
                context.startActivity(WebViewActivity.getWebViewIntent(context, Constants.WebViewTitleUrl.TERMS_OF_USE.getTitle(), Constants.WebViewTitleUrl.TERMS_OF_USE.getUrl()));
                break;
            case R.id.tv_privacy_policy:
                context.startActivity(WebViewActivity.getWebViewIntent(context, Constants.WebViewTitleUrl.PRIVACY_POLICY.getTitle(), Constants.WebViewTitleUrl.PRIVACY_POLICY.getUrl()));
                break;
            case R.id.tv_about:
                context.startActivity(WebViewActivity.getWebViewIntent(context, Constants.WebViewTitleUrl.ABOUT_US.getTitle(), Constants.WebViewTitleUrl.ABOUT_US.getUrl()));
                break;
            case R.id.tv_logout:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("HomeMadeBazar");
                alertDialogBuilder.setMessage("Are you sure you want to exit...");
                alertDialogBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                UserLocation userLocation = SharedPreference.getUserLocation(context);
                                AppWalkThroughModel appWalkThroughModel = SharedPreference.getWalkThroughModel(context);
                                ServiceUtils.deviceLoginLogoutApiCall(context, userId, deviceToken, Constants.LoginHistory.LOGOUT);
                                SharedPreference.clearSharedPreference(context);
                                SharedPreference.saveUserLocation(context, userLocation);
                                SharedPreference.saveWalkThroughModel(context, appWalkThroughModel);
                                context.startActivity(new Intent(context, LoginActivity.class));
                                ((Activity) context).finish();
                            }
                        });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialogBuilder.show();
                break;
        }

    }

    public static boolean checkPassword(String password) {
        Matcher hasLetter = letter.matcher(password);
        Matcher hasDigit = digit.matcher(password);
        Matcher hasSpecial = special.matcher(password);
        boolean isMinEightDigit = password.length() >= 8 ? true : false;
        return hasLetter.find() && hasDigit.find() && hasSpecial.find()
                && isMinEightDigit;
    }

    public static String parseUrl(String url) {
        return url.replaceAll(" ", "%20");
    }

    public static void showUserProfile(Activity context) {
        FragmentTransaction ft = context.getFragmentManager().beginTransaction();
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        userProfileFragment.show(ft, "User Profile Dialog");
    }

    public static ArrayList<String> getPriceArray(String title) {
        ArrayList<String> priceArray = new ArrayList<>();
        if (!TextUtils.isEmpty(title)) {
            priceArray.add(title);
        }
        for (int i = 20; i <= 1000; i += 20) {
            priceArray.add(String.valueOf(i));
        }
        return priceArray;
    }

    public static void showProfile(Context context, String userId) {
        context.startActivity(ProfileViewActivity.getProfileIntent(context, userId));
    }

    public static boolean checkLocationProvider(final Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
            }

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
            }

            if (!gps_enabled && !network_enabled) {
                // notify user
                DialogUtils.showAlert(context, "Location settings is disabled.Please turn on location settings.", new Runnable() {
                    @Override
                    public void run() {
                        // OK
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(myIntent);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        // Cancel
                        ((Activity) context).finish();
                    }
                });
            } else {
                return true;
            }
        }
        return false;
    }

    public static void startCall(Context context, String mobileNumber) {
        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + mobileNumber));
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void message(Context context, String mobileNumber) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        context.startActivity(sendIntent);
    }

    public static void showDirections(Context mContext, double curLatitude, double curLongitude, double destLatitude, double destLongitude) {

        final Intent intent = new
                Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" +
                "saddr=" + curLatitude + "," + curLongitude + "&daddr=" + destLatitude + "," +
                destLongitude));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        mContext.startActivity(intent);

    }

    public static String getRulesText(String rules) {
        String strRules = "";
        try {
            String temp[] = rules.split("@@");
            for (int i = 0; i < temp.length; i++) {
                strRules = strRules + temp[i];
                if (i != temp.length - 1)
                    strRules = strRules + "\n";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strRules;
    }
}
