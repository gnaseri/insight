package com.ubiqlog.ubiqlogwear.sensors;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by User on 2/23/15.
 */
public class AppUsageSensor2 extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
            Log.i(
                    "AppUsage",
                    "Window Package: " + event.getPackageName()
            );
    }



    @Override
    public void onInterrupt() {

    }
}
