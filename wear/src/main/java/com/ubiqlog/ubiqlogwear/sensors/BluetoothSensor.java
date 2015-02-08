package com.ubiqlog.ubiqlogwear.sensors;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/* This class monitors connection between Handheld and Wearable */
public class BluetoothSensor extends WearableListenerService {
    private static final String LOG_TAG = BluetoothSensor.class.getSimpleName();
    private GoogleApiClient mClient;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "OnStart command called");
        if (mClient != null){
            mClient.connect();
            Log.d(LOG_TAG, "Connected");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.d(LOG_TAG, "Successful connect");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();
    }

    @Override
    public void onPeerConnected(Node peer) {
        Log.d(LOG_TAG, "Bluetooth connected");
    }

    @Override
    public void onPeerDisconnected(Node peer) {
        Log.d(LOG_TAG, "Bluetooth Disconnected");
    }
}
