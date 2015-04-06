package com.insight.insight.services;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.insight.insight.util.CalendarUtil;
import com.insight.insight.util.GoogleFitConnection;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by CM on 2/15/15.
 */
public class HeartRateSensor {

    public interface SyncRequestInterface {
        public void setDataSet(DataSet dataSet);
    }

    private static final String TAG = HeartRateSensor.class.getSimpleName();
    private static GoogleApiClient mFitClient;
    private static Context mContext;

    public static GoogleApiClient buildFitClient(Context context) {
        mContext = context;
        GoogleFitConnection gfc = new GoogleFitConnection(context);
        mFitClient = gfc.buildFitClient();

        return mFitClient;
    }
    /* This runnable retrieves heartDataSet from History API
        It sends DataSet to HeartActivity to be sent to wearable
     */
    public static class HeartResultsRunnable implements Runnable {
        private GoogleApiClient mGoogleApiClient;
        private SyncRequestInterface sync;
        private Date date;

        public HeartResultsRunnable(GoogleApiClient googleApiClient, SyncRequestInterface sync
                ,Date date) {
            this.mGoogleApiClient = googleApiClient;
            this.sync = sync;
            this.date = date;
        }

        @Override
        public void run() {
            PendingResult<DataReadResult> pendingResult =
                    Fitness.HistoryApi.readData(mGoogleApiClient, buildDataReadRequestPoints());

            DataReadResult dataReadResult = pendingResult.await();
            DataSet dataSet = dataReadResult.getDataSet(DataType.TYPE_HEART_RATE_BPM);
            sync.setDataSet(dataSet);
            dumpHeartDataPoints(dataSet);
        }
    }


    public static void getDataPoints(Handler h, GoogleApiClient client, SyncRequestInterface sync
                ,Date date) {
        h.post(new HeartResultsRunnable(client,sync, date));
    }

    private static DataReadRequest buildDataReadRequestPoints() {
        Calendar cal = Calendar.getInstance();
        Long[] startEnd = CalendarUtil.getStartandEndTime(cal);

        DataReadRequest dataReadRequest = new DataReadRequest.Builder()
                .read(DataType.TYPE_HEART_RATE_BPM)
                .setTimeRange(startEnd[0], startEnd[1], TimeUnit.MILLISECONDS)
                .build();
        return dataReadRequest;

    }


    private static void dumpHeartDataPoints(DataSet dataSet) {
        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.d(TAG, "Data Returned of type:" + dp.getDataType().getName());
            Log.d(TAG, "Data Point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: " + CalendarUtil.dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + CalendarUtil.dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
            }
        }
    }

}



