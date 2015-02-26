package com.ubiqlog.ubiqlogwear.Services;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.ubiqlog.ubiqlogwear.Listeners.WearableDataLayer;

/**
 * Created by User on 2/25/15.
 */
public class WearableListenerService extends com.google.android.gms.wearable.WearableListenerService {
    private final String TAG = this.getClass().getSimpleName();
    private static GoogleApiClient mGoogleApiClient;
    private static final String SYNC_KEY = "/start/HistorySYNC";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        if (messageEvent.getPath().equals(SYNC_KEY)) {
            Log.d("WEAR", "SYNC REQUESTED");
            buildGoogleAPIClient();
            mGoogleApiClient.connect();
            fetchHeartDataSet(this);
            //mGoogleApiClient.disconnect();

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
    public static void fetchHeartDataSet(Context context) {
        GoogleApiClient fitClient = HeartRateSensor.buildFitClient(context);
        fitClient.connect();

        Handler heartHandler = buildHandler();

        HeartRateSensor.getDataPoints(heartHandler, fitClient, new HeartRateSensor.SyncRequestInterface() {
            @Override
            public void setDataSet(DataSet dataSet) {
                sendToWearable(dataSet);
            }
        });
    }

    private static Handler buildHandler() {
        HandlerThread heartThread = new HandlerThread("Heartthread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        heartThread.start();
        Handler heartHandler = new Handler(heartThread.getLooper());
        return heartHandler;
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
