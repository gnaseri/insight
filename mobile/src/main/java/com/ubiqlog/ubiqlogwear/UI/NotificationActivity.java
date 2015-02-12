package com.ubiqlog.ubiqlogwear.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ubiqlog.ubiqlogwear.R;
import com.ubiqlog.ubiqlogwear.Services.NotificationListener;


public class NotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        final Intent settingsIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Button launchSettings = (Button) findViewById(R.id.auth_btn);
        launchSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(settingsIntent);
            }
        });
        startService(new Intent(this, NotificationListener.class));
        Toast.makeText(this, "Notification Listener Started", Toast.LENGTH_SHORT).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification, menu);
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
