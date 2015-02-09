package com.ubiqlog.ubiqlogwear.sensors;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Check this code: http://weimenglee.blogspot.com/2014/03/android-tip-notification-listener.html
 * Created by rawassizadeh on 12/19/14.
 */
public class NotificationSensor extends AccessibilityService{
    private boolean isInit;
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED){
            Log.d("NotificationSensor", "UPDATE");

        }
    }

    @Override
    protected void onServiceConnected() {
        if (isInit){
            return;
        }
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        setServiceInfo(info);
        isInit = true;

    }

    @Override
    public void onInterrupt() {
        isInit = false;

    }
}

/*
public class NotificationSensor extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        //---show current notification---
        Log.i("", "---A notification appears---");
        Log.i("","ID :" + sbn.getId() + "\t" +
                sbn.getNotification().tickerText + "\t" +
                sbn.getPackageName());
        Log.i("","--------------------------");

        //---show all active notifications---
        Log.i("","===All Notifications===");
        for (StatusBarNotification notif :
                this.getActiveNotifications()) {
            Log.i("","ID :" + notif.getId() + "\t" +
                    notif.getNotification().tickerText + "\t" +
                    notif.getPackageName());
        }
        Log.i("","=======================");
    }

    @Override
    public void onNotificationRemoved(
            StatusBarNotification sbn) {
        Log.i("","---a notification has been removed---");
        Log.i("","ID :" + sbn.getId() + "\t" +
                sbn.getNotification().tickerText + "\t" +
                sbn.getPackageName());
        Log.i("","--------------------------");

    }
} */
