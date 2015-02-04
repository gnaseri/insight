package com.ubiqlog.ubiqlogwear.core;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.ubiqlog.ubiqlogwear.common.Setting;
import com.ubiqlog.ubiqlogwear.utils.IOManager;

public class DataAggregator extends Service {
    public static String LOG_TAG = DataAggregator.class.getSimpleName();
    private final int minBuffSize = 20;

    IOManager datalogger;
    private Handler mHandler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable doDataAggregation = new Runnable() {
        public void run() {
            manageDataAcq(minBuffSize);
            mHandler.postDelayed(doDataAggregation,Setting.SAVE_FILE_WAIT_INTERVAL);
        }
    };

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "--- onCreate");
        datalogger = new IOManager();
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(doDataAggregation);
        manageDataAcq(0);
        Log.d(LOG_TAG, "--- onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "--- onStartCommand");
        if (intent != null){
            manageDataAcq(minBuffSize);
            mHandler.postDelayed(doDataAggregation, Setting.SAVE_FILE_WAIT_INTERVAL);
        }

        return START_STICKY;
    }


    private void manageDataAcq(int minimumSize) {
        if (DataAcquisitor.dataBuffer.size() > minimumSize) {
            //Write data to file
            datalogger.logData(this,DataAcquisitor.dataBuffer);
            //Clear buffer
            DataAcquisitor.dataBuffer.clear();
        }

    }

}