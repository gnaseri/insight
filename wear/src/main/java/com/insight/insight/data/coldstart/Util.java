package com.insight.insight.data.coldstart;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by User on 3/24/15.
 */
public class Util {

    public static String encodedLastRead(String dirName, String date) throws JSONException {
        JSONObject jObj = new JSONObject();
        jObj.put("dirname", dirName);
        jObj.put("date", date);

        return jObj.toString();

    }

    public static String[] decodeLastReadJSON(String line) {
        JSONObject jObj;
        try {
            jObj = new JSONObject(line);

            String dirName = jObj.getString("dirname");
            String date = jObj.getString("date");
            return new String[]{dirName, date};
        } catch (JSONException e1) {
            return null;
        }


    }

    public static Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("M-d-yyyy");
        return sdf.parse(dateString);
    }

    public static boolean isDateAfter(Date afterDate, Date beforeDate) {

        if (afterDate.after(beforeDate)) {
            return true;
        }
        return false;
    }

    /**
     * input: [0]:sensorName [1]:sensorData [2]:Timestamp [3]:semantic data
     * [4]:semanticTime
     */
    static SA_Obj buildSAObj(String line) {
        String[] tokens = line.split(",");
        String sensorName = tokens[0];
        String sensorData = tokens[3];
        String sensorTime = tokens[4];
        String weekDay = ColdStart.getWeekday(tokens[2]);
        String tmp;
        if ((tmp = Util.convertBatteryData(sensorData)) != null) {
            sensorData = tmp;
        }
        SA_Obj saObj = new SA_Obj(sensorName, sensorData, weekDay, sensorTime);
        return saObj;
    }

    static SA_Obj buildSAObj_JSON(String line) {
        try {
            JSONObject jObj = new JSONObject(line);
            String sensor = jObj.getString("sensor");
            String sensorTime = jObj.getString("time");
            JSONArray weekDays = (JSONArray) jObj.get("weekdays");
            ArrayList<String> weekDayArray = new ArrayList<>();
            for (int i = 0; i < weekDays.length(); i++) {
                weekDayArray.add(weekDays.get(i).toString());
            }

            int totalweekdays = jObj.getInt("totalweekdays");

            String[] tokens = sensor.split(";"); //0 sensor name 1 sensorData
            SA_Obj returnObj = new SA_Obj(tokens[0], tokens[1], null, sensorTime);
            returnObj.mWeekDays.clear();
            returnObj.mWeekDays.addAll(weekDayArray);
            returnObj.mTotalWeekdays = totalweekdays;

            return returnObj;

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    static String convertBatteryData(String dataToken) {
        if (dataToken.contains("Charging:")) {
            StringBuilder output = new StringBuilder("");

            String[] tokens = dataToken.split(" ");
            String percent = tokens[0];
            String state = tokens[1];
            output.append(percent + "-");
            String[] chargingTokens = state.split(":");
            if (chargingTokens[1].equals("true")) {
                output.append("Charging");
            } else {
                output.append("Discharging");
            }
            return output.toString();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static File[] getFirstFilesInDir(String folder, int count) {
        // filter files to return just txt files and not empty
        FilenameFilter myFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (name.toLowerCase().endsWith(".txt")) {
                    File f = new File(dir.getAbsolutePath() + "/" + name);
                    if (f.isFile() && f.length() > 0)
                        return true;
                    else
                        return false;
                } else {
                    return false;
                }
            }
        };
        // TODO FIX THIS
        File dir = new File(ColdStart.getDataFolderFullPath(folder));
        File[] files = dir.listFiles(myFilter);

        // sort files list on lastModified date
        if (files.length > 0) {
            Arrays.sort(files, new Comparator() {
                public int compare(Object o1, Object o2) {
                    if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                        return 1;
                    } else if (((File) o1).lastModified() < ((File) o2)
                            .lastModified()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
        }
        // return limited count of files ordered by lastModified date
        if (files.length <= count)
            return files;
        else {
            File[] result = new File[count];
            System.arraycopy(files, 0, result, 0, result.length);
            return result;
        }
    }

    @SuppressWarnings("unchecked")
    public static File[] getLastFilesInDir(String folder, int count) {
        // filter files to return just txt files and not empty
        FilenameFilter myFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (name.toLowerCase().endsWith(".txt")) {
                    File f = new File(dir.getAbsolutePath() + "/" + name);
                    if (f.isFile() && f.length() > 0)
                        return true;
                    else
                        return false;
                } else {
                    return false;
                }
            }
        };
        // TODO FIX THIS
        File dir = new File(ColdStart.getDataFolderFullPath(folder));
        File[] files = dir.listFiles(myFilter);

        // sort files list on lastModified date
        if (files.length > 0) {
            Arrays.sort(files, new Comparator() {
                public int compare(Object o1, Object o2) {
                    if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                        return -1;
                    } else if (((File) o1).lastModified() < ((File) o2)
                            .lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                }
            });
        }
        // return limited count of files ordered by lastModified date
        if (files.length <= count)
            return files;
        else {
            File[] result = new File[count];
            System.arraycopy(files, 0, result, 0, result.length);
            return result;
        }
    }

    static String getStringDateFromFileName(File file) {
        String dateString = file.getName();
        String[] tokens = dateString.split("\\.");
        return tokens[0];
    }

    static String POLICY_LOCATION = ColdStart.getDataFolderFullPath("policy");
    static String PROFILE_LOCATION = ColdStart.getDataFolderFullPath("profile"); //gives filelocation

    static String getLastUpdateDateString(String dirName) {
        Log.d("ColdStart", "Starting updateSTring");
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(new File(POLICY_LOCATION)));
            Log.d("ColdStart", "OpenedBR");
            String line;
            while ((line = br.readLine()) != null) {
                Log.d("ColdStart", "Line:" + line);
                String[] decoded = decodeLastReadJSON(line);
                Log.d("ColdStart", "Decode:" + decoded[0] + "\t" + decoded[1]);

                if (decoded != null && isCorrectLine(dirName, decoded)) {
                    return decoded[1];
                }
            }
            Log.d("ColdStart", "Finished Reading");


        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.d("COLDSTART", "IOEXCEPTION");
            e.printStackTrace();
            return null;
        }
        return null;


    }

    public static boolean isCorrectLine(String dirname, String[] decoded) {


        Log.d("ColdStart", "TrueFolder:" + decoded[0]);
        if (dirname.equals(decoded[0])) {
            return true;
        }
        return false;
    }

}


