package com.ubiqlog.ubiqlogwear.sensors;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.ubiqlog.ubiqlogwear.core.DataAcquisitor;
import com.ubiqlog.ubiqlogwear.utils.CSVEncodeDecode;

import java.util.Date;

/**
 * Created by prajnashetty on 10/30/14.
 */

public class BatterySensor extends Service {
    public static final String LOG_TAG = BatterySensor.class.getSimpleName();

    private Handler mHandler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable doBatteryLogging = new Runnable() {
        public void run() {
            readSensor();
            mHandler.postDelayed(doBatteryLogging, SensorConstants.BATTERY_LOG_INTERVAL);
        }
    };

    private void readSensor() {
        Log.d(LOG_TAG,"Reading from battery sensor");

        //Register for the battery changed
        IntentFilter filter = new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED);
        // return value contains batteries status
        Intent batteryStatus = this.registerReceiver(null,filter);


        // Is the battery charging
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        boolean isCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING) ||
                ( status == BatteryManager.BATTERY_STATUS_FULL);

        // Current level of battery
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        // highest level for battery
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);


        Date currentDate = new Date();

        String encoded =
                CSVEncodeDecode.encodeBattery(level, isCharging, currentDate);

        DataAcquisitor.dataBuffer.add(encoded);

        Log.d(LOG_TAG, encoded);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Battery-Logging", "--- onStart");
        // May not have intent if service was killed or restarted
        if (intent != null){
            readSensor();
            mHandler.postDelayed(doBatteryLogging, SensorConstants.BATTERY_LOG_INTERVAL);
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        //Create a new Thread which also has a Looper Object
        HandlerThread thread = new HandlerThread("BatteryThread",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // create our handler using the looper from thread
        mHandler = new Handler(thread.getLooper());

        Toast.makeText(this, "Start Battery Log", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "Stopped Battery Logging");
        mHandler.removeCallbacks(doBatteryLogging);
        Toast.makeText(this, "Stopped Battery Logging", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

}