package com.ubiqlog.ubiqlogwear.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by User on 3/6/15.
 */
public class TempGranUtil {

    /**
     * Convert start and endtime by temporal granularity
     * @param startTime
     * @param endTime
     * @return
     */
    public static Date[] convertDatesByTempGran (Date startTime, Date endTime){
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);

        Date newStartDate = new Date(setCalByTempGran(cal).getTimeInMillis()); //convert startTime

        cal.setTime(endTime);
        Date newEndDate = new Date(setCalByTempGran(cal).getTimeInMillis()); //convert endTime

        return new Date[]{newStartDate,newEndDate};
    }

    public static String getTemporalTimeStamp(Date timeStamp){
        Calendar cal = Calendar.getInstance();
        cal.setTime(timeStamp);
        cal = setCalByTempGran(cal);
        int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        return hourOfDay + ":00";
    }

    public static Calendar setCalByTempGran(Calendar cal){
        if (cal.get(Calendar.MINUTE) > 30){
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
        } // round down
        else{
            cal.set(Calendar.MINUTE, 0);

        }
        return cal;
    }

    public static String encodeTemporalData (String sensor, String data, Date timeStamp){
        StringBuilder encoded = new StringBuilder("");
        encoded.append(sensor + ",");
        encoded.append(data + ",");
        encoded.append(timeStamp + ",");
        encoded.append(getTemporalTimeStamp(timeStamp));
        return encoded.toString();
    }

}
