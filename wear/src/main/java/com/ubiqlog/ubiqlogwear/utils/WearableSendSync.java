package com.ubiqlog.ubiqlogwear.utils;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by Cole Murray
 */

/* This class sends a message to the handheld to sync history
    Handheld will receive message and send data information back
 */
public class WearableSendSync  {
    private static final String TAG = WearableSendSync.class.getSimpleName();
    public static final String START_HIST_SYNC = "/start/HeartSync";
    public static final String START_ACTV_SYNC = "/start/ActvSync";

    public static void sendSyncToDevice(GoogleApiClient mGoogleApiClient, String key, Date date){
        Log.d(TAG, "Sending sync message");
        Collection<String> nodes = getNodes(mGoogleApiClient);
        for (String n : nodes){
            //sendSyncMessage(mGoogleApiClient, n, key);
            sendSyncDataItem(mGoogleApiClient,n,key, date);
        }
    }
    private static Collection<String> getNodes(GoogleApiClient mGoogleApiClient) {
        HashSet<String> results = new HashSet<String>();
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
        for (Node node : nodes.getNodes()) {
            results.add(node.getId());
        }
        return results;
    }

    private static void sendSyncMessage (GoogleApiClient mGoogleApiClient,String nodeId, String key){
        Wearable.MessageApi.sendMessage(
                mGoogleApiClient, nodeId, key, new byte[0]).setResultCallback(
                new ResultCallback<MessageApi.SendMessageResult>() {
                    @Override
                    public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                        if (!sendMessageResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Failed to send message with status code: "
                                    + sendMessageResult.getStatus().getStatusCode());
                        }
                    }
                }
        );
    }
    /* This function is used to send sync Request. It includes the time so the handheld can make the
    correct time call
     */
    private static void sendSyncDataItem (GoogleApiClient mGoogleApiClient, String nodeId, String key, Date date){

        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(key);
        putDataMapReq.getDataMap().putLong("time", date.getTime());

        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        //Send Data To wearable
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient,putDataReq);

    }

    public static void sendDailyNotifFileWrapper(GoogleApiClient mGoogleApiClient){
        Collection<String> nodes = getNodes(mGoogleApiClient);
        for (String n : nodes){
            sendDailyNotifFile(mGoogleApiClient, n);
        }
    }
    private static void sendDailyNotifFile(GoogleApiClient mGoogleApiClient, String nodeId){
        IOManager ioManager = new IOManager();
        File[] notifFileArr = ioManager.getLastFilesInDir("Notif", 1); //only get the last file
        File notifFile = notifFileArr[0];

        //Convert to byte arr
        byte [] bytes = ioManager.convertFileToBytes(notifFile);

        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/get/notifFile");
        putDataMapReq.getDataMap().putLong("time", new Date().getTime());
        putDataMapReq.getDataMap().putByteArray("NOTIF_FILE", bytes);

        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        //Send Data To wearable
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient,putDataReq);
    }

}

