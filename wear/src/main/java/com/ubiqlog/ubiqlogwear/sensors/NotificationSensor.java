package com.ubiqlog.ubiqlogwear.sensors;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.ubiqlog.ubiqlogwear.common.NotificationParcel;
import com.ubiqlog.ubiqlogwear.core.DataAcquisitor;
import com.ubiqlog.ubiqlogwear.utils.JSONUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by User on 2/21/15.
 */

/* This class logs Bluetooth and Notifications. Only one wearableListener service is allowed
   per application
 */
public class NotificationSensor extends WearableListenerService {
    private final String NOTIF_KEY = "com.insight.notif";
    private final String HEART_KEY = "com.insight.heartrate";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");

    private static final String TAG = NotificationSensor.class.getSimpleName();
    private GoogleApiClient mClient;

    private DataAcquisitor mBTBuffer;
    private DataAcquisitor mNotifBuffer;
    private DataAcquisitor mHeartBuffer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mClient != null){
            mClient.connect();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBTBuffer = new DataAcquisitor(this,"Bluetooth");
        mNotifBuffer = new DataAcquisitor(this,"Notif");
        mHeartBuffer = new DataAcquisitor(this,"HeartRate");
        mClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.d(TAG, "Successful connect");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();
    }

    @Override
    public void onPeerConnected(Node peer) {
        Log.d(TAG, "Bluetooth connected");
        String encoded = JSONUtil.encodeBT("Connected", new Date());
        Log.d(TAG,encoded);
        mBTBuffer.insert(encoded,true);
    }

    @Override
    public void onPeerDisconnected(Node peer) {
        Log.d(TAG, "Bluetooth Disconnected");
        String encoded = JSONUtil.encodeBT("Disconnected", new Date());
        Log.d(TAG,encoded);
        mBTBuffer.insert(encoded,true);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "On data changed");
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);

        for (DataEvent event : events){
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                //Data item changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/notif") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    final byte[] bytes = dataMap.getByteArray(NOTIF_KEY);
                    Log.d(TAG, "Parcelable retrieved of size:" + bytes.length);
                    NotificationParcel np = unMarshall(bytes);
                    String encoded = JSONUtil.encodeNotification(np);
                    Log.d(TAG, encoded);
                    mNotifBuffer.insert(encoded,true);


                }
                if (item.getUri().getPath().compareTo("/data") == 0){
                    Log.d(TAG, "Heartrate");
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    final byte[] bytes = dataMap.getByteArray(HEART_KEY);
                    Log.d(TAG, "Parcelable retrieved of size:" + bytes.length);
                    DataSet dataSet = unMarshallHeartData(bytes);
                   // dumpHeartDataPoints(dataSet);
                    ArrayList <String> encodedDataSet = encodeDataSet(dataSet);
                    writeHeartRateValues(encodedDataSet);
                    //String encoded = JSONUtil.encodeNotification(np);


                }

            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }


    }

    private NotificationParcel unMarshall(byte[] bytes){
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes,0,bytes.length);
        parcel.setDataPosition(0);

        NotificationParcel np = NotificationParcel.CREATOR.createFromParcel(parcel);
        parcel.recycle();
        return np;
    }
    private DataSet unMarshallHeartData (byte[] bytes){
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes,0,bytes.length);
        parcel.setDataPosition(0);

        DataSet dataSet = DataSet.CREATOR.createFromParcel(parcel);
        parcel.recycle();
        return dataSet;
    }

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

    private ArrayList<String> encodeDataSet(DataSet dataSet){
        ArrayList<String> encodedDp = new ArrayList<String>();
        for (DataPoint dp : dataSet.getDataPoints()){
            Date start = new Date(dp.getStartTime(TimeUnit.MILLISECONDS));

            Value bpm;
            for (Field field : dp.getDataType().getFields()){
                bpm = dp.getValue(field);
                Log.d(TAG, "BPM:" + bpm);
                String encoded = JSONUtil.encodeHeartRate(start,bpm.asFloat());
                encodedDp.add(encoded);
            }

            Log.d(TAG,"---------");
        }
        return encodedDp;
    }
    /* We do not want to append since heartrate values will return same values if called
        during day more than once
     */
    private void writeHeartRateValues(ArrayList<String> encoded){
        for (String s : encoded){
            mHeartBuffer.insert(s,false);
        }
        mHeartBuffer.flush(false);
    }

}
