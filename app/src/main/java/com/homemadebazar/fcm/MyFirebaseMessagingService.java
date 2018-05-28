package com.homemadebazar.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.homemadebazar.R;
import com.homemadebazar.activity.ChatActivity;
import com.homemadebazar.activity.NotificationActivity;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

/**
 * Created by atulraj on 24/12/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private MediaPlayer m = new MediaPlayer();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(">>>>>FCM", remoteMessage.getData().toString());
        System.out.println(Constants.ServiceTAG.NOTIFICATION + remoteMessage.getData().toString());

        /* Checking Device Login*/
        boolean isLoggedIn = SharedPreference.getBooleanPreference(this, SharedPreference.IS_LOGGED_IN);
        if (!isLoggedIn)
            return;

        try {
            int notificationType = Integer.parseInt(remoteMessage.getData().get("NotificationType"));
            if (notificationType != Constants.NotificationType.INCOMING_MESSAGE) {
                playNotificationSound();
                int count = SharedPreference.getIntegerPreference(this, SharedPreference.NOTIFICATION_COUNT);
                count++;
                SharedPreference.setIntegerPreference(this, SharedPreference.NOTIFICATION_COUNT, count);
                Utils.sendLocalNotificationCount(this);
            } else {
                String senderId = remoteMessage.getData().get("SenderId");
                if (!senderId.equals(Constants.activeChatUserId)) {
                    playNotificationSound();
                }
            }

            if (notificationType == Constants.NotificationType.INCOMING_MESSAGE) {
                Intent messageIntent = new Intent(Constants.BroadCastFilter.INCOMING_MESSAGE);
                messageIntent.putExtra(Constants.BundleKeys.RECEIVER_ID, remoteMessage.getData().get("ReceiverId"));
                String senderId = remoteMessage.getData().get("SenderId");
                messageIntent.putExtra(Constants.BundleKeys.SENDER_ID, senderId);
                messageIntent.putExtra(Constants.BundleKeys.MESSAGE, remoteMessage.getData().get("Message"));
                LocalBroadcastManager.getInstance(MyFirebaseMessagingService.this).sendBroadcast(messageIntent);
                UserModel targetUserModel = new UserModel();
                targetUserModel.setUserId(remoteMessage.getData().get("SenderId"));
                targetUserModel.setFirstName(remoteMessage.getData().get("Name"));
                targetUserModel.setLastName("");
                targetUserModel.setProfilePic(remoteMessage.getData().get("DP"));
                Intent intent = ChatActivity.getChatIntent(MyFirebaseMessagingService.this, targetUserModel);
                int id = -1;
                try {
                    id = Integer.parseInt(remoteMessage.getData().get("SenderId"));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    id = (int) System.currentTimeMillis();
                }
                if (!senderId.equals(Constants.activeChatUserId)) {
                    showNotification(intent, remoteMessage.getData().get("Title"), remoteMessage.getData().get("Message"), id);
                }
            } else if (notificationType == Constants.NotificationType.FOODIE_ORDER_BOOKED) {
                Intent intent = new Intent(MyFirebaseMessagingService.this, NotificationActivity.class);
                String message = remoteMessage.getData().get("Message");
                String title = remoteMessage.getData().get("Title");
                String bookingId = remoteMessage.getData().get("Bookingid");
                showNotification(intent, title, message, notificationType);

            } else if (notificationType == Constants.NotificationType.FOODIE_ORDER_ACCEPT_REJECT) {
                Intent intent = new Intent(MyFirebaseMessagingService.this, NotificationActivity.class);
                String message = remoteMessage.getData().get("Message");
                String title = remoteMessage.getData().get("Title");
                showNotification(intent, title, message, notificationType);

            } else if (notificationType == Constants.NotificationType.FRIEND_REQUEST_RECEIVED) {
                Intent intent = new Intent(MyFirebaseMessagingService.this, NotificationActivity.class);
                String message = remoteMessage.getData().get("Message");
                String title = remoteMessage.getData().get("Title");
                showNotification(intent, title, message, notificationType);
            } else if (notificationType == Constants.NotificationType.FRIEND_ACCEPTED) {
                Intent intent = new Intent(MyFirebaseMessagingService.this, NotificationActivity.class);
                String message = remoteMessage.getData().get("Message");
                String title = remoteMessage.getData().get("Title");
                showNotification(intent, title, message, notificationType);
            } else if (notificationType == Constants.NotificationType.MARKETPLACE_INCOMING_ORDER) {
                Intent intent = new Intent(MyFirebaseMessagingService.this, NotificationActivity.class);
                String message = remoteMessage.getData().get("Message");
                String title = remoteMessage.getData().get("Title");
                showNotification(intent, title, message, notificationType);
            } else if (notificationType == Constants.NotificationType.MARKETPLACE_ACCEPT_REJECT_DISPATCH_ORDER) {
                Intent intent = new Intent(MyFirebaseMessagingService.this, NotificationActivity.class);
                String message = remoteMessage.getData().get("Message");
                String title = remoteMessage.getData().get("Title");
                showNotification(intent, title, message, notificationType);
            } else if (notificationType == Constants.NotificationType.RECEIVE_MONEY) {
                Intent intent = new Intent(MyFirebaseMessagingService.this, NotificationActivity.class);
                String message = remoteMessage.getData().get("Message");
                String title = remoteMessage.getData().get("Title");
                showNotification(intent, title, message, notificationType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotification(Intent intent, String title, String messageBody, int id) {

//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis()/* ID of row_notification*/, notificationBuilder.build());
        /*Later on System Time can be replaced using UserId*/
    }

    private void playNotificationSound() {
        try {
            try {
                if (m.isPlaying()) {
                    m.stop();
                    m.release();
                    m = new MediaPlayer();
                }

                AssetFileDescriptor descriptor = getAssets().openFd("notification.mp3");
                m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();

                m.prepare();
                m.setVolume(1f, 1f);
                m.setLooping(false);
                m.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
