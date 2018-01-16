package com.homemadebazar.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.homemadebazar.model.BaseModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.DeviceLoginLogoutApiCall;

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
                                if (loginHistory == Constants.LoginHistory.LOGOUT)
                                    Toast.makeText(context, baseModel.getStatusMessage(), Toast.LENGTH_SHORT).show();
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
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), context, null);
        }
    }
}
