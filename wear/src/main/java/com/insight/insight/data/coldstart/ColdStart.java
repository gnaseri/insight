package com.insight.insight.data.coldstart;

/**
 * Created by User on 3/24/15.
 */

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/* This class has two main functions
   Create Profile and Update Profile
 */
public class ColdStart {

    /* This function will get the first 7 Files in each directory
     It compares these 7 files with each other to create the profile
     It then writes the dates for each of the last updates for each Directory
   */
    public static void createProfile() throws ParseException, IOException {
        ArrayList<String> dirNames = new ArrayList<>(Arrays.asList("SA/BatterySensor",
                "SA/Bluetooth", "SA/Notif"));
        ArrayList<Pair> lastUpdateEntries = new ArrayList<>();
        for (String dirName : dirNames) {
            File[] files = Util.getFirstFilesInDir(dirName, 7);

            Pair entry = new Pair (dirName, Util.getStringDateFromFileName(files[files.length-1]));
            lastUpdateEntries.add(entry);
            compareAllFiles(files);
        }
        writeLastUpdates(lastUpdateEntries);

    }

    /* This function updates our profile.
       It performs a check with isReady on each directory in dirNames
       to see if 2 files exist after the last profile update
       if so, Update the profile
     */
    public static void updateProfile(){
        ArrayList<String> dirNames = new ArrayList<>(Arrays.asList("SA/BatterySensor",
                "SA/Bluetooth", "SA/Notif"));
        Set <SA_Obj> updated = null;
        for (String dir : dirNames){
            UpdateProfile profile = new UpdateProfile();
            boolean isReady = profile.isReady(dir);

            if (isReady){
                updated = UpdateProfile.getUpdatedProfileObjs(dir);

            }
        }

        if (updated != null){
            UpdateProfile.incrementTotalDays(updated);
            UpdateProfile.writeUpdateToProfile(updated);
        }

    }



    private static Set<SA_Obj> loadFileIntoSet(File f1) {
        String line;
        Set<SA_Obj> objs = new HashSet<SA_Obj>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f1));
            while ((line = br.readLine()) != null) {
                SA_Obj obj = Util.buildSAObj(line);
                objs.add(obj);
            } // all objects loaded into set

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objs;

    }
    protected static Set<SA_Obj> loadProfileIntoSet(File f1) {
        String line;
        Set<SA_Obj> objs = new HashSet<SA_Obj>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f1));
            while ((line = br.readLine()) != null) {
                SA_Obj obj = Util.buildSAObj_JSON(line);
                objs.add(obj);
            } // all objects loaded into set

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objs;

    }

    static String getWeekday(String line) {
        try {
            Date weekDate = new SimpleDateFormat("EE").parse(line);
            String weekDay = new SimpleDateFormat("EE").format(weekDate);
            return weekDay;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    /* Compare one file with all other files
        The files are loaded into an Set of objects for each line
        Using a set allows us to avoid duplicates

        return this set of objects
     */
    protected static Set<SA_Obj> compareFiles(File f1, File[] files) {
        Set<SA_Obj> f1_Objs = loadFileIntoSet(f1);

        for (File f2 : files) {
            Set<SA_Obj> f2_Objs = loadFileIntoSet(f2);
            String f2WeekDay = ((SA_Obj) (f2_Objs.iterator().next())).mWeekDay;
            for (SA_Obj s1 : f1_Objs) {

                if (f2_Objs.contains(s1)) {
                    s1.insertWeekDay(f2WeekDay);
                }
                s1.incrementDayTotal();
            }
        }

        return f1_Objs;

    }

    protected static void compareAllFiles(File[] files) {
        File[] original = files;
        Set<SA_Obj> sa_objs;
        ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(files));
        for (int i = 0; i < files.length; i++) {
            File current = fileList.remove(i);
            sa_objs = compareFiles(current, fileList.toArray(new File[0]));
            writeObjsToFile(sa_objs);
            fileList.add(i, current);
        }
    }

    private static void writeObjsToFile(Set<SA_Obj> sa_objs) {
        File out = new File(Util.PROFILE_LOCATION);
        try {
            FileWriter fw = new FileWriter(out, true);

            for (SA_Obj s : sa_objs) {
                fw.append(s.toJSONString());
                fw.append('\n');
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static void writeLastUpdates (ArrayList<Pair> arr){
        File policyFile = new File (Util.POLICY_LOCATION);
        FileWriter fw;
        try {
            fw = new FileWriter(policyFile,false);
            for (Pair p : arr){
                String encoded;
                try {
                    encoded = Util.encodedLastRead(p.dirName, p.date);
                    fw.append(encoded);
                    fw.append("\n");

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }

            }
            fw.flush();
            fw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    private static class Pair {
        String dirName;
        String date;
        Pair (String dir, String date){ this.dirName = dir; this.date = date;}
    }






}

