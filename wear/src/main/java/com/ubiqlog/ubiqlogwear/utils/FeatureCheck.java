package com.ubiqlog.ubiqlogwear.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by Cole on 2/4/15.
 */
public class FeatureCheck {
    public static boolean hasBluetoothFeature(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);

    }

    public static boolean hasLightFeature(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT);
    }
}
