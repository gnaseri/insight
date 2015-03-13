package com.ubiqlog.ubiqlogwear.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;

import com.ubiqlog.ubiqlogwear.Adapters.MyAdapter;
import com.ubiqlog.ubiqlogwear.Alarm.AlarmReceiver;
import com.ubiqlog.ubiqlogwear.R;
import com.ubiqlog.ubiqlogwear.common.MainListItem;
import com.ubiqlog.ubiqlogwear.sensors.ActivitySensor;
import com.ubiqlog.ubiqlogwear.sensors.BatterySensor;
import com.ubiqlog.ubiqlogwear.sensors.LightSensor;
import com.ubiqlog.ubiqlogwear.utils.FeatureCheck;
import com.ubiqlog.ubiqlogwear.utils.IOManager;
import com.ubiqlog.ubiqlogwear.utils.MenuItems;
import com.ubiqlog.ubiqlogwear.utils.PredictionNotification;

/**
 * Created by Cole
 */

public class OldMailActivity extends Activity {


    public static String LOG_TAG = OldMailActivity.class.getSimpleName();

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
                    MenuItems menuItems = new MenuItems(OldMailActivity.this);
                    //Start Alarm manager

                    listView = (WearableListView) roundBack.findViewById(R.id.wearable_list);
                    listView.setAdapter(new MyAdapter(OldMailActivity.this, menuItems.createMeListItems()));
                    listView.setClickListener(new WearableListView.ClickListener() {
                        @Override
                        public void onClick(WearableListView.ViewHolder viewHolder) {

                            MainListItem listItem = (MainListItem) viewHolder.itemView.getTag();
                            if (!listItem.getTitle().equals("Me") &&
                                    !listItem.getTitle().equals("System"))
                                startActivity(listItem.getIntent(OldMailActivity.this));

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
        startService(new Intent(this, ActivitySensor.class));
        //TODO Activity Sensor have hooks applied and needs to be redone
        //Notification and Bluetooth autostart due to dataLayer
        // HeartRate needs hooks applied to activity
        if (FeatureCheck.hasLightFeature(this)) {
            startService(new Intent(this, LightSensor.class));
        }

        AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.setMidnightAlarmManager(OldMailActivity.this);
        //TODO Remove NotifExperiment
        //BufferExperiment bufferExperiment = new BufferExperiment();
        //bufferExperiment.setTestAlarmManager(this);
    }

}


