package com.munchmash.util;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.munchmash.model.BaseModel;
import com.munchmash.network.HttpRequestHandler;
import com.munchmash.network.api.ApiCall;
import com.munchmash.network.apicall.ActionByMarketPlaceUsersApiCall;
import com.munchmash.network.apicall.DeviceLoginLogoutApiCall;
import com.munchmash.network.apicall.HomeChefFoodieOrderAcceptRejectApiCall;
import com.munchmash.network.apicall.SaveUserRatingApiCall;

/**
 * Created by Atul on 1/16/18.
 */

public class ServiceUtils {

    public static void deviceLoginLogoutApiCall(final Context context, String userId, String token, final int loginHistory) {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(context, null);
            if (loginHistory == Constants.LoginHistory.LOGOUT) {
                progressDialog.show();
            }

            final DeviceLoginLogoutApiCall apiCall = new DeviceLoginLogoutApiCall(userId, token, loginHistory);
            HttpRequestHandler.getInstance(context.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    if (loginHistory == Constants.LoginHistory.LOGOUT)
                        DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                if (loginHistory == Constants.LoginHistory.LOGOUT) {
                                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.cancelAll();
                                    Toast.makeText(context, "Logout Successful", Toast.LENGTH_SHORT).show();
                                    SharedPreference.setBooleanPreference(context, SharedPreference.IS_LOGGED_IN, false);
                                } else {
                                    SharedPreference.setBooleanPreference(context, SharedPreference.IS_LOGGED_IN, true);
                                }
                            } else {
                                Toast.makeText(context, baseModel.getStatusMessage(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), context, null);
                    }
                }
            }, false);
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), context, null);
        }
    }

    public static void foodieOrderAcceptReject(final Context context, String userId, String bookingReferenceId, String orderActionType, String otp, final OrderActionInterface orderActionInterface) {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(context, null);
            progressDialog.show();

            final HomeChefFoodieOrderAcceptRejectApiCall apiCall = new HomeChefFoodieOrderAcceptRejectApiCall(userId, bookingReferenceId, orderActionType, otp);
            HttpRequestHandler.getInstance(context.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            orderActionInterface.onOrderAction(baseModel);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), context, null);
                    }
                }
            }, false);
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), context, null);
        }
    }

    public static void submitReview(final Context context, String userId, String ratingToUserId, int rating, String ratingAgainstOrderId, String desc) {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(context, null);
            progressDialog.show();

            final SaveUserRatingApiCall apiCall = new SaveUserRatingApiCall(userId, ratingToUserId, rating, ratingAgainstOrderId, desc);
            HttpRequestHandler.getInstance(context.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                DialogUtils.showAlert(context, "Review Submitted Successfully");
                            } else {
                                DialogUtils.showAlert(context, baseModel.getStatusMessage());
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), context, null);
                    }
                }
            }, false);
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), context, null);
        }
    }

    public static void actionByMarketPlaceUsers(final Context context, String userId, String actionType, String rowId, final MarketPlaceOrderActionInterface marketPlaceOrderActionInterface) {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(context, null);
            progressDialog.show();

            final ActionByMarketPlaceUsersApiCall apiCall = new ActionByMarketPlaceUsersApiCall(userId, actionType, rowId);
            HttpRequestHandler.getInstance(context.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            marketPlaceOrderActionInterface.onOrderAtion(baseModel);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), context, null);
                    }
                }
            }, false);
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), context, null);
        }
    }

    public interface OrderActionInterface {
        void onOrderAction(BaseModel baseModel);
    }

    public interface MarketPlaceOrderActionInterface {
        void onOrderAtion(BaseModel baseModel);
    }
}
