package com.ubiqlog.ubiqlogwear.sensors;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.widget.Toast;


import com.ubiqlog.ubiqlogwear.utils.JsonEncodeDecode;
import com.ubiqlog.ubiqlogwear.utils.SensorState;

/**
 * This class should be changed in a way not to turn on Bluetooth. 
 * @author Reza
 *
 */
public class BluetoothSensor extends Service  {

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BroadcastReceiver broadcastReceiver;
    //	private Handler objHandler = new Handler();
    private com.ubiqlog.ubiqlogwear.core.DataAcquisitor dataAcq;

    private Timer bluetoothTimer;
    private Calendar currentDateTime = Calendar.getInstance();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i("Bluetooth-Logging", "--- onCreate");

        try {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                Log.e("[BluetoothSensor.onCreate]", "----- Bluetooth is not supported");
            }

            Toast.makeText(getApplicationContext(), "Started Bluetooth Logging", Toast.LENGTH_SHORT).show();
            super.onCreate();
        }
        catch (Exception e){
            Log.e("[BluetoothSensor.onCreate]", "----------Error reading the log interval from sensor catalouge-----"
                    + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private Timer timer = new Timer();

    private class BTtimerTask extends TimerTask {
        @Override
        public void run() {
            try {
                Log.i("Bluetooth-Logging", "--- onRun");
                if (bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.startDiscovery();

                    currentDateTime = Calendar.getInstance();
                    currentDateTime.setTimeInMillis(System.currentTimeMillis());
                    if (currentDateTime.get(Calendar.SECOND) > 30) {
                        currentDateTime.add(Calendar.MINUTE, 1);
                    }
                    currentDateTime.set(Calendar.SECOND, 0);
                    currentDateTime.set(Calendar.MILLISECOND, 0);

                    broadcastReceiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            //						Log.e("Bluetooth-Logging", "-----READ SENSOR------------------" + intent.getAction());
                            String action = intent.getAction();
                            Log.i("Bluetooth-Logging", "--- action =" + action);
                            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                                SensorState.Bluetooth stateBluetoothDevice = null;
//								Log.e("......xxxx.....",device.getName());
                                switch (device.getBondState()) {
                                    case BluetoothDevice.BOND_NONE:
                                        stateBluetoothDevice = SensorState.Bluetooth.NONE;
                                        break;
                                    case BluetoothDevice.BOND_BONDING:
                                        stateBluetoothDevice = SensorState.Bluetooth.BONDING;
                                        break;
                                    case BluetoothDevice.BOND_BONDED:
                                        stateBluetoothDevice = SensorState.Bluetooth.BONDED;
                                        break;
                                }
                                String deviceName = device.getName();
                                if (deviceName == null) {
                                    deviceName = "NO_DEVICENAME";
                                }
                                String jsonString = JsonEncodeDecode.EncodeBluetooth(deviceName, device.getAddress(), stateBluetoothDevice.getState(), currentDateTime.getTime());
                                dataAcq.dataBuffer.add(jsonString);
                                Log.i("Bluetooth-Logging",jsonString);
                                jsonString = null;
                            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                            }
                        }
                    };
                    //				IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                    IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                    filter.addAction(BluetoothDevice.ACTION_FOUND);
                    //				IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

                    registerReceiver(broadcastReceiver, filter);
                }else {
                    Log.e("Bluetooth-Logging","----------BT is disabled");
                }
            }catch (Exception ex){
                ex.printStackTrace();
                Log.e("Bluetooth-Logging","----------Error inside run method of TimerTask");
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Bluetooth-Logging", "--- onStartCommand");
        timer.schedule(new BTtimerTask(), SensorConstants.BT_LOG_INTERVAL, SensorConstants.BT_LOG_INTERVAL);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("Bluetooth-Logging", "--- onDestroy");
        if (bluetoothTimer != null) {
            bluetoothTimer.cancel();
        }
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }

        try {
            this.unregisterReceiver(broadcastReceiver);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * check if it is second or minutes
     * @param strScanInterval
     * @return
     */
    private Long parseScanInterval(String strScanInterval) {
        String lowerCase = strScanInterval.toLowerCase();
        String toParse = "";
        int multiplier = 0;
        if (lowerCase.endsWith("m")) {
            toParse = lowerCase.split("m")[0];
            multiplier = 60000;
        } else if (lowerCase.endsWith("s")) {
            toParse = lowerCase.split("s")[0];
            multiplier = 1000;
        } else {
            toParse = lowerCase;
            multiplier = 1000;
        }

        return Long.parseLong(toParse);
    }

    public String getSensorName() {
        return "BLUETOOTH";
    }

}
