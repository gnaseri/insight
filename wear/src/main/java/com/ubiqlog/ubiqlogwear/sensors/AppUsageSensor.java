package com.ubiqlog.ubiqlogwear.sensors;

import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by Cole on 2/13/15.
 */

/* This class utilizes UsageStats to get usage time about packages within the system */
public class AppUsageSensor {
    /*private UsageStatsManager usageStats;
    void something(Context context){
        context.startActivity(new Intent());
    }
*/

   @SuppressWarnings("ResourceType")
   void d (Context context ){
       // If lollipop
       if (Build.VERSION.SDK_INT >= 21 ){
           UsageStatsManager mUsage = (UsageStatsManager) context.getSystemService ("usagestats");
           // user needs to approve
           context.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
       }
   }
}
