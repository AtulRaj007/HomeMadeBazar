package com.homemadebazar.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.homemadebazar.R;
import com.homemadebazar.activity.ChatActivity;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.util.Constants;

/**
 * Created by atulraj on 24/12/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(">>>>>FCM", remoteMessage.getData().toString());
        System.out.println(Constants.ServiceTAG.NOTIFICATION + remoteMessage.getData().toString());
        try {
            int notificationType = Integer.parseInt(remoteMessage.getData().get("NotificationType"));
            if (notificationType == Constants.NotificationType.INCOMING_MESSAGE) {
                Intent messageIntent = new Intent(Constants.BroadCastFilter.INCOMING_MESSAGE);
                messageIntent.putExtra(Constants.BundleKeys.RECEIVER_ID, remoteMessage.getData().get("ReceiverId"));
                messageIntent.putExtra(Constants.BundleKeys.SENDER_ID, remoteMessage.getData().get("SenderId"));
                messageIntent.putExtra(Constants.BundleKeys.MESSAGE, remoteMessage.getData().get("Message"));
                LocalBroadcastManager.getInstance(MyFirebaseMessagingService.this).sendBroadcast(messageIntent);
                UserModel targetUserModel = new UserModel();
                targetUserModel.setUserId(remoteMessage.getData().get("SenderId"));
                targetUserModel.setFirstName(remoteMessage.getData().get("FirstName"));
                targetUserModel.setLastName("LastName");
                targetUserModel.setProfilePic("ProfilePic");
                Intent intent = ChatActivity.getChatIntent(MyFirebaseMessagingService.this, targetUserModel);
                int id = -1;
                try {
                    id = Integer.parseInt(remoteMessage.getData().get("SenderId"));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    id = (int) System.currentTimeMillis();
                }
                showNotification(intent, remoteMessage.getData().get("Title"), remoteMessage.getData().get("Message"), id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotification(Intent intent, String title, String messageBody, int Id) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1/* ID of row_notification*/, notificationBuilder.build());
    }
}

//{ReceiverId=1801060, Message=Atul Jio has send you a message., Title=New Message, NotificationType=2, SenderId=1801062}
//{ReceiverId=1801062, Message=ABC Shah has send you a message., Title=New Message, NotificationType=2, SenderId=1801060}
//{ReceiverId=1801060, Message=Atul Jio has send you a message., DP=http://103.54.24.25:200/api/CreateOrder/GetImage, Name=Atul Jio, Title=New Message, NotificationType=2, SenderId=1801062}
