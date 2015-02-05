package com.ubiqlog.ubiqlogwear.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class Engine extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
	/*
//	private Context ctx = getApplicationContext();
	private Context ctx = getBaseContext();
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate(){
		try {
			IntentFilter filter = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);
			BroadcastReceiver asubiqlog = new AutoStartUbiqlog() ;
			registerReceiver(asubiqlog,filter);
			
			Log.e("[Engine]","---onCreate 4m UBIQLOG");
            Log.i("[core.Engine.onStartCommand]", "-------START--------");
			startRecording(getApplicationContext(), getApplication());

			String msgToast = "Start the Logging Process.";
			Toast toast = Toast.makeText(getApplicationContext(), msgToast, Toast.LENGTH_SHORT);
			toast.show();

		} catch (Exception e) {
			Log.e("[Engine]", "-----------ERROR:" + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	/**
	 * Run UbiqLog after boot
	 */

    /*
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		Log.e("[Engine]","----onStartCommand 4m UBIQLOG");
        Log.i("[core.Engine.onStartCommand]", "-------START--------");
		try {
			startRecording(getApplicationContext(), getApplication());

			String msgToast = "Start the Logging Process.";
			Toast toast = Toast.makeText(getApplicationContext(), msgToast, Toast.LENGTH_SHORT);
			toast.show();

		} catch (Exception e) {
			Log.e("[Engine]", "-----------ERROR:" + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return START_STICKY;
	}
	
	public static void startRecording(Context ctx, Application app) throws Exception {

		File audioDir = new File(com.ubiqlog.ubiqlogwear.common.Setting.Instance(ctx).getLogFolder());
		if ((audioDir.mkdir())) {
			Log.d("[core.Engine.startRecording]", "-------AUDIO DIRECTORY ALREADY CREATED--------");
		} else {
			Log.d("[core.Engine.startRecording]","-------AUDIO DIRECTORY IS NOT CREATED PROBABLY IT ALREADY EXISTS--------");
		}

		Intent i = new Intent();
		i.setClass(app, DataAggregator.class);
		ctx.startService(i);

        Intent iCleanUp = new Intent();
        iCleanUp.setClass(app, CleanUpService.class);
        ctx.startService(iCleanUp);

        Intent i1 = new Intent();
        i1.setClass(app, HeartRateSensor.class) ;
        ctx.startService(i1);

        Intent i2 = new Intent();
        i2.setClass(app, AccelerometerSensor.class) ;
        ctx.startService(i2);

        Intent i3 = new Intent();
        i3.setClass(app, GyroSensor.class) ;
        ctx.startService(i3);

        Intent i4 = new Intent();
        i4.setClass(app, BatterySensor.class) ;
        ctx.startService(i4);

        Intent i5 = new Intent();
        i5.setClass(app, ApplicationSensor.class) ;
        ctx.startService(i5);

        Intent i6 = new Intent();
        i6.setClass(app, BluetoothSensor.class);
        ctx.startService(i6);

	}

	public static void stopRecording(Context ctx, Application app) {

        Intent i= new Intent();
        i.setClass(app, DataAggregator.class);
        ctx.stopService(i);

        Intent iCleanUp= new Intent();
        iCleanUp.setClass(app, CleanUpService.class);
        ctx.stopService(iCleanUp);

		Intent i1 = new Intent();
		i1.setClass(app, HeartRateSensor.class);
		ctx.stopService(i1);

        Intent i2 = new Intent();
        i2.setClass(app, AccelerometerSensor.class);
        ctx.stopService(i2);

        Intent i3 = new Intent();
        i3.setClass(app, GyroSensor.class);
        ctx.stopService(i3);

        Intent i4 = new Intent();
        i4.setClass(app, BatterySensor.class);
        ctx.stopService(i4);

        Intent i5 = new Intent();
        i5.setClass(app, ApplicationSensor.class);
        ctx.stopService(i5);

        Intent i6 = new Intent();
        i6.setClass(app, BluetoothSensor.class);
        ctx.stopService(i6);


    }

*/
}