package com.ubiqlog.ubiqlogwear.sensors;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;

import java.util.Date;
import java.util.concurrent.TimeUnit;


/* This class will log steps using GoogleFitAPI.
   Currently, the watch does not have the sensors to
   get changeOfStep, perform activity recognition, or get speed
 */
public class ActivitySensor extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String LOG_TAG = ActivitySensor.class.getSimpleName();

    private ActivityDataHelper.StepList stepList;

    public static GoogleApiClient mFitnessClient;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stepList.getmDataBuffer().flush(true);
        super.onDestroy();

    }

    @Override
    public void onCreate() {
        super.onCreate();

        buildFitnessActivity();
        stepList = new ActivityDataHelper.StepList(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "Started Activity Monitoring");
        if (mFitnessClient != null) {
            mFitnessClient.connect();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    private void buildFitnessActivity() {
        mFitnessClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.API)
                .addScope(Fitness.SCOPE_ACTIVITY_READ)
                .addScope(Fitness.SCOPE_BODY_READ_WRITE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(LOG_TAG, "Connected to Fitness API");
        invokeFitnessApi();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(LOG_TAG, "Connection failed: " + connectionResult.getErrorCode());

    }

    private void invokeFitnessApi() {
        setupSensorRequest();

    }

    private void setupSensorRequest() {
        SensorRequest req = new SensorRequest.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .setSamplingRate(1, TimeUnit.SECONDS)
                .build();

        PendingResult<Status> regResult =
                Fitness.SensorsApi.add(mFitnessClient, req, new DataSourceListener());

    }

    private class DataSourceListener implements OnDataPointListener {
        @Override
        public void onDataPoint(DataPoint dataPoint) {
            for (Field field : dataPoint.getDataType().getFields()) {
                final Value val = dataPoint.getValue(field);
                if (val != null) {


                    ActivityDataHelper.Step newStep = new ActivityDataHelper.Step(val.asInt(), new Date());

                    //This method writes to file when walking gap conditions are met
                    stepList.insert(newStep);

                    Log.d(LOG_TAG, "Detected datapoint field: " + field.getName());
                    Log.d(LOG_TAG, "Detected datapoint value: " + val);
                }
            }
        }

    }
}



