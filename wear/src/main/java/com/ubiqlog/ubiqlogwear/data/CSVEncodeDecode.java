package com.ubiqlog.ubiqlogwear.data;

import com.ubiqlog.ubiqlogwear.utils.NotificationParcel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by User on 2/4/15.
 */
public class CSVEncodeDecode {
    private static final String LOG_TAG = CSVEncodeDecode.class.getSimpleName();

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
    public static String encodeBattery(int percent, boolean charging, Date timeStamp) {
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

    public static String encodeBT (String state, Date timestamp){
        StringBuilder encodedString = new StringBuilder("");
        encodedString = encodedString.append(
                "Bluetooth,Status:" + state + "," + dateFormat.format(timestamp)
        );
        return encodedString.toString();
    }

    public static String encodeStepActivity (Date startTime, Date endTime, int culmStepAmt,
                                                                             int stepDiff){
        StringBuilder encodedString = new StringBuilder("");
        encodedString = encodedString.append(
                "Activity,StartTime:" + dateFormat.format(startTime) + " "
                + "EndTime:" + dateFormat.format(endTime) + " " + "StepDiff:"
                + stepDiff + " " + "CulmStep:" + culmStepAmt + "," + dateFormat.format(endTime)
        );
        return encodedString.toString();
    }

    public static String encodeNotification (NotificationParcel n){
        StringBuilder encoded = new StringBuilder("");
        String title = n.EXTRA_TITLE;
        String text = n.EXTRA_TEXT;
        String packageName = n.PACKAGE_NAME;
        Integer flags = n.flags;
        Date postDate = new Date(n.POST_TIME);
        //Special Filter for MMS
        if (packageName.equals("com.android.mms")){
            encoded = encoded.append(
                    "Notification,T:" + title + "\t" + text + "\t" + packageName + "\t"
                            + flags + "\t" + postDate
            );
        }

        encoded = encoded.append(
                "Notification,T:" + title + "\t" + text + "\t" + packageName + "\t"
                + flags + "\t" + postDate
        );
        return encoded.toString();
    }

    private static ArrayList<String> parseEncoded(String encoded){
        String[] temp = encoded.split(",");
        return new ArrayList<String>(Arrays.asList(temp));
    }
    public static ArrayList<String> decodeBluetooth(String encoded){
       return parseEncoded(encoded);
    }

    /**
     * decoded[0] = sensor;
     * decoded[1] = Info;
     * decoded[2] = Date;
     */
    public static ArrayList<String> decodeLight (String encoded){
        return parseEncoded(encoded);
    }

    public static ArrayList<String> decodeBattery (String encoded) {
        ArrayList<String> decoded = parseEncoded(encoded);
        String info = decoded.remove(1);
        String[] infoTemp = info.split(" ");
        decoded.add(1, infoTemp[0]); //Percent
        decoded.add(2, infoTemp[1]); // Charging Status
        return decoded;
    }

}
