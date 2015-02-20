package com.ubiqlog.ubiqlogwear.sensors;

import android.os.Parcel;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class NotificationReceiver extends WearableListenerService {
    private final String NOTIF_KEY = "com.insight.notif";
    private GoogleApiClient mGoogleApiClient;
    private final String TAG = this.getClass().getSimpleName();
    public NotificationReceiver() {
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        ConnectionResult connectionResult = mGoogleApiClient.blockingConnect(30, TimeUnit.SECONDS);
        if (!connectionResult.isSuccess()){
            Log.e(TAG," Failed to connect");
            return;
        }

        for (DataEvent event : events){
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                //Data item changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/notif") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    final byte[] bytes = dataMap.getByteArray(NOTIF_KEY);
                    Log.d(TAG, "Parcelable retrieved of size: " + bytes.length);
                    StatusBarNotification sbn = unMarshall(bytes);
                    Log.d(TAG, sbn.toString());


                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }


    }

    private StatusBarNotification unMarshall(byte[] bytes){
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes,0,bytes.length);
        parcel.setDataPosition(0);

        StatusBarNotification sbn = StatusBarNotification.CREATOR.createFromParcel(parcel);
        parcel.recycle();
        return sbn;
    }
}
