package com.ubiqlog.ubiqlogwear.services;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
import com.ubiqlog.ubiqlogwear.listeners.WearableDataLayer;

/**
 * Created by User on 2/10/15.
 */
public class NotificationSensor extends NotificationListenerService {
    private static final String TAG = NotificationSensor.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;

    private final String NOTIF_KEY = "com.insight.notif";


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn.getPackageName().equals("android")){
            return;
        }
        Log.d (TAG, "--Current Notification--");

        Log.d(TAG, "ID:" + sbn.getId() + "\t" + sbn.getNotification().tickerText
                + "\t" + sbn.getPackageName() + "\t"
                + sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE) + "\t"
                + sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT) + "\t"
                + sbn.getPostTime() +  "\t" + sbn.getNotification().when + "\t");
        Log.d(TAG, "-------------");


        sendNotificationsToWear(sbn,NOTIF_KEY);

       /* // show all currently active notifications
        Log.d(TAG, "=====ALL NOTIFICATIONS======");
        for (StatusBarNotification notif : getActiveNotifications()){

            Notification notification = notif.getNotification();
            Log.d(TAG, "Flags:" + notification.flags);
            // This isn't working.
            if ((notification.flags & notification.FLAG_LOCAL_ONLY) == notification.FLAG_LOCAL_ONLY){
                Log.d(TAG, "DEVICE ONLY");
            }
            Log.d(TAG, "ID:" + notif.getId() + "\t" + notif.getNotification().tickerText
                        + "\t" + sbn.getPackageName());
            Log.d(TAG, "T:" +
                     notif.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE)
                      + "ET:" +  notif.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT));

        }
        Log.d(TAG, "================="); */


    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d(TAG, "Notification Removed");
        Log.d(TAG, "ID:" + sbn.getId() + "\t" + sbn.getNotification().tickerText
                    + "\t" + sbn.getPackageName());
        Log.d(TAG, "-------------------");

    }

    private void buildGoogleAPIClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.d(TAG, "Connected");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener( new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        //Try reconnect
                        mGoogleApiClient.connect();
                    }
                })
                .build();
    }
    private void sendNotificationsToWear(StatusBarNotification sbn, String KEY_NAME){
        buildGoogleAPIClient();
        mGoogleApiClient.connect();

        WearableDataLayer.sendNotificationtoWear(mGoogleApiClient,sbn, KEY_NAME);
        //mGoogleApiClient.disconnect();
    }


}
