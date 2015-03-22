package com.insight.insight.utils.coldstart;

import android.os.Environment;
import android.util.Log;

import com.insight.insight.common.Setting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by User on 3/20/15.
 */
public class ColdStart {
    private static String getDataFolderFullPath(String folder){
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                + Setting.APP_FOLDER + "/" + folder;

    }

    private static File[] getFirstFilesInDir(String folder, int count) {
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
        //TODO FIX THIS
        File dir = new File(getDataFolderFullPath(folder));
        File[] files = dir.listFiles(myFilter);


        // sort files list on lastModified date
        if (files.length > 0) {
            Arrays.sort(files, new Comparator() {
                public int compare(Object o1, Object o2) {
                    if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                        return 1;
                    } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
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

    /**
     * input:
     * [0]:sensorName
     * [1]:sensorData
     * [2]:Timestamp
     * [3]:semantic data
     * [4]:semanticTime
     * */
    private static SA_Obj buildSAObj(String line){
        String[] tokens = line.split(",");
        String sensorName = tokens[0];
        String sensorData = tokens[3];
        String sensorTime = tokens[4];
        String weekDay = getWeekday(tokens[2]);

        String tmp;
        if ((tmp = convertBatteryData(sensorData)) != null){
            sensorData = tmp;
        }

        SA_Obj saObj = new SA_Obj(sensorName,sensorData, weekDay, sensorTime);
        return saObj;
    }

    private static String convertBatteryData (String dataToken){
        if (dataToken.contains("Charging:")){
            StringBuilder output = new StringBuilder("");

            String[] tokens = dataToken.split(" ");
            String percent = tokens[0];
            String state = tokens[1];
            output.append(percent + "-");
            String[] chargingTokens = state.split(":");
            if (chargingTokens[1].equals("true")){
                output.append("Charging");
            }
            else{
                output.append("Discharging");
            }
            return output.toString();
        }
        return null;
    }


    private static Set<SA_Obj> loadFileIntoSet (File f1){
        String line;
        Set <SA_Obj> objs = new HashSet<SA_Obj>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f1));
            while ((line = br.readLine()) != null){
                SA_Obj obj = buildSAObj(line);
                objs.add(obj);
            } // all objects loaded into set

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return objs;

    }

    private static String getWeekday (String line){
        try {
            Date weekDate = new SimpleDateFormat("EE").parse(line);
            String weekDay = new SimpleDateFormat("EE").format(weekDate);
            return weekDay;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }


    private static Set<SA_Obj> compareFiles(File f1, File[] files){
        Set <SA_Obj> f1_Objs = loadFileIntoSet(f1);

        for (File f2 : files){
            Set <SA_Obj> f2_Objs = loadFileIntoSet(f2);
            String f2WeekDay = ((SA_Obj)(f2_Objs.iterator().next())).mWeekDay;
            for (SA_Obj s1 : f1_Objs){

                if (f2_Objs.contains(s1)){
                    s1.insertWeekDay(f2WeekDay);
                }
                s1.incrementDayTotal();
            }
        }

        return f1_Objs;



    }

    private static void compareAllFiles(File[] files){
        File[] original = files;
        Set <SA_Obj> sa_objs;
        ArrayList <File> fileList = new ArrayList<File>(Arrays.asList(files));
        for (int i = 0; i < files.length; i++){
            File current = fileList.remove(i);
            sa_objs = compareFiles(current,fileList.toArray(new File[0]));
            writeObjsToFile(sa_objs);
            fileList.add(i, current);
        }
    }
    private static void writeObjsToFile(Set <SA_Obj> sa_objs){
        File out = new File ( Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                + Setting.APP_FOLDER + "/" +  "profile");
        try {
            FileWriter fw = new FileWriter(out,true);

            for (SA_Obj s: sa_objs){
                fw.append(s.toJSONString());
                fw.append('\n');
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getProfile(){
        Log.d("ColdStart", "Started profile");
        ArrayList<String> dirNames = new ArrayList<>(Arrays.asList("SA/BatterySensor", "SA/Bluetooth", "SA/Notif"));
        for (String dirName : dirNames){
            File[] files = getFirstFilesInDir(dirName, 7);
            compareAllFiles(files);
        }
    }

}
