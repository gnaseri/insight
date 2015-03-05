package com.ubiqlog.ubiqlogwear.Services;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.ubiqlog.ubiqlogwear.Listeners.WearableDataLayer;

import java.util.Date;

/**
 * Created by User on 2/25/15.
 */
public class WearableListenerService extends com.google.android.gms.wearable.WearableListenerService {
    private final String TAG = this.getClass().getSimpleName();
    private static GoogleApiClient mGoogleApiClient;
    private static final String HEART_SYNC_KEY = "/start/HeartSync";
    private static final String ACTV_SYNC_KEY = "/start/ActvSync";

    @Override
    public void onDataChanged(DataEventBuffer events) {
        Log.d (TAG, "On data Changed");
        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();

                if (item.getUri().getPath().compareTo(HEART_SYNC_KEY) == 0){
                    Log.d(TAG, "HEART SYNC REQ");
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    Date date = new Date(dataMap.getLong("time"));
                    buildGoogleAPIClient();
                    mGoogleApiClient.connect();
                    fetchHeartDataSet(this, date);

                }
                if (item.getUri().getPath().compareTo(ACTV_SYNC_KEY) == 0){
                    Log.d(TAG,"ACTIVITY SYNC REQ");
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    Date date = new Date(dataMap.getLong("time"));
                    buildGoogleAPIClient();
                    mGoogleApiClient.connect();

                    Handler actvHandler = buildActivityHandler();
                    actvHandler.post(new ActivitySensor.ActivityInformationRunnable(mGoogleApiClient, this, date));
                }
            }
        }

    }

    //Send the dataSet to the wearable
    public static void sendToWearable(DataSet dataSet) {
        WearableDataLayer.sendData(mGoogleApiClient, dataSet, WearableDataLayer.HEART_HIST_KEY);

    }

    /*This function builds a fitClient after receiving a sync Request from the wearable
        It starts a thread to fetch the heartDataSet and calls to sendToWearable, sending
        the dataset to the wearable
     */
    public static void fetchHeartDataSet(Context context, Date date) {
        GoogleApiClient fitClient = HeartRateSensor.buildFitClient(context);
        fitClient.connect();

        Handler heartHandler = buildHandler();

        HeartRateSensor.getDataPoints(heartHandler, fitClient, new HeartRateSensor.SyncRequestInterface() {
            @Override
            public void setDataSet(DataSet dataSet) {
                sendToWearable(dataSet);
            }
        }, date);
    }

    private static Handler buildHandler() {
        HandlerThread heartThread = new HandlerThread("Heartthread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        heartThread.start();
        Handler heartHandler = new Handler(heartThread.getLooper());
        return heartHandler;
    }

    private static Handler buildActivityHandler() {
        HandlerThread actvThread = new HandlerThread("Actvthread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        actvThread.start();
        Handler actvHandler = new Handler(actvThread.getLooper());
        return actvHandler;
    }


    private void buildGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.d(TAG, "Connected");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        //Try reconnect
                        mGoogleApiClient.connect();
                    }
                })
                .build();
    }
}
