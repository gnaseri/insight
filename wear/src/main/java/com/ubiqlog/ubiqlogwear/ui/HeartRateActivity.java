package com.ubiqlog.ubiqlogwear.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.ubiqlog.ubiqlogwear.R;
import com.ubiqlog.ubiqlogwear.utils.WearableSendSync;

public class HeartRateActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, DataApi.DataListener {

    private static final String TAG = HeartRateActivity.class.getSimpleName();

    private static final String SEND_KEY = "com.example.data";

    private GoogleApiClient mGoogleAPIClient;
    private TextView tv;
    private Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);

        mGoogleAPIClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        sendBtn = (Button) findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SyncTask().execute();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(mGoogleAPIClient, this);
        mGoogleAPIClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleAPIClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Connected");
        //add listener
        Wearable.DataApi.addListener(mGoogleAPIClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection Failed");

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "Received data");
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                //Data item changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/data") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    doAction(dataMap.getInt(SEND_KEY));
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    private void doAction(final int i) {
        Log.d(TAG, "Received: " + i);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(HeartRateActivity.this,"Received:" + i, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class SyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            WearableSendSync.sendSyncToDevice(mGoogleAPIClient);
            return null;
        }
    }
}