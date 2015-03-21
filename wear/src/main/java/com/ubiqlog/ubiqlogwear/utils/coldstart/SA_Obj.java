package com.ubiqlog.ubiqlogwear.utils.coldstart;

/**
 * Created by User on 3/20/15.
 */
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/* This class is used to hold the semantic strings in memory */
public class SA_Obj {
    String mSensor_name;
    String mSensor_data;
    String mSensor_time;
    ArrayList<String> mWeekDays;
    int mTotalWeekdays;
    String mWeekDay;

    public SA_Obj (String sensor_name, String sensor_data, String weekDay, String sensor_time){
        this.mSensor_name = sensor_name;
        this.mSensor_data = sensor_data;
        this.mSensor_time = sensor_time;
        mWeekDay = weekDay;
        mWeekDays = new ArrayList<>(7);
        mWeekDays.add(weekDay);
        mTotalWeekdays = 1;
    }

    public double getConfidence (){
        return mWeekDays.size() / (double) mTotalWeekdays;
    }

    public ArrayList<String> getWeekDayArray(){
        return mWeekDays;
    }

    public void insertWeekDay(String weekDay){
        mWeekDays.add(weekDay);
    }
    public void incrementDayTotal(){
        mTotalWeekdays++;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((mSensor_data == null) ? 0 : mSensor_data.hashCode());
        result = prime * result
                + ((mSensor_name == null) ? 0 : mSensor_name.hashCode());
        result = prime * result
                + ((mSensor_time == null) ? 0 : mSensor_time.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SA_Obj other = (SA_Obj) obj;
        if (mSensor_data == null) {
            if (other.mSensor_data != null)
                return false;
        } else if (!mSensor_data.equals(other.mSensor_data))
            return false;
        if (mSensor_name == null) {
            if (other.mSensor_name != null)
                return false;
        } else if (!mSensor_name.equals(other.mSensor_name))
            return false;
        if (mSensor_time == null) {
            if (other.mSensor_time != null)
                return false;
        } else if (!mSensor_time.equals(other.mSensor_time))
            return false;
        return true;
    }

    public String toJSONString(){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("confidence", getConfidence());
            jsonObj.put("time", mSensor_time);
            jsonObj.put(mSensor_name, mSensor_data );
            jsonObj.put("weekdays", getWeekDayArray());

            return jsonObj.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }



}
