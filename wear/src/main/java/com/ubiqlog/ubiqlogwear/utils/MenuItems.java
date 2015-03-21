package com.ubiqlog.ubiqlogwear.utils;

import android.app.Activity;

import com.ubiqlog.ubiqlogwear.R;
import com.ubiqlog.ubiqlogwear.ui.MainListItem;
import com.ubiqlog.ubiqlogwear.ui.wActivity;
import com.ubiqlog.ubiqlogwear.ui.wAmbientLight;
import com.ubiqlog.ubiqlogwear.ui.wAppUsage;
import com.ubiqlog.ubiqlogwear.ui.wBattery;
import com.ubiqlog.ubiqlogwear.ui.wBluetooth;
import com.ubiqlog.ubiqlogwear.ui.wHeartRate;
import com.ubiqlog.ubiqlogwear.ui.wNotifications;

import java.util.ArrayList;

/**
 * Created by Manouchehr on 3/11/2015.
 */
public class MenuItems {
    Activity context = new Activity();

    public MenuItems(Activity context) {
        this.context = context;
    }

    // Create the list view items to be displayed in the homescreen Listview
    public MainListItem[] createMeListItems() {
        ArrayList<MainListItem> listArray = new ArrayList<MainListItem>();
        MainListItem meTitle = new MainListItem(context.getString(R.string.me_title),
                null, null);
       // listArray.add(meTitle);
        MainListItem activityItem = new MainListItem(
                context.getResources().getString(R.string.activity_title),
                R.drawable.ic_activity, wActivity.class);//ActivitySensor.class
        listArray.add(activityItem);
        MainListItem heartrateItem = new MainListItem(
                context.getString(R.string.heart_rate_title),
                R.drawable.ic_heart, wHeartRate.class);
        listArray.add(heartrateItem);
        MainListItem appUsageItem = new MainListItem(
                context.getString(R.string.app_usage_title),
                R.drawable.ic_appusage, wAppUsage.class);

        listArray.add(appUsageItem);
        listArray.addAll(createSystemListItems());

        return listArray.toArray(new MainListItem[listArray.size()]);
    }

    public ArrayList<MainListItem> createSystemListItems() {
        ArrayList<MainListItem> systemItemList = new ArrayList<MainListItem>();
        MainListItem systemTitle = new MainListItem(
                context.getString(R.string.system_title),
                null, null);
        //systemItemList.add(systemTitle);

        MainListItem battItem = new MainListItem(
                context.getString(R.string.battery_title),
                R.drawable.ic_battery,
                wBattery.class);//BatteryLevelActivity.class

        systemItemList.add(battItem);

        MainListItem notifItem = new MainListItem(
                context.getString(R.string.notification_title),
                R.drawable.ic_notif,
                wNotifications.class);
        systemItemList.add(notifItem);

        if (FeatureCheck.hasBluetoothFeature(context)) {
            // create MainListItem bluetooth

            MainListItem bluetoothItem = new MainListItem(
                    context.getString(R.string.bluetooth_title),
                    R.drawable.ic_bluetooth, wBluetooth.class);
            systemItemList.add(bluetoothItem);
        }
        if (FeatureCheck.hasLightFeature(context)) {
            MainListItem ambLightItem = new MainListItem(
                    context.getString(R.string.amblight_title),
                    R.drawable.ic_light, wAmbientLight.class);
            systemItemList.add(ambLightItem);
        }

        return systemItemList;
    }
}
