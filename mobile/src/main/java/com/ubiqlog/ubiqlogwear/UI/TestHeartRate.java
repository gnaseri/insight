package com.ubiqlog.ubiqlogwear.UI;

import android.app.Activity;
import android.os.*;
import android.view.Menu;
import android.view.MenuItem;

import com.ubiqlog.ubiqlogwear.R;
import com.ubiqlog.ubiqlogwear.Services.HeartRate;

public class TestHeartRate extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_heart_rate);
        HandlerThread ht = new HandlerThread("HR", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        ht.start();
        Handler h = new Handler(ht.getLooper());
        HeartRate.setup(this); // will print stuff to log
        HeartRate.getData(h);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_heart_rate, menu);
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
