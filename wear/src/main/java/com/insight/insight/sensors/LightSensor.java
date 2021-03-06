package com.insight.insight.sensors;

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

import com.insight.insight.common.Setting;
import com.insight.insight.core.DataAcquisitor;
import com.insight.insight.data.JSONUtil;
import com.insight.insight.data.SemanticTempCSVUtil;

import java.util.Date;

/**
 * Created by CM
 */

/**
 * Class will take 5 lux readings every SensorConstant.LIGHT_SENSOR_INTERVAL
 * and write to file
 */

public class LightSensor extends Service implements SensorEventListener {
    private static final String LOG_TAG = LightSensor.class.getSimpleName();
    private Sensor mLight;
    private SensorManager mSensorManager;
    private Handler mHandler;
    int count;
    float totalSum;

    private DataAcquisitor mDataBuffer;
    private DataAcquisitor mSA_lightBuffer;

    public LightSensor() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        count = 0;
        totalSum = 0f;
        mDataBuffer = new DataAcquisitor(this, Setting.dataFilename_LightSensor);
        mSA_lightBuffer = new DataAcquisitor(this, "SA/" + Setting.dataFilename_LightSensor);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        Log.d(LOG_TAG, "Light sensor started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            //SensorDelayNormal is 200,000 ms
            HandlerThread ht = new HandlerThread("LightThread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
            ht.start();
            mHandler = new Handler(ht.getLooper());
            mHandler.post(activateLightListener);

        }
        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Light sensor returns one value

        //After taking 3 readings, unregister the listener
        //Call handler again in SensorConstant.Interval
        if (count >= SensorConstants.LIGHT_SAMPLE_AMNT) {
            mSensorManager.unregisterListener(this);

            Date date = new Date();
            float avg = totalSum / count;
            //Encode the lux value and date
            String encoded = JSONUtil.encodeLight(avg, date);
            Log.d(LOG_TAG, encoded);

            //add encoded string to buffer
            mDataBuffer.insert(encoded, true, Setting.bufferMaxSize);
            mDataBuffer.flush(true);

            String encoded_SA = SemanticTempCSVUtil.encodeLight(avg, date);
            mSA_lightBuffer.insert(encoded_SA, true, Setting.bufferMaxSize);
            mSA_lightBuffer.flush(true);
            totalSum = 0;
            count = 0;

            mHandler.postDelayed(activateLightListener, SensorConstants.LIGHT_SENSOR_INTERVAL);

        } else {
            float lux = event.values[0];
            totalSum += lux;
            count++;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        mDataBuffer.flush(true);
        mSA_lightBuffer.flush(true);
        mSensorManager.unregisterListener(this);
        Log.d(LOG_TAG, "Light sensor stopped");
        super.onDestroy();
    }

    private Runnable activateLightListener = new Runnable() {
        @Override
        public void run() {
            mSensorManager.registerListener(LightSensor.this, mLight, SensorManager.SENSOR_DELAY_FASTEST);
        }
    };
}