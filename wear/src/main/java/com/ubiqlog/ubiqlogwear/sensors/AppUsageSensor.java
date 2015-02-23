package com.ubiqlog.ubiqlogwear.sensors;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.util.Log;

import java.util.List;

/**
 * Created by Cole on 2/13/15.
 */

/* This class utilizes UsageStats to get usage time about packages within the system */
public class AppUsageSensor extends Service {
    private final String TAG = this.getClass().getSimpleName();
    private Handler mHandler;
    private GetActivity mRunnable;

    @Override
    public void onCreate() {
        HandlerThread appBackThread = new HandlerThread("AppUsage", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        appBackThread.start();
        mHandler = new Handler(appBackThread.getLooper());
        mRunnable = new GetActivity(this);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler.post(mRunnable);
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressWarnings("ResourceType")
   class GetActivity implements Runnable {
       Context context;
       public GetActivity(Context context){
           this.context = context;
       }
       @Override
       public void run() {
           ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
           List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
           Log.d("topActivity", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName());
           ComponentName componentInfo = taskInfo.get(0).topActivity;
           String packageName = componentInfo.getPackageName();
           Log.d(TAG,"Package name:" + packageName);
           mHandler.postDelayed(mRunnable,6000);
       }
   }

    //{
       // If lollipop
      /* if (Build.VERSION.SDK_INT >= 21 ){
           UsageStatsManager mUsage = (UsageStatsManager) context.getSystemService ("usagestats");
           / user needs to approve
           context.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
       }
       else{ */

       //}
   //}



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
