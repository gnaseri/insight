package com.insight.insight.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;

import com.insight.insight.R;
import com.insight.insight.alarm.AlarmReceiver;
import com.insight.insight.sensors.BatterySensor;
import com.insight.insight.sensors.LightSensor;
import com.insight.insight.ui.adapters.ListViewAdapter;
import com.insight.insight.utils.FeatureCheck;
import com.insight.insight.utils.IOManager;
import com.insight.insight.utils.MenuItems;
import com.insight.insight.utils.PredictionNotification;

/**
 * Created by Cole
 */

public class OldMainActivity extends Activity {


    public static String LOG_TAG = OldMainActivity.class.getSimpleName();

    private WearableListView listView;
    private BoxInsetLayout roundBack;

    IOManager ioManager = new IOManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO Remove after autostart created
        startAllServices();
        setContentView(R.layout.activity_main_wear);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                roundBack = (BoxInsetLayout) stub.findViewById(R.id.roundLayout);
                if (roundBack != null) {
                    MenuItems menuItems = new MenuItems(OldMainActivity.this);
                    //Start Alarm manager

                    listView = (WearableListView) roundBack.findViewById(R.id.wearable_list);
                    listView.setAdapter(new ListViewAdapter(OldMainActivity.this, menuItems.createMeListItems()));
                    listView.setClickListener(new WearableListView.ClickListener() {
                        @Override
                        public void onClick(WearableListView.ViewHolder viewHolder) {

                            MainListItem listItem = (MainListItem) viewHolder.itemView.getTag();
                            if (!listItem.getTitle().equals("Me") &&
                                    !listItem.getTitle().equals("System"))
                                startActivity(listItem.getIntent(OldMainActivity.this));

                        }

                        @Override
                        public void onTopEmptyRegionClick() {

                        }
                    });
                }
            }
        });

        PredictionNotification pNotif = new PredictionNotification();
        pNotif.show(this, "Caution", "You are using battery lower than average!");

    }

    //TODO Integrate this into autostart
    private void startAllServices() {
        startService(new Intent(this, BatterySensor.class));
       // startService(new Intent(this, ActivitySensor.class));
        //TODO Activity Sensor have hooks applied and needs to be redone
        //Notification and Bluetooth autostart due to dataLayer
        // HeartRate needs hooks applied to activity
        if (FeatureCheck.hasLightFeature(this)) {
            startService(new Intent(this, LightSensor.class));
        }

        AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.setMidnightAlarmManager(OldMainActivity.this);
        //TODO Remove NotifExperiment
        //BufferExperiment bufferExperiment = new BufferExperiment();
        //bufferExperiment.setTestAlarmManager(this);
    }

}


