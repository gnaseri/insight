package com.ubiqlog.ubiqlogwear.sensors;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;


public class BluetoothSensor extends Service  {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    /* Attach a broadcast receiver to watch for connection/disconnection */
    @Override
    public void onCreate() {
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);

        this.registerReceiver(mReceiver, filter1);
        this.registerReceiver(mReceiver,filter2);
        this.registerReceiver(mReceiver,filter3);
    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                //Device found
            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)){
                //Device is connected
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                //Done searching
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)){
                //About to disconnect
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)){
                // Disconnected
            }

        }
    };
}
