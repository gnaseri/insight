package com.ubiqlog.ubiqlogwear.sensors;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;

import com.ubiqlog.ubiqlogwear.core.DataAcquisitor;
import com.ubiqlog.ubiqlogwear.utils.JSONUtil;

import java.util.Date;

/**
 * Created by User on 2/22/15.
 */
public class BatterySensor extends Service {
    IntentFilter mIntentFilter;
    BatteryReceiver batteryReceiver;
    DataAcquisitor mDataBuffer;
    private final String TAG = this.getClass().getSimpleName();
    private int lastVal;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null){
            this.registerReceiver(batteryReceiver,mIntentFilter);
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        batteryReceiver = new BatteryReceiver();
        mDataBuffer = new DataAcquisitor(this,this.getClass().getSimpleName());

    }

    @Override
    public void onDestroy() {
        mDataBuffer.flush(true);
        unregisterReceiver(batteryReceiver);
        super.onDestroy();

    }

    private class BatteryReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent batteryStatus) {
            if (batteryStatus.getAction().equalsIgnoreCase(Intent.ACTION_BATTERY_CHANGED)){
                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                if (level == lastVal){
                    return;
                }
                lastVal = level;

                boolean isCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING) ||
                        ( status == BatteryManager.BATTERY_STATUS_FULL);

                if (level % 5 == 0){
                    //store in buff
                    Log.d(TAG,"Level:" + level);
                    Log.d(TAG, "Charging:" + isCharging);
                    String encoded = JSONUtil.encodeBattery(level, isCharging, new Date());
                    mDataBuffer.insert(encoded,true);
                }

            }

        }
    }
}
