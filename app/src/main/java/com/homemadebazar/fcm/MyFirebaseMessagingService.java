package com.homemadebazar.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.homemadebazar.R;
import com.homemadebazar.activity.HomeActivity;

/**
 * Created by atulraj on 24/12/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(">>>>>FCM", remoteMessage.getData().toString());

    }

    private void showNotification(){

    }
}

//{ReceiverId=1801062, Message=ABC Shah has send you a message., Title=New Message, NotificationType=2, SenderId=1801060}