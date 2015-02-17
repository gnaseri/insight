package com.ubiqlog.ubiqlogwear.Listeners;

/**
 * Created by User on 2/16/15.
 */

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.ubiqlog.ubiqlogwear.UI.HeartRateActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

public class WearableDataLayer implements MessageApi.MessageListener{
    private static String s = "YOOO";
    private static final String TAG = WearableDataLayer.class.getSimpleName();

    private static final String SEND_KEY = "com.example.data";
    private static final String SYNC_KEY = "/start/HistorySYNC";
    private static Context mContext;

    public static GoogleApiClient buildDataApiClient(Context context){
        mContext = context;
        final GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(TAG, "onConnected: " + connectionHint);
                        // Now you can use the Data Layer API

                    }
                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d(TAG, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG, "onConnectionFailed: " + result);
                    }
                })
                        // Request access only to the Wearable API
                .addApi(Wearable.API)
                .build();

        return mGoogleApiClient;

    }
    private static DataMap buildDataMap(DataSet dataSet){
        byte[] arr = serialize(dataSet);
        Log.d(TAG,"DataSet of size:" + arr.length);
        DataMap dataMap = new DataMap();
        dataMap.putByteArray(SEND_KEY,arr);
        return dataMap;
    }

    public static void sendData(GoogleApiClient mClient, DataSet dataSet){

        Log.d(TAG, "Sending data");
        Log.d(TAG, "Dataset as string: " + dataSet.toString());
        Log.d(TAG, "DataSet is size" + dataSet.getDataPoints().size());
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/data");
        putDataMapReq.getDataMap().putLong("time", new Date().getTime());
        putDataMapReq.getDataMap().putString(SEND_KEY, dataSet.toString());
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        //Send Data To wearable
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mClient,putDataReq);

    }

    private static byte[] serialize (DataSet dataSet){
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        try {
            ObjectOutputStream o = new ObjectOutputStream(bOut);
            o.writeObject(dataSet);
            o.close();

            Log.d(TAG, "In Serialize: bytes: " + bOut.size());

            return bOut.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(SYNC_KEY)) {
            Log.d(TAG, "Message Event received");
            HeartRateActivity.fetchHeartDataSet(mContext);
        }
    }
}