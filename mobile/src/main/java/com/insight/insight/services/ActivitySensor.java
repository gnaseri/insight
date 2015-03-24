package com.insight.insight.services;

import android.content.Context;
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
import com.insight.insight.listeners.WearableDataLayer;
import com.insight.insight.util.CalendarUtil;
import com.insight.insight.util.GoogleFitConnection;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by User on 3/2/15.
 */
public class ActivitySensor {
    private static final String TAG = ActivitySensor.class.getSimpleName();
    private int previousValue = 0;


    public static class ActivityInformationRunnable implements Runnable{
        private GoogleApiClient mGoogleApiClient;
        private Context mcontext;
        private Date date;

        public ActivityInformationRunnable(GoogleApiClient mGoogleApiClient, Context context, Date date){
            this.mGoogleApiClient = mGoogleApiClient;
            this.mcontext = context;
            this.date = date;
        }
        @Override
        public void run() {
            GoogleFitConnection googleFitConnection = new GoogleFitConnection(mcontext);
            GoogleApiClient mFitClient = googleFitConnection.buildFitClient();
            mFitClient.connect();
            DataReadResult dr = getDataInformation(mFitClient, buildDataReadRequestPoints(date));
            //DataReadResult step_result = getDataInformation(mFitClient,buildStepRequest(date));
            DataReadResult step_result = getStepData(mFitClient,buildStepRequest(date));
            Log.d(TAG,"SENDING DATARESULTS");
            mFitClient.disconnect();
            mGoogleApiClient.connect();
            WearableDataLayer.sendDataResult(mGoogleApiClient, dr, WearableDataLayer.ACTV_HIST_KEY);
            WearableDataLayer.sendStepResult(mGoogleApiClient, step_result, WearableDataLayer.STEP_HIST_KEY);

        }
    }

    public static DataReadResult getDataInformation(GoogleApiClient mClient, DataReadRequest request){
        PendingResult <DataReadResult> pendingResult =
                Fitness.HistoryApi.readData(mClient,request);
        DataReadResult dataReadResult = pendingResult.await();
        printReadResult(dataReadResult);
        return dataReadResult;

    }

    public static DataReadResult getStepData (GoogleApiClient mClient, DataReadRequest request){
        PendingResult <DataReadResult> pendingResult =
                Fitness.HistoryApi.readData(mClient,request);
        DataReadResult dataReadResult = pendingResult.await();
        printStepResult(dataReadResult);
        return dataReadResult;
    }
    private static void printStepResult (DataReadResult dataReadResult){
        int stepCount = 0;
        DataSet dataSet = dataReadResult.getDataSet(DataType.TYPE_STEP_COUNT_DELTA);
        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.d(TAG, "Data Returned of type:" + dp.getDataType().getName());
            Log.d(TAG, "Data Point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: " + CalendarUtil.dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + CalendarUtil.dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
                stepCount += dp.getValue(field).asInt();
            }
        }
        Log.d(TAG, "StepCount: " + stepCount);
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
            Log.i(TAG, "\tStart: " + CalendarUtil.dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + CalendarUtil.dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field)
                        + "Type: " + dp.getValue(field).asActivity());
            }
        }
        Log.d(TAG, "-----------------");
    }
    public static DataReadRequest buildDataReadRequestPoints(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Long[] startEndTimes = CalendarUtil.getStartandEndTime(cal);

        DataReadRequest dataReadRequest = new DataReadRequest.Builder()

                .aggregate(DataType.TYPE_ACTIVITY_SEGMENT,DataType.AGGREGATE_ACTIVITY_SUMMARY)
                .bucketByActivitySegment(1, TimeUnit.MINUTES)
                .setTimeRange(startEndTimes[0], startEndTimes[1], TimeUnit.MILLISECONDS)
                .build();
        return dataReadRequest;

    }

    public static DataReadRequest buildStepRequest(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Long[] startEndTimes = CalendarUtil.getStartandEndTime(cal);

        DataReadRequest dataReadRequest = new DataReadRequest.Builder()
                .read(DataType.TYPE_STEP_COUNT_DELTA)
                //.aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                //.bucketByActivitySegment(1, TimeUnit.DAYS)
                .setTimeRange(startEndTimes[0],startEndTimes[1], TimeUnit.MILLISECONDS)
                .build();
        return dataReadRequest;
    }


}
