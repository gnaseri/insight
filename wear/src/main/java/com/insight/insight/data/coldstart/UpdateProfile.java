package com.insight.insight.data.coldstart;

/**
 * Created by User on 3/24/15.
 */

import android.util.Log;

import com.insight.insight.utils.IOManager;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

public class UpdateProfile {

    public static boolean isReady(String dir) {

        File[] files = Util.getLastFilesInDir(dir, 2);
        // Get the second to last file
        System.out.println(files.length);
        File secondLast = files[0];

        try {
            String secondLastDateString = Util.getStringDateFromFileName(secondLast);
            Log.d("ColdStart", secondLastDateString);
            Date secondLastDate = Util.parseDate(secondLastDateString);
            String lastUpdateString = Util.getLastUpdateDateString(dir);
            Log.d("ColdStart", "S:" + lastUpdateString);

            Date lastUpdate = Util.parseDate(lastUpdateString);

            System.out.println("SecondLast:" + secondLastDate + "\t"
                    + "lastUpdate:" + lastUpdate);

            if (Util.isDateAfter(secondLastDate, lastUpdate)) {
                UpdateProfile.updatePolicy(secondLastDateString, dir, lastUpdateString);
                return true;
            }
            return false;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void updatePolicy(String secondLastDate, String dirName, String lastUpdate) throws IOException {
        File file = new File(Util.POLICY_LOCATION);
        String encoded;
        String previousEncoded;
        try {
            encoded = Util.encodedLastRead(dirName, secondLastDate);
            previousEncoded = Util.encodedLastRead(dirName,lastUpdate);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        BufferedReader br = new BufferedReader(new FileReader(file));
        File tmp = new File(ColdStart.getDataFolderFullPath("tmp"));
        FileWriter fw = new FileWriter(tmp, false);
        String line;

        while ((line = br.readLine()) != null) {
            line = line.replace(previousEncoded, encoded);
            Log.d("UpdatePol:", line);
            fw.append(line);
            fw.append('\n');
        }
        fw.flush();
        fw.close();
        IOManager.copyFile(tmp, file); //Copy file back
        tmp.delete();



    }

    public static Set<SA_Obj> getUpdatedProfileObjs(String dir) {
        File[] files = Util.getLastFilesInDir(dir, 2);
        ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(files));
        File first = fileList.remove(0);
        Set<SA_Obj> objs = ColdStart.compareFiles(first,
                fileList.toArray(new File[0]));
        Set<SA_Obj> profileObjs = ColdStart.loadProfileIntoSet(new File(
                Util.PROFILE_LOCATION));


        ArrayList<String> weekDays = objs.iterator().next().getWeekDayArray();
        int maxWeekDayTotal = 0;
        //Update all confidences
        for (SA_Obj s : profileObjs) {
            if (objs.contains(s)) {
                for (String weekDay : weekDays) {
                    s.insertWeekDay(weekDay);
                    if (maxWeekDayTotal < s.mTotalWeekdays) {
                        maxWeekDayTotal = s.mTotalWeekdays;
                    }
                }
            }

        }
        //Since we are using a set, we can add all elements in, since the once we just updated
        // will not
        for (SA_Obj s : objs) {
            s.mTotalWeekdays = maxWeekDayTotal; // update weekdays
            profileObjs.add(s);
        }

        return profileObjs;

    }

    public static void writeUpdateToProfile(Set<SA_Obj> sa_objs) {
        File out = new File(ColdStart.getDataFolderFullPath("profile"));
        try {
            FileWriter fw = new FileWriter(out, false);

            for (SA_Obj s : sa_objs) {
                fw.append(s.toJSONString());
                fw.append('\n');
            }
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void incrementTotalDays(Set<SA_Obj> saObjs) {
        for (SA_Obj s : saObjs) {
            s.mTotalWeekdays += 2;
        }
    }

}

