
package com.ubiqlog.ubiqlogwear.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ubiqlog.ubiqlogwear.R;

/**
 * Created by rawassizadeh on 12/7/14.
 */

public class MainActivity extends Activity implements SensorEventListener {


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
        image = new Integer[]{null, R.drawable.runicn, R.drawable.hearticn, R.drawable.appicn, null, R.drawable.batteryicn,
                R.drawable.bluetoothicn, R.drawable.lighticn, R.drawable.notificn};

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
}
