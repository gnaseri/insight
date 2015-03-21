package com.ubiqlog.ubiqlogwear.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by User on 3/3/15.
 */
public class CalendarUtil {
    public static final String TAG = CalendarUtil.class.getSimpleName();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");

    /* Resets the calendar provided to the start of the provided day
       Returns start in [0] end [1]
     */
    public static Long[] getStartandEndTime(Calendar cal){
        long endTime = cal.getTimeInMillis();

        cal.add(Calendar.HOUR_OF_DAY, -cal.get(Calendar.HOUR_OF_DAY));
        cal.add(Calendar.MINUTE, -cal.get(Calendar.MINUTE));
        cal.add(Calendar.SECOND, -cal.get(Calendar.SECOND));
        long startTime = cal.getTimeInMillis();

        Log.i(TAG, "Range start:" + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        return new Long[]{startTime,endTime};

    }
}
