package com.ubiqlog.ubiqlogwear.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by CM on 2/4/15.
 */
public class FeatureCheck {
    public static boolean hasBluetoothFeature(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);

    }

    public static boolean hasLightFeature(Context context){
        boolean status =  context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT);
        Log.d ("Light", "" + status);
        return status;
    }
}
