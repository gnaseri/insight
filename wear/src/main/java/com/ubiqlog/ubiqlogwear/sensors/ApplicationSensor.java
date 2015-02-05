package com.ubiqlog.ubiqlogwear.sensors;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * This sensor will use time interval for logging
 * @author Reza Rawassizadeh
 *
 */
public class ApplicationSensor extends Service implements SensorConnector {
    @Override
    public void readSensor() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
/*
  //  private com.ubiqlog.ubiqlogwear.core.DataAcquisitor dataAcq;

    private Handler objHandler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable doAppLogging = new Runnable() {
        public void run() {
            readSensor();
            objHandler.postDelayed(doAppLogging, SensorConstants.APP_LOG_INTERVAL);
        }
    };

    @Override
    public void onCreate() {
        Log.d("Application-Logging", "--- onCreate");
        Toast.makeText(getApplicationContext(), "Started Application Logging", Toast.LENGTH_SHORT).show();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        objHandler.removeCallbacks(doAppLogging);
        Log.d("Application-Logging", "--- onDestroy");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d("Application-Logging", "--- onStartCommand");
        readSensor();
        objHandler.postDelayed(doAppLogging, SensorConstants.APP_LOG_INTERVAL);
        return START_STICKY;
    }

    public void readSensor() {
        try {


            ActivityManager activeMan = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> activityList = activeMan.getRunningAppProcesses();
            Date _currentDate = new Date();
            ArrayList<String> _foundApps = new ArrayList<String>();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            for (int i = 0; i < activityList.size(); i++) {
                if ((activityList.get(i).importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) &&
                        ! containSysProc(activityList.get(i).processName) && pm.isInteractive()) {
                    if(!ApplicationSensorHelper.Instance()._apps.containsKey(activityList.get(i).processName)){
                        ApplicationSensorHelper.Instance()._apps.put(activityList.get(i).processName, _currentDate);
                    }
                    _foundApps.add(activityList.get(i).processName);
                }
            }
            ApplicationSensorHelper.Instance().logApps(_foundApps, "Application", SensorConstants.APP_LOG_INTERVAL_2,_currentDate);
           // Log.d("Application-Logging", "--- active application list " + activityList );

        } catch (Exception e) {
            IOManager errlogger = new IOManager();
            errlogger.logError("[ApplicationSensor.readSensor] error:" + e.getMessage()+ " Stack:" + Log.getStackTraceString(e));
        }
    }

    private boolean containSysProc(String input) {
        for (int j = 0; j < Setting.Instance(this).getCoreProcs().length; j++) {
            if (input.equalsIgnoreCase(Setting.Instance(this).getCoreProcs()[j])) {
                return true;
            }
        }
        return false;
    }


    public String getSensorName() {
        return "APPLICATION";
    }
*/
}

