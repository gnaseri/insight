package com.ubiqlog.ubiqlogwear.test;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.ubiqlog.ubiqlogwear.utils.CSVEncodeDecode;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by User on 2/10/15.
 */
public class DecodeTest extends InstrumentationTestCase{
    private String encodedBT;
    private String encodedBatt;
    private ArrayList<String> decoded;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        encodedBT = "Bluetooth,Status:Connected," + new Date();
        encodedBatt = "Battery,Percent:88 Status:Charging," + new Date();
    }

    private void printArray(ArrayList<String> A){
        for (String s : A){
            Log.d("Test",s);
        }
    }
    public void testDecode(){
        decoded = CSVEncodeDecode.decodeBluetooth(encodedBT);
        printArray(decoded);
        decoded = CSVEncodeDecode.decodeBattery(encodedBatt);
        printArray(decoded);

    }
}
