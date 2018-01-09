package com.homemadebazar.fcm;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.homemadebazar.util.Constants;

/**
 * Created by atulraj on 24/12/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(">>>>>FCM", remoteMessage.getData().toString());
        System.out.println(Constants.ServiceTAG.NOTIFICATION + remoteMessage.getData().toString());

    }

    private void showNotification() {

    }
}

//{ReceiverId=1801062, Message=ABC Shah has send you a message., Title=New Message, NotificationType=2, SenderId=1801060}