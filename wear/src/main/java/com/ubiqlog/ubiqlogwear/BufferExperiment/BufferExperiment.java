package com.ubiqlog.ubiqlogwear.BufferExperiment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.ubiqlog.ubiqlogwear.core.DataAcquisitor;

/**
 * Created by User on 3/12/15.
 */
public class BufferExperiment extends WakefulBroadcastReceiver {
    DataAcquisitor mDataBuf;
    PendingIntent alarmIntent;
    AlarmManager alarm;

    public BufferExperiment(){
        mDataBuf = new DataAcquisitor(null,"BufferExperiment");
    }
    /* On Receiving an alarm, will add mock data into buffer. Buffer will be written every 20
       iterations. Currently buffer will receive an update every 5 seconds
     */

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BufferExp", "Received");
        Intent bufferService = new Intent(context,BufferService.class);
        startWakefulService(context, bufferService);

    }

    public AlarmManager setTestAlarmManager(Context context){
        Log.d("ALARMRCV", "Set alarm");
        Intent intent = new Intent(context, BufferExperiment.class);
        alarmIntent = PendingIntent.getBroadcast(context,0,intent,0);




        alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                5*1000,
                alarmIntent
        );

        return alarm;
    }
}
