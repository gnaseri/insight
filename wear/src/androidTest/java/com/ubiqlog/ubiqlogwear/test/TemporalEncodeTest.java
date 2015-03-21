package com.ubiqlog.ubiqlogwear.test;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.ubiqlog.ubiqlogwear.data.TempGranUtil;

import java.util.Date;

/**
 * Created by User on 3/8/15.
 */
public class TemporalEncodeTest extends InstrumentationTestCase {
    private String sensor;
    private String sensorData;
    private Date timeStamp;

    @Override
    protected void setUp() throws Exception {
        sensor = "battery";
        sensorData = "55% Charging:true";
        timeStamp = new Date();

    }

    public void testEncode(){
        String encoded = TempGranUtil.encodeTemporalData(sensor,sensorData,timeStamp);
        Log.d("TempTest", encoded);
        assertEquals("battery,55% Charging:true," + timeStamp + ","
                + TempGranUtil.getTemporalTimeStamp(timeStamp), encoded);
    }
}
