package com.ubiqlog.ubiqlogwear.test;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.ubiqlog.ubiqlogwear.data.JSONUtil;

import java.util.Date;

/**
 * Created by User on 2/23/15.
 */
public class JSONTest extends InstrumentationTestCase {
    boolean charging;
    int percent;
    Date timeStamp;

    @Override
    protected void setUp() throws Exception {
        charging = false;
        percent = 50;
        timeStamp = new Date();

    }

    public void testEncodeBattery(){
        String jsonEncoded = JSONUtil.encodeBattery(percent,charging,timeStamp);
        Log.d("TestEncode",jsonEncoded);
        System.out.println("TestEncode:" + jsonEncoded);
        //this is correct
    }
}
