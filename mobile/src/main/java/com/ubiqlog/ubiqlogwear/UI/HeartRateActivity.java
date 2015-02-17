package com.ubiqlog.ubiqlogwear.UI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.wearable.Wearable;
import com.ubiqlog.ubiqlogwear.Listeners.WearableDataLayer;
import com.ubiqlog.ubiqlogwear.R;
import com.ubiqlog.ubiqlogwear.Services.HeartRate;


/* This class will wait for a sync request from the wearable. Once it receives the sync request,
   it will send HeartRate History Data over
 */
public class HeartRateActivity extends Activity {

    static GoogleApiClient mWearableClient;
    Button sendBtn;
    WearableDataLayer wearableDataLayer;

    /* Build the client and setup a listener to await sync from wearable*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);
        wearableDataLayer = new WearableDataLayer();

        mWearableClient = WearableDataLayer.buildDataApiClient(this);
        mWearableClient.connect();

        Wearable.MessageApi.addListener(mWearableClient, wearableDataLayer);


    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.MessageApi.removeListener(mWearableClient, wearableDataLayer);
        mWearableClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWearableClient.connect();
        Wearable.MessageApi.addListener(mWearableClient,wearableDataLayer);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_heart_rate, menu);
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

    //Send the dataSet to the wearable
    public static void sendToWearable(DataSet dataSet){
        WearableDataLayer.sendData(mWearableClient, dataSet);

    }
    public static void fetchHeartDataSet(Context context){
        GoogleApiClient fitClient = HeartRate.buildFitClient(context);
        fitClient.connect();
        HandlerThread heartThread = new HandlerThread("Heartthread",android.os.Process.THREAD_PRIORITY_BACKGROUND);
        heartThread.start();
        Handler heartHandler = new Handler(heartThread.getLooper());
        HeartRate.getDataPoints(heartHandler,fitClient,new HeartRate.SyncRequestInterface() {
            @Override
            public void setDataSet(DataSet dataSet) {
                sendToWearable(dataSet);
            }
        });
    }

}
