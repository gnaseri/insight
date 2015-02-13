package com.ubiqlog.ubiqlogwear.sensors;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.ubiqlog.ubiqlogwear.ui.HeartRateActivity;

import java.util.Date;

/**
 * Created by prajnashetty on 10/30/14.
 */

public class HeartRateSensor extends Service {
    private static final String LOG_TAG = HeartRateSensor.class.getSimpleName();
    private SensorManager mSensorManager;
    private Sensor heartRateSensor;
    private int READCOUNT = 0;
    private Float avgBPM;
    SensorEventListener listen;

    private Handler mHandler;

    private Runnable processSensor = new Runnable() {
        @Override
        public void run() {
            mSensorManager.registerListener(listen, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
            mHandler.postDelayed(processSensor, 600000);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            mHandler.post(processSensor);
            //mSensorManager.registerListener(listen, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Toast.makeText(getApplicationContext(), "Started Heart Rate Logging", Toast.LENGTH_SHORT).show();
        super.onCreate();
        HandlerThread ht = new HandlerThread("HeartThread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        ht.start();
        mHandler = new Handler(ht.getLooper());
        listen = new SensorListen();
        mSensorManager = (SensorManager) getApplicationContext()
                .getSystemService(SENSOR_SERVICE);
        heartRateSensor = mSensorManager.getDefaultSensor(65562); //
    }


    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(listen);
        Toast.makeText(this, "Destroy Heart Rate Logging", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    public class SensorListen implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            Date _currentDate = new Date();
            float heartBPM = event.values[0];
            if (heartBPM > 0) {

                if (avgBPM == null) {
                    avgBPM = heartBPM;
                } else
                    avgBPM = (avgBPM + heartBPM) / 2;

                HeartRateActivity.updateValues(avgBPM);

            }
            Log.d(LOG_TAG, "HeartBPM: " + heartBPM + "\t" + "Accur:" + event.accuracy);
            READCOUNT++;
            if (READCOUNT > 500) {
                Log.d(LOG_TAG, "RESET OF COUNT");
                mSensorManager.unregisterListener(listen);
                READCOUNT = 0;

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

    }
}