package com.insight.insight.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
import com.insight.insight.common.Setting;
import com.insight.insight.data.coldstart.ColdStart;
import com.insight.insight.utils.WearableSendSync;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by CM on 3/5/15.
 */

/* This class creates an alarm that sends a sync message to the handheld
    for Google Fit's Activity and HeartRate data. It also sends the current days notif
    file to get the genre information
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    PendingIntent alarmIntent;
    AlarmManager alarm;

    /* Returns an alarm manager that will fire at 11:58 and ever day there after*/
    public AlarmManager setMidnightAlarmManager(Context context){
        Log.d("AlarmRCV", "Set alarm");
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context,0,intent,0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,58);
        calendar.set(Calendar.SECOND,0);


        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                alarmIntent
        );


        return alarm;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("AlarmRecv", "ALARM ");
        sendFitDataSync(context);
        profileUpdate();
    }

    /* This function sends the 3 messages to the handheld
        syncs:
            Google Fit Activity Data
            Google Fit HeartRate
        Sends current day's notification file to handheld have genre evaluated

     */
    private void sendFitDataSync(final Context context){
        final Date date = new Date();
        HandlerThread ht = new HandlerThread("HeartThread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        ht.start();
        Handler h = new Handler(ht.getLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
                GoogleApiClient mGoogleAPIClient = new GoogleApiClient.Builder(context)
                        .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(Bundle bundle) {
                                Log.d("Heart", "Connected");
                            }

                            @Override
                            public void onConnectionSuspended(int i) {

                            }
                        })
                        .addApi(Wearable.API).build();
                mGoogleAPIClient.blockingConnect(10, TimeUnit.SECONDS);
                /* Sync msg & time to handheld */
                WearableSendSync.sendSyncToDevice(mGoogleAPIClient, WearableSendSync.START_ACTV_SYNC, date);
                WearableSendSync.sendSyncToDevice(mGoogleAPIClient, WearableSendSync.START_HIST_SYNC, date);
                WearableSendSync.sendDailyNotifFileWrapper(mGoogleAPIClient);

            }
        });
    }

    /* If profile file doesn't exist, run coldstart
        if it does exist, start a thread to update the profile with the new files
     */
    private void profileUpdate(){
        File out = new File ( Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                + Setting.APP_FOLDER + "/" +  "profile");
        if (!out.exists()){
            try {
                ColdStart.createProfile();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            HandlerThread ht = new HandlerThread("UpdateProfile", android.os.Process.THREAD_PRIORITY_BACKGROUND);
            ht.start();
            Handler h = new Handler(ht.getLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    ColdStart.updateProfile();
                }
            });

        }
    }
}
