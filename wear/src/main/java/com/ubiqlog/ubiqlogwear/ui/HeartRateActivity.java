package com.ubiqlog.ubiqlogwear.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ubiqlog.ubiqlogwear.R;
import com.ubiqlog.ubiqlogwear.sensors.HeartRateSensor;

public class HeartRateActivity extends Activity {

    public static void updateValues(float heartBPM){
        if (hr != null){
            hr.setText("heart:" + heartBPM);
        }
    }

    private static final String LOG_TAG = HeartRateActivity.class.getSimpleName();
    private static TextView hr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG,"Starting heartService");
        startService(new Intent(this, HeartRateSensor.class));
        setContentView(R.layout.activity_heart_rate);
        hr = (TextView) findViewById(R.id.hr_tv);
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
