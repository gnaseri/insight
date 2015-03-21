package com.ubiqlog.ubiqlogwear.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ubiqlog.ubiqlogwear.R;
import com.ubiqlog.ubiqlogwear.Services.WearableListenerService;

public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_main_activity);
        Button auth_btn = (Button) findViewById(R.id.auth_btn);
        Button notif_btn = (Button) findViewById(R.id.notif_btn);
        Button appUsage_btn = (Button) findViewById(R.id.appusage_btn);
        Button testHR_btn = (Button) findViewById(R.id.hr_btn);
        Button activ_btn = (Button) findViewById(R.id.activ_btn);

        auth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyActivity.this, GooglePlayServicesActivity.class));

            }
        });
        notif_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyActivity.this,NotificationActivity.class));
            }
        });

        appUsage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        testHR_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MyActivity.this,HeartRateActivity.class));
                startService(new Intent(MyActivity.this, WearableListenerService.class));
            }
        });

        activ_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyActivity.this, ActivitySegmentActivity.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
