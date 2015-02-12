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

import com.ubiqlog.ubiqlogwear.utils.JsonEncodeDecode;

import java.util.Date;

/**
 * Created by prajnashetty on 10/30/14.
 */

public class HeartRateSensor extends Service {
    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private long lastUpdate;
    SensorEventListener listen;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sensorManager.registerListener(listen, heartRateSensor, 2);
        lastUpdate = System.currentTimeMillis();

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Toast.makeText(getApplicationContext(), "Started Heart Rate Logging", Toast.LENGTH_SHORT).show();
        super.onCreate();

        listen = new SensorListen();
        sensorManager = (SensorManager) getApplicationContext()
                .getSystemService(SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE );
    }



    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(listen);
        Toast.makeText(this, "Destroy Heart Rate Logging", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    public class SensorListen implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            Date _currentDate = new Date();
            if (event.accuracy == 3) {
                String jsonString = JsonEncodeDecode.EncodeHeartRate(event.values[0], _currentDate);
               // DataAcquisitor.dataBuffer.add(jsonString);
                Log.i("HeartRate-Logging", jsonString);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

    }
}