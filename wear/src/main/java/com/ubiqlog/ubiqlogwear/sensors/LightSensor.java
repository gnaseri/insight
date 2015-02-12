package com.ubiqlog.ubiqlogwear.sensors;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.ubiqlog.ubiqlogwear.core.DataAcquisitor;
import com.ubiqlog.ubiqlogwear.utils.CSVEncodeDecode;

import java.util.Date;

/**
 * Created by prajnashetty on 10/30/14.
 */

public class LightSensor extends Service implements SensorEventListener {
    private static final String LOG_TAG = LightSensor.class.getSimpleName();
    private Sensor mLight;
    private SensorManager mSensorManager;

    private DataAcquisitor mDataBuffer;

    public LightSensor() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mDataBuffer = new DataAcquisitor(this,this.getClass().getSimpleName());
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        Toast.makeText(this,"LightSens Logging Started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null){
            //SensorDelayNormal is 200,000 ms
            mSensorManager.registerListener(this,mLight,SensorManager.SENSOR_DELAY_NORMAL);

        }
        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Light sensor returns one value
        float lux = event.values[0];
        Date date = new Date();

        //Encode the lux value and date
        String encoded = CSVEncodeDecode.encodeLight(lux, date);
        Log.d(LOG_TAG, encoded);

        // add encoded string to buffer
        mDataBuffer.insert(encoded);

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
}