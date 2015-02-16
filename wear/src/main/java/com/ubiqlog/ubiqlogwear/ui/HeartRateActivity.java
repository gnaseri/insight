package com.ubiqlog.ubiqlogwear.ui;

import android.app.Activity;
import android.os.*;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ubiqlog.ubiqlogwear.R;
import com.ubiqlog.ubiqlogwear.sensors.HeartRate.HeartRate;

public class HeartRateActivity extends Activity {

    private static final String LOG_TAG = HeartRateActivity.class.getSimpleName();
    private static TextView hr;
    private HandlerThread mFitHandlerThread;
    private Handler mFitHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This builds a connection to google fit api and connects.
        HeartRate.setup(this);

        //Initialize and Start Handler Thread
        initializeHandler();

        //This will fetch HeartRateBPM from HistoryAPI for a week of activity
        HeartRate.getDataPoints(mFitHandler);


    }

    private void initializeHandler(){

        mFitHandlerThread = new HandlerThread("FitHandlerThread",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mFitHandlerThread.start();

        mFitHandler = new Handler(mFitHandlerThread.getLooper());

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
}
