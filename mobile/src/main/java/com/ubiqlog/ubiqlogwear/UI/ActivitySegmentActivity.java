package com.ubiqlog.ubiqlogwear.UI;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.ubiqlog.ubiqlogwear.R;
import com.ubiqlog.ubiqlogwear.Services.ActivitySensor;
import com.ubiqlog.ubiqlogwear.Util.GoogleFitConnection;

import java.util.Date;

public class ActivitySegmentActivity extends Activity {
    private Button syncBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_segment);

        syncBTN = (Button) findViewById(R.id.activ_btn);
        GoogleFitConnection gfc = new GoogleFitConnection(this);
        final GoogleApiClient mClient = gfc.buildFitClient();
        mClient.connect();

        syncBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivityInformation(mClient);
            }
        });
    }

    private void getActivityInformation(final GoogleApiClient mClient){
        HandlerThread h = new HandlerThread("Activ", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        h.start();
        Handler mHandler = new Handler(h.getLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ActivitySensor.getDataInformation(mClient, ActivitySensor.buildDataReadRequestPoints(new Date()));


            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_segment, menu);
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
