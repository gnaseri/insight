package com.ubiqlog.ubiqlogwear.sensors;

import android.hardware.SensorEvent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.os.IBinder;
import android.content.Intent;
import android.app.Service;
import android.util.Log;
import android.widget.Toast;

import com.ubiqlog.ubiqlogwear.core.DataAcquisitor;
import com.ubiqlog.ubiqlogwear.utils.JsonEncodeDecode;

import java.util.Date;

/**
 * Created by prajnashetty on 10/30/14.
 */

public class GyroSensor extends Service {
    private SensorManager sensorManager;
    private Sensor gyroSensor;
    private long lastUpdate;
    SensorEventListener listen;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sensorManager.registerListener(listen, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        lastUpdate = System.currentTimeMillis();

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Toast.makeText(getApplicationContext(), "Started Gyroscope Logging", Toast.LENGTH_SHORT).show();
        super.onCreate();

        listen = new SensorListen();
        sensorManager = (SensorManager) getApplicationContext()
                .getSystemService(SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE );
    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(listen);
        Toast.makeText(this, "Destroy Gyroscope Logging", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    public class SensorListen implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.accuracy == 3) {
                if (System.currentTimeMillis() > (lastUpdate + SensorConstants.GYROSCOPE_LOG_INTERVAL)) {

                    Date _currentDate = new Date();
                    String jsonString = JsonEncodeDecode.EncodeGyroscope(event.values[0],
                            event.values[1], event.values[2], _currentDate);
                    DataAcquisitor.dataBuffer.add(jsonString);
                    Log.i("Gyroscope-Logging", jsonString);
                    lastUpdate = System.currentTimeMillis();
                }
            }
        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

    }
}