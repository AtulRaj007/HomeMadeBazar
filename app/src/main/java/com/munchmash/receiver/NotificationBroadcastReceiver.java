package com.munchmash.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by and-04 on 29/11/17.
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_GET_NOTIFICATION = "row_notification";
    public static final String ACTION_MESSAGE_NOTIFICATION = "message_received";
    private NotificationObserver notificationObserver;

    public NotificationBroadcastReceiver(NotificationObserver notificationObserver) {
        this.notificationObserver = notificationObserver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationObserver.onNotificationReceive(intent);
    }

    public interface NotificationObserver {
        void onNotificationReceive(Intent intent);
    }
}
