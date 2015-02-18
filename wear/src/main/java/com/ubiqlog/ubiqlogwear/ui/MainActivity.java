package com.ubiqlog.ubiqlogwear.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;

import com.ubiqlog.ubiqlogwear.Adapters.MyAdapter;
import com.ubiqlog.ubiqlogwear.R;
import com.ubiqlog.ubiqlogwear.common.MainListItem;
import com.ubiqlog.ubiqlogwear.utils.FeatureCheck;

import java.util.ArrayList;

/**
 * Created by Cole
 */

public class MainActivity extends Activity  {


    public static String LOG_TAG = MainActivity.class.getSimpleName();

    private WearableListView listView;
    private BoxInsetLayout roundBack;


    // Create the list view items to be displayed in the homescreen Listview
    private MainListItem[] createMeListItems() {
        ArrayList<MainListItem> listArray = new ArrayList<MainListItem>();
        MainListItem meTitle = new MainListItem(getString(R.string.me_title),
                null, null);
        listArray.add(meTitle);
        MainListItem activityItem = new MainListItem(
                getResources().getString(R.string.activity_title),
                R.drawable.ic_activity, wActivity.class);//ActivitySensor.class
        listArray.add(activityItem);
        MainListItem heartrateItem = new MainListItem(
                getString(R.string.heart_rate_title),
                R.drawable.ic_heart, wHeartRate.class);
        listArray.add(heartrateItem);
        MainListItem appUsageItem = new MainListItem(
                getString(R.string.app_usage_title),
                R.drawable.ic_appusage, wAppUsage.class);

        listArray.add(appUsageItem);
        listArray.addAll(createSystemListItems());

        return listArray.toArray(new MainListItem[listArray.size()]);


    }

    private ArrayList<MainListItem> createSystemListItems() {
        ArrayList<MainListItem> systemItemList = new ArrayList<MainListItem>();
        MainListItem systemTitle = new MainListItem(
                getString(R.string.system_title),
                null, null);
        systemItemList.add(systemTitle);

        MainListItem battItem = new MainListItem(
                getString(R.string.battery_title),
                R.drawable.ic_battery,
                wBattery.class);//BatteryLevelActivity.class

        systemItemList.add(battItem);

        MainListItem notifItem = new MainListItem(
                getString(R.string.notification_title),
                R.drawable.ic_notif,
                wNotifications.class);
        systemItemList.add(notifItem);

        if (FeatureCheck.hasBluetoothFeature(this)) {
            // create MainListItem bluetooth

            MainListItem bluetoothItem = new MainListItem(
                    getString(R.string.bluetooth_title),
                    R.drawable.ic_bluetooth, wBluetooth.class);
            systemItemList.add(bluetoothItem);
        }
        if (!FeatureCheck.hasLightFeature(this)) {
            MainListItem ambLightItem = new MainListItem(
                    getString(R.string.amblight_title),
                    R.drawable.ic_light, wAmbientLight.class);
            systemItemList.add(ambLightItem);
        }

        return systemItemList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wear);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                roundBack = (BoxInsetLayout) stub.findViewById(R.id.roundLayout);
                if (roundBack != null) {


                    listView = (WearableListView) roundBack.findViewById(R.id.wearable_list);
                    listView.setAdapter(new MyAdapter(MainActivity.this, createMeListItems()));
                    listView.setClickListener(new WearableListView.ClickListener() {
                        @Override
                        public void onClick(WearableListView.ViewHolder viewHolder) {

                            MainListItem listItem = (MainListItem) viewHolder.itemView.getTag();
                            if (!listItem.getTitle().equals("Me") &&
                                    !listItem.getTitle().equals("System"))
                                startActivity(listItem.getIntent(MainActivity.this));

                        }

                        @Override
                        public void onTopEmptyRegionClick() {

                        }
                    });
                }

            }
        });
    }
}


