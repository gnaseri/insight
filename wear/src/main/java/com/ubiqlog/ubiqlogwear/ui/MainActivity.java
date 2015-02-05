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
                R.drawable.ic_activity, MainActivity.class);
        listArray.add(activityItem);
        MainListItem heartrateItem = new MainListItem(
                getString(R.string.heart_rate_title),
                R.drawable.ic_heart, MainActivity.class);
        listArray.add(heartrateItem);
        MainListItem appUsageItem = new MainListItem(
                getString(R.string.app_usage_title),
                R.drawable.ic_appusage, MainActivity.class);

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
                BatteryLevelActivity.class);

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
        if (FeatureCheck.hasLightFeature(this)) {
            MainListItem ambLightItem = new MainListItem(
                    getString(R.string.amblight_title),
                    R.drawable.ic_light, MainActivity.class);
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

/*
    String[] name = null;
    Integer[] image = null;
    //Sensor and SensorManager
    Sensor mHeartRateSensor;
    SensorManager mSensorManager;
    private TextView mTextView;
    private WearableListView mWearableListView;

    public MainActivity() {
        //  super(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wear);

        //Sensor and sensor manager
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);


        name = new String[]{getResources().getString(R.string.me), getResources().getString(R.string.activity), getResources().getString(R.string.heartrate),
                getResources().getString(R.string.appusage), getResources().getString(R.string.device), getResources().getString(R.string.battery),
                getResources().getString(R.string.bluetooth), getResources().getString(R.string.amlight), getResources().getString(R.string.notification)};
        image = new Integer[]{null, R.drawable.ic_activity, R.drawable.ic_heart, R.drawable.ic_appusage, null, R.drawable.ic_battery,
                R.drawable.ic_bluetooth, R.drawable.ic_light, R.drawable.ic_notif};

        mWearableListView = (WearableListView) findViewById(R.id.times_list_view);
        //setadapter to listview
        mWearableListView.setAdapter(new TimerWearableListViewAdapter(this));
        //on item click
        mWearableListView.setClickListener(new WearableListView.ClickListener() {
            @Override
            public void onClick(WearableListView.ViewHolder viewHolder) {
                String selectedItem = name[viewHolder.getPosition()];

                if (selectedItem.toLowerCase().contains("bluetooth")) {
                    Intent i = new Intent(getApplicationContext(), wBluetooth.class);
                    startActivity(i);

                } else if (selectedItem.toLowerCase().contains("notifications")) {
                    Intent i = new Intent(getApplicationContext(), wNotifications.class);
                    startActivity(i);
                } else if (selectedItem.toLowerCase().contains("battery")) {
                    startActivity(new Intent(MainActivity.this, BatteryLevelActivity.class));
                } else
                    Toast.makeText(getApplicationContext(), "Not Active!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onTopEmptyRegionClick() {

            }
        });

    }

    // Sensor listerner methods
    @Override
    protected void onResume() {
        super.onResume();
        //Register the listener
        if (mSensorManager != null) {
            mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Unregister the listener
        if (mSensorManager != null)
            mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Update your data. This check is very raw. You should improve it when the sensor is unable to calculate the heart rate
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            if ((int) event.values[0] > 0) {
                //mCircledImageView.setCircleColor(getResources().getColor(R.color.green));
                //mTextView.setText("" + (int) event.values[0]);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private static final class Adapter extends WearableListView.Adapter {
        private final Context mContext;
        private final LayoutInflater mInflater;

        private Adapter(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
//            return new WearableListView.ViewHolder(
//                    mInflater.inflate(R.layout.main_list_item, null));
        }

        @Override
        public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
            //TextView view = (TextView) holder.itemView.findViewById(R.id.name);
            //view.setText(mContext.getString(NotificationPresets.PRESETS[position].nameResId));
            //holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
//            return NotificationPresets.PRESETS.length;
            return 0;
        }
    }

    //List View Adapter
    private final class TimerWearableListViewAdapter extends
            WearableListView.Adapter {
        private final Context mContext;
        private final LayoutInflater mInflater;

        private TimerWearableListViewAdapter(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WearableListView.ViewHolder(
                    mInflater.inflate(R.layout.list_item, null));
        }

        @Override
        public void onBindViewHolder(WearableListView.ViewHolder holder,
                                     int position) {
            TextView view = (TextView) holder.itemView.findViewById(R.id.time_text);

            if (name[position].contains("Me") || name[position].contains("Device")) {
                view.setTextColor(Color.parseColor(getResources().getString(R.string.custom_orange)));
                view.setTextScaleX(1.3f);
                view.setTypeface(null, Typeface.BOLD);
                //view.setTypeface(null, Typeface.ITALIC);
                view.setText(name[position]);
                // getResources().getString(R.string.custom_orange)
                // "#FFBF00"
            } else {
                view.setText(name[position]);
            }
            //holder.itemView.setTag(position);
            if (image[position] != null) {
                ImageView img = (ImageView) holder.itemView.findViewById(R.id.circle);
                img.setImageResource(image[position]);
            }
        }

        @Override
        public int getItemCount() {
            return name.length;
        }
    }
} */
