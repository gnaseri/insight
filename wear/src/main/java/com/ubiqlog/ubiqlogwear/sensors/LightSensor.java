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

import com.ubiqlog.ubiqlogwear.core.DataAcquisitor;
import com.ubiqlog.ubiqlogwear.utils.CSVEncodeDecode;

import java.util.Date;

/**
 * Created by Cole
 */

/**
 *  Class will take 5 lux readings every SensorConstant.LIGHT_SENSOR_INTERVAL
    and write to file
 */

public class LightSensor extends Service implements SensorEventListener {
    private static final String LOG_TAG = LightSensor.class.getSimpleName();
    private Sensor mLight;
    private SensorManager mSensorManager;
    private Handler mHandler;
    int count;
    float avg;


    private DataAcquisitor mDataBuffer;

    public LightSensor() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        count = 0;
        mDataBuffer = new DataAcquisitor(this,this.getClass().getSimpleName());
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        Toast.makeText(this,"LightSens Logging Started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null){
            //SensorDelayNormal is 200,000 ms
            HandlerThread ht = new HandlerThread("LightThread",android.os.Process.THREAD_PRIORITY_BACKGROUND);
            ht.start();
            mHandler = new Handler(ht.getLooper());
            mHandler.post(activateLightListener);

        }
        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Light sensor returns one value

        //After taking 5 readings, unregister the listener
        //Call handler again in SensorConstant.Interval
        if (count > 5){
            mSensorManager.unregisterListener(this);
            count = 0;

            Date date = new Date();

            //Encode the lux value and date
            String encoded = CSVEncodeDecode.encodeLight(avg, date);
            Log.d(LOG_TAG, encoded);

            //add encoded string to buffer
            mDataBuffer.insert(encoded);
            mDataBuffer.flush();

            mHandler.postDelayed(activateLightListener,SensorConstants.LIGHT_SENSOR_INTERVAL);
        }

        float lux = event.values[0];
        avg = (avg + lux) / 2f;
        count++;



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(this);
        Log.d(LOG_TAG, "Light sensor stopped");
        super.onDestroy();
    }

    private Runnable activateLightListener = new Runnable() {
        @Override
        public void run() {
            mSensorManager.registerListener(LightSensor.this,mLight,SensorManager.SENSOR_DELAY_FASTEST);
        }
    };
}