package com.ubiqlog.ubiqlogwear.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by User on 3/6/15.
 */
public class TempGranUtil {



    public static String getTemporalTimeStamp(Date timeStamp){
        Calendar cal = Calendar.getInstance();
        cal.setTime(timeStamp);
        cal = setCalByTempGran(cal);
        int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        return hourOfDay + ":00";
    }

    private static Calendar setCalByTempGran(Calendar cal){
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

    public static String encodeRawData (String sensor, String data, Date timeStamp){
        StringBuilder encoded = new StringBuilder("");
        encoded.append(sensor + ",");
        encoded.append(data + ",");
        encoded.append(timeStamp);
        return encoded.toString();
    }
    //TODO Need to implement semantic abstraction
    public static String encodeTemporalSemantic (String sensor, String data, Date timeStamp){
        StringBuilder encoded = new StringBuilder("");
        encoded.append(sensor + ",");
        encoded.append(data + ",");
        encoded.append(timeStamp + ",");
        // encoded.append (SemanticAbstraction + ",")
        encoded.append(getTemporalTimeStamp(timeStamp));
        return encoded.toString();
    }

}
