package com.ubiqlog.ubiqlogwear.sensors;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

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
import com.ubiqlog.ubiqlogwear.R;

import java.util.Date;
import java.util.concurrent.TimeUnit;


/* This class will log steps using GoogleFitAPI.
   Currently, the watch does not have the sensors to
   get changeOfStep, perform activity recognition, or get speed
 */
public class ActivitySensor extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String LOG_TAG = ActivitySensor.class.getSimpleName();

    private ActivityDataHelper.StepList stepList;

    private TextView mTextView;
    private GoogleApiClient mFitnessClient;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Destroying ActivitySensor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_sensor);

        buildFitnessActivity();
        stepList = new ActivityDataHelper.StepList(this);


        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mFitnessClient != null) {
            mFitnessClient.connect();
        }

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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextView.setText("Connect account with handheld device");
            }
        });

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


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityDataHelper.Step newStep = new ActivityDataHelper.Step(val.asInt(), new Date());

                            //This method writes to file when walking gap conditions are met
                            stepList.insert(newStep);
                            if (mTextView != null) {
                                mTextView.setText(val.asInt() + " steps");

                            }
                        }
                    });
                }
                Log.d(LOG_TAG, "Detected datapoint field: " + field.getName());
                Log.d(LOG_TAG, "Detected datapoint value: " + val);
            }
        }
    }

}
