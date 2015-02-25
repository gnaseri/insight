package com.ubiqlog.ubiqlogwear.sensors;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
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

import java.util.Date;
import java.util.List;

/**
 * Created by User on 2/21/15.
 */

/* This class logs Bluetooth and Notifications. Only one wearableListener service is allowed
   per application
 */
public class NotificationSensor extends WearableListenerService {
    private final String NOTIF_KEY = "com.insight.notif";
    private static final String LOG_TAG = NotificationSensor.class.getSimpleName();
    private GoogleApiClient mClient;

    private DataAcquisitor mBTBuffer;
    private DataAcquisitor mNotifBuffer;

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
        String encoded = JSONUtil.encodeBT("Connected", new Date());
        Log.d(LOG_TAG,encoded);
        mBTBuffer.insert(encoded);
    }

    @Override
    public void onPeerDisconnected(Node peer) {
        Log.d(LOG_TAG, "Bluetooth Disconnected");
        String encoded = JSONUtil.encodeBT("Disconnected", new Date());
        Log.d(LOG_TAG,encoded);
        mBTBuffer.insert(encoded);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(LOG_TAG, "On data changed");
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);

        for (DataEvent event : events){
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                //Data item changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/notif") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    final byte[] bytes = dataMap.getByteArray(NOTIF_KEY);
                    Log.d(LOG_TAG, "Parcelable retrieved of size:" + bytes.length);
                    NotificationParcel np = unMarshall(bytes);
                    String encoded = JSONUtil.encodeNotification(np);
                    Log.d(LOG_TAG, encoded);
                    mNotifBuffer.insert(encoded);


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
}
