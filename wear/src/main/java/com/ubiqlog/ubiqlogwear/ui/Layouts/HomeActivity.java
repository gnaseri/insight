package com.ubiqlog.ubiqlogwear.ui.Layouts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ubiqlog.ubiqlogwear.R;
import com.ubiqlog.ubiqlogwear.common.MainListItem;
import com.ubiqlog.ubiqlogwear.sensors.ActivitySensor;
import com.ubiqlog.ubiqlogwear.sensors.BatterySensor;
import com.ubiqlog.ubiqlogwear.sensors.LightSensor;
import com.ubiqlog.ubiqlogwear.utils.FeatureCheck;
import com.ubiqlog.ubiqlogwear.utils.MenuItems;

/**
 * Created by Manouchehr on 3/10/2015.
 */
public class HomeActivity extends Activity {
    public static String LOG_TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MenuItems menuItemsClass = new MenuItems(HomeActivity.this);
        final MainListItem[] menuItems = menuItemsClass.createMeListItems();

        GridLayout frameBox = (GridLayout) findViewById(R.id.home_lstItems);

        //int[] icons = new int[]{R.drawable.ic_activity, R.drawable.ic_heart, R.drawable.ic_battery, R.drawable.ic_bluetooth, R.drawable.ic_notif, R.drawable.ic_light, R.drawable.ic_appusage};
        // int[] icons = new int[]{R.drawable.ic_activity, R.drawable.ic_heart, R.drawable.ic_battery, R.drawable.ic_bluetooth, R.drawable.ic_notif, R.drawable.ic_light, R.drawable.ic_appusage};
        //String[] labels = new String[]{"Activity", "Heart Rate", "Battery", "Bluetooth", "Notifications", "Ambient Light", "App Usage"};
        int index = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                String itemLabel = menuItems[index].getTitle();
                int itemImage = menuItems[index].getImgId();
                final Intent itemIntent = menuItems[index].getIntent(this);
                LinearLayout contentItem = new LinearLayout(this);
                final LinearLayout.LayoutParams contentItemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                contentItem.setLayoutParams(contentItemParams);
                contentItem.setTag(itemLabel);
                contentItem.setOrientation(LinearLayout.VERTICAL);
                contentItem.setClickable(true);
                contentItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(HomeActivity.this, v.getTag().toString(), Toast.LENGTH_SHORT).show();
                        startActivity(itemIntent);
                    }
                });

                ImageView icon = new ImageView(this);
                final RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(getSizeInDP(60), getSizeInDP(50));
                icon.setLayoutParams(iconParams);
                int iconPadding = getSizeInDP(10);
                icon.setPadding(iconPadding, iconPadding, iconPadding, iconPadding);
                icon.setBackgroundColor(Color.TRANSPARENT);
                icon.setImageResource(itemImage);
                icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                contentItem.addView(icon);

                TextView label = new TextView(this);
                final TableRow.LayoutParams labelParams = new TableRow.LayoutParams(getSizeInDP(60), ViewGroup.LayoutParams.MATCH_PARENT);
                labelParams.gravity = (Gravity.BOTTOM | Gravity.CENTER);
                //labelParams.addRule(RelativeLayout.BELOW, icon.getId());
                label.setLayoutParams(labelParams);
                label.setSingleLine(false);
                label.setTextSize(getResources().getDimension(R.dimen.home_labels_fontsize));
                label.setText(itemLabel);
                label.setLines(2);
                label.setTextColor(getResources().getColor(R.color.home_labels_color));
                label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                label.setGravity(Gravity.TOP | Gravity.CENTER);
                contentItem.addView(label);

                frameBox.addView(contentItem, new GridLayout.LayoutParams(
                        GridLayout.spec(i, GridLayout.CENTER),
                        GridLayout.spec(j, GridLayout.CENTER)));

                if (index == menuItems.length - 1)
                    break;
                index += 1;
            }
        startAllServices();
    }


    private int getSizeInDP(int x) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x, getResources().getDisplayMetrics());
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
    }
}
