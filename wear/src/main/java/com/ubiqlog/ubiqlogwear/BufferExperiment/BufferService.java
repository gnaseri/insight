package com.ubiqlog.ubiqlogwear.BufferExperiment;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Date;

/**
 * Created by User on 3/12/15.
 */
public class BufferService extends IntentService {


    public BufferService(){
        super("BufferService");
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String mockData = "Nonsense_Sensor,timeStamp:00:00:00,RandomMaterial"
                + new Date().toString();
        BufferExpBuffer.mDataBuffer.insert(mockData,false,20);
        Log.d("BuffExp", "size:" + BufferExpBuffer.mDataBuffer.getDataBuffer().size());
        BufferExperiment.completeWakefulIntent(intent);
    }



}
