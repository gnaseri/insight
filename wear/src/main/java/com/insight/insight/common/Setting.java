package com.insight.insight.common;

import java.text.SimpleDateFormat;
/*
    Created by CM
 */

//TODO 4m Reza: static file names should be converted to CAPITAL letters
public final class Setting {

    public static final long SAVE_FILE_WAIT_INTERVAL = 10000L; // 10 seconds
    //Internal File Directory is appended to this
    public static final String LOG_FOLDER = "insight" ;
    public static final String APP_FOLDER = "insight" ;

    public static final int bufferMaxSize = 10; // default: 10

    public static final int linksButtonCount = 7;
    
    public static final String dataFilename_Battery ="BatterySensor";
    public static final String dataFilename_Bluetooth ="Bluetooth";
    public static final String dataFilename_Notifications ="Notif";
    public static final String dataFilename_HeartRate ="HeartRate";
    public static final String dataFilename_LightSensor ="LightSensor";
    public static final String dataFilename_ActivFit ="ActivFit";

    public static final SimpleDateFormat filenameFormat = new SimpleDateFormat("M-d-yyyy");
    public static final SimpleDateFormat timestampFormat = new SimpleDateFormat("E MMM d HH:mm:ss zzz yyyy"); // e.g. 'Wed Mar 04 00:03:56 GMT+01:00 2015'

}
