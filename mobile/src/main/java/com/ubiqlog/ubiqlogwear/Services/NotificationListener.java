package com.ubiqlog.ubiqlogwear.Services;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by User on 2/10/15.
 */
public class NotificationListener extends NotificationListenerService {
    private static final String LOG_TAG = NotificationListener.class.getSimpleName();


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d (LOG_TAG, "--Current Notification--");

        Log.d(LOG_TAG, "ID:" + sbn.getId() + "\t" + sbn.getNotification().tickerText
                + "\t" + sbn.getPackageName());
        Log.d(LOG_TAG, "-------------");

        // show all currently active notifications
        Log.d(LOG_TAG, "=====ALL NOTIFICATIONS======");
        for (StatusBarNotification notif : getActiveNotifications()){
            Log.d(LOG_TAG, "ID:" + notif.getId() + "\t" + notif.getNotification().tickerText
                        + "\t" + sbn.getPackageName());

        }
        Log.d(LOG_TAG, "=================");


    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d(LOG_TAG, "Notification Removed");
        Log.d(LOG_TAG, "ID:" + sbn.getId() + "\t" + sbn.getNotification().tickerText
                    + "\t" + sbn.getPackageName());
        Log.d(LOG_TAG, "-------------------");

    }
}
