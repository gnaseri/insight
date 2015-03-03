package com.ubiqlog.ubiqlogwear.Services;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by User on 3/2/15.
 */
public class ActivitySensor {
    private static final String TAG = ActivitySensor.class.getSimpleName();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");

    public static void getDataInformation(GoogleApiClient mClient, DataReadRequest request){
        PendingResult <DataReadResult> pendingResult =
                Fitness.HistoryApi.readData(mClient,request);
        DataReadResult dataReadResult = pendingResult.await();

        //print info
        printReadResult(dataReadResult);



    }

    private static void printReadResult(DataReadResult dataReadResult){
        Log.d(TAG, "Printing results");
        Log.d(TAG, "Bucketsize: " + dataReadResult.getBuckets().size());

        for (Bucket bucket : dataReadResult.getBuckets()){
            List<DataSet> dataSets = bucket.getDataSets();
            for (DataSet dataSet: dataSets){
                    processDataSet(dataSet);
            }
        }
    }

    private static void processDataSet(DataSet dataSet){
        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.d(TAG, "Data Returned of type:" + dp.getDataType().getName());
            Log.d(TAG, "Data Point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field)
                        + "Type: " + dp.getValue(field).asActivity());
            }
        }
        Log.d(TAG, "-----------------");
    }
    public static DataReadRequest buildDataReadRequestPoints() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        long endTime = cal.getTimeInMillis();

        cal.add(Calendar.HOUR_OF_DAY, -cal.get(Calendar.HOUR_OF_DAY));
        cal.add(Calendar.MINUTE, -cal.get(Calendar.MINUTE));
        cal.add(Calendar.SECOND, -cal.get(Calendar.SECOND));
        long startTime = cal.getTimeInMillis();

        Log.i(TAG, "Range start:" + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest dataReadRequest = new DataReadRequest.Builder()

                .aggregate(DataType.TYPE_ACTIVITY_SEGMENT,DataType.AGGREGATE_ACTIVITY_SUMMARY)
                .bucketByActivitySegment(1, TimeUnit.MINUTES)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        return dataReadRequest;

    }
}
