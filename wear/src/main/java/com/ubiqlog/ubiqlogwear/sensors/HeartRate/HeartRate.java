package com.ubiqlog.ubiqlogwear.sensors.HeartRate;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.ubiqlog.ubiqlogwear.utils.GoogleFitConnection;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by User on 2/16/15.
 */
public class HeartRate {
    private static final String TAG = HeartRate.class.getSimpleName();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");
    private static GoogleApiClient mFitClient;

    static final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            DataReadResult dataReadResult =
                    Fitness.HistoryApi.readData(mFitClient, buildDataReadRequest())
                            .await(1, TimeUnit.MINUTES);
            if (dataReadResult.getBuckets().size() > 0) {
                Log.d(TAG, "Returned buckets: " + dataReadResult.getBuckets().size());
                for (Bucket bucket : dataReadResult.getBuckets()) {
                    List<DataSet> dataSets = bucket.getDataSets();
                    for (DataSet ds : dataSets) {
                        dumpDataSet(ds);
                    }
                }
            }
        }
    };

    public static void setup(Context context) {
        GoogleFitConnection gfc = new GoogleFitConnection(context);
        mFitClient = gfc.buildFitClient();

        mFitClient.connect();

        Log.d(TAG, "Is Connected");
    }

    public static void getData(Handler h) {
        h.post(runnable);
    }

    public static void getDataPoints(Handler h) {
        h.post(getHeartDataResult);
    }

    private static DataReadRequest buildDataReadRequestPoints() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        Log.i(TAG, "Range start:" + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest dataReadRequest = new DataReadRequest.Builder()
                .read(DataType.TYPE_HEART_RATE_BPM)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        return dataReadRequest;

    }

    private static Runnable getHeartDataResult = new Runnable() {
        @Override
        public void run() {
            PendingResult<DataReadResult> pendingResult =
                    Fitness.HistoryApi.readData(mFitClient, buildDataReadRequestPoints());

            DataReadResult dataReadResult = pendingResult.await();
            DataSet dataSet = dataReadResult.getDataSet(DataType.TYPE_HEART_RATE_BPM);
            dumpHeartDataPoints(dataSet);
        }
    };

    private static void dumpHeartDataPoints(DataSet dataSet) {
        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.d(TAG, "Data Returned of type:" + dp.getDataType().getName());
            Log.d(TAG, "Data Point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
            }
        }
    }

    private static DataReadRequest buildDataReadRequest() {
        //Get info from a week ago

        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();


        Log.i(TAG, "Range start:" + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .aggregate(DataType.TYPE_HEART_RATE_BPM, DataType.AGGREGATE_HEART_RATE_SUMMARY)
                .bucketByTime(1, TimeUnit.HOURS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        return readRequest;
    }

    private static void dumpDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
            }
        }
    }
}
