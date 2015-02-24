package com.ubiqlog.ubiqlogwear.utils;

import com.ubiqlog.ubiqlogwear.common.NotificationParcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 2/23/15.
 */
public class JSONUtil {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");

    public static String encodeBattery(int percent, boolean charging, Date timeStamp) {
        JSONObject jsonObject = new JSONObject();
        JSONObject sensorDataObj = new JSONObject();
        try {
            jsonObject.put("sensor_name", "Battery");
            jsonObject.put("timestamp", timeStamp);

            sensorDataObj.put("percent", percent);
            sensorDataObj.put("charging", charging);

            jsonObject.put("sensor_data", sensorDataObj);

            return jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String encodeLight(float lux, Date timeStamp) {
        JSONObject jsonObject = new JSONObject();
        JSONObject sensorData = new JSONObject();
        try {
            jsonObject.put("sensor_name", "Light");
            jsonObject.put("timestamp", timeStamp);

            sensorData.put("lux", lux);

            jsonObject.put("sensor_data", sensorData);

            return jsonObject.toString();


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeBT(String state, Date timeStamp) {
        JSONObject jsonObject = new JSONObject();
        JSONObject sensorData = new JSONObject();
        try {
            jsonObject.put("sensor_name", "BT");
            jsonObject.put("timestamp", timeStamp);

            sensorData.put("state", state);

            jsonObject.put("sensor_data", sensorData);

            return jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeStepActivity(Date startTime, Date endTime,
                                            int culmStepAmt, int stepDiff) {

        JSONObject jsonObject = new JSONObject();
        JSONObject sensorData = new JSONObject();
        try {
            jsonObject.put("sensor_name", "Activity");
            jsonObject.put("timestamp", endTime);

            sensorData.put("start_time", startTime);
            sensorData.put("end_time", endTime);
            sensorData.put("culm_step_amount", culmStepAmt);
            sensorData.put("step_delta", stepDiff);

            jsonObject.put("sensor_data", sensorData);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeNotification(NotificationParcel in) {
        JSONObject jsonObject = new JSONObject();
        JSONObject sensorData = new JSONObject();
        try {
            jsonObject.put("sensor_name", "Notification");
            jsonObject.put("timestamp", new Date(in.POST_TIME));

            sensorData.put("package_name", in.PACKAGE_NAME);
            sensorData.put("title", in.EXTRA_TITLE);
            sensorData.put("text", in.EXTRA_TEXT);
            sensorData.put("flags", in.flags);
            sensorData.put("category", in.category);

            jsonObject.put("sensor_data", sensorData);

            return jsonObject.toString();


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* Decode Methods*/


    /**
     * @param encoded
     * @return obj[0] : timestamp
     * obj[1] : percent
     * obj[2] : charging
     */
    public Object[] decodeBattery(String encoded) {
        try {
            JSONObject jObj = new JSONObject(encoded);
            Date date = (Date) jObj.get("timestamp");

            JSONObject sensorData = jObj.getJSONObject("sensor_data");

            int percent = sensorData.getInt("percent");
            boolean charging = sensorData.getBoolean("charging");

            return new Object[]{date, percent, charging};
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * obj[0] : date
     * obj[1] : lux
     *
     * @param encoded
     * @return
     */
    public Object[] decodeLight(String encoded) {
        try {
            JSONObject jObj = new JSONObject(encoded);
            Date date = (Date) jObj.get("timestamp");

            JSONObject sensorData = jObj.getJSONObject("sensor_data");

            float lux = (float) sensorData.get("lux");

            return new Object[]{date, lux};

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * obj[0]:date
     * obj[1] :pkg name
     * obj[2]: title
     * obj[3] : text
     * obj[4] : flags
     * obj[5] : category
     * @param encoded
     * @return
     */
    public Object[] decodeNotification(String encoded) {
        try {
            JSONObject jObj = new JSONObject(encoded);
            Date date = (Date) jObj.get("timestamp");

            JSONObject sensorData = jObj.getJSONObject("sensor_data");
            String packageName = sensorData.getString("package_name");
            String title = sensorData.getString("title");
            String text = sensorData.getString("text");
            Integer flags = sensorData.getInt("flags");
            String category = sensorData.getString("category");


            return new Object[]{date, packageName, title, text, flags, category};

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * obj[0] :date
     * obj[1]: startTime
     * obj[2] : endTime
     * obj[3] : culmStep
     * obj[4] : step_delta
     * @param encoded
     * @return
     */
    public Object[] decodeStepActivity(String encoded) {
        try {
            JSONObject jObj = new JSONObject(encoded);
            Date date = (Date) jObj.get("timestamp");

            JSONObject sensorData = jObj.getJSONObject("sensor_data");
            Date startDate = (Date) sensorData.get("start_time");
            Date endDate = (Date) sensorData.get("end_time");
            Integer culmAmt = sensorData.getInt("culm_step_amount");
            Integer stepDelta = sensorData.getInt("step_delta");

            return new Object[]{date, startDate, endDate, culmAmt, stepDelta};


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
