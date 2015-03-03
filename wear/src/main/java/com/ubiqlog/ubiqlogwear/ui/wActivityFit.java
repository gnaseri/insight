package com.ubiqlog.ubiqlogwear.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
import com.ubiqlog.ubiqlogwear.R;
import com.ubiqlog.ubiqlogwear.utils.WearableSendSync;

import java.util.concurrent.TimeUnit;

public class wActivityFit extends Activity {
    private static final String TAG = wActivityFit.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_w_activity_fit);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new SyncFitActivityData().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_w_activity_fit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class SyncFitActivityData extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(wActivityFit.this)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            Log.d(TAG, "Connected");
                        }

                        @Override
                        public void onConnectionSuspended(int i) {

                        }
                    })
                    .addApi(Wearable.API)
                    .build();
            mGoogleApiClient.blockingConnect(10, TimeUnit.SECONDS);
            if (mGoogleApiClient.isConnected()){
                WearableSendSync.sendSyncToDevice(mGoogleApiClient,
                                                    WearableSendSync.START_ACTV_SYNC);
            }
            return null;

        }
    }
}
