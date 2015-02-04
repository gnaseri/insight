package com.ubiqlog.ubiqlogwear.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 2/4/15.
 */
public class CSVEncodeDecode {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");

    /**
     * Battery will be encoded into a csv format using parameters.
     * Format will be as follows
     * "Battery,Percent:percent Status:charging,timestamp"
     *
     * @param percent
     * @param charging
     * @param timeStamp
     * @return
     */
    public static String encodeBattery(float percent, boolean charging, Date timeStamp) {
        String chargingString = charging ? "Charging" : "Discharging";

        StringBuilder encodedString = new StringBuilder("");
        encodedString = encodedString.append(
                "Battery,Percent:" + percent + " "
                        + "Status:" + chargingString + "," +
                        dateFormat.format(timeStamp)
        );

        return encodedString.toString();
    }

    public static String encodeLight(float lux, Date timestamp) {
        StringBuilder encodedString = new StringBuilder("");
        encodedString = encodedString.append(
                "Light,Lux:" + lux + "," +
                        dateFormat.format(timestamp)
        );

        return encodedString.toString();
    }
}
