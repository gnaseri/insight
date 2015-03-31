package com.insight.insight.data.coldstart;

/**
 * Created by User on 3/24/15.
 */

import android.os.Environment;
import android.util.Log;

import com.insight.insight.common.Setting;

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

public class ColdStart {

    static String getDataFolderFullPath(String folder) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Setting.APP_FOLDER + "/" + folder;

    }

    public static Set<SA_Obj> loadFileIntoSet(File f1) {
        String line;
        Set<SA_Obj> objs = new HashSet<SA_Obj>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f1));
            while ((line = br.readLine()) != null) {
                SA_Obj obj = Util.buildSAObj(line);
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
    public static Set<SA_Obj> loadProfileIntoSet(File f1) {
        String line;
        Set<SA_Obj> objs = new HashSet<SA_Obj>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f1));
            while ((line = br.readLine()) != null) {
                SA_Obj obj = Util.buildSAObj_JSON(line);
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

    static String getWeekday(String line) {
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

	/*public static SA_Obj updateSet (Set <SA_Obj> s1Objs, Set <SA_Obj> s2Objs){
		String f2WeekDay = ((SA_Obj) (s2Objs.iterator().next())).mWeekDay;
		for (SA_Obj s1 : s1Objs) {

			if (s2Objs.contains(s1)) {
				s1.insertWeekDay(f2WeekDay);
			}
			s1.incrementDayTotal();
		}
	} */

    public static Set<SA_Obj> compareFiles(File f1, File[] files) {
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

    public static void compareAllFiles(File[] files) {
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
            // TODO Auto-generated catch block
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
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            fw.flush();
            fw.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    public static class Pair {
        String dirName;
        String date;
        Pair (String dir, String date){ this.dirName = dir; this.date = date;}
    }

    public static void getProfile() throws ParseException, IOException {
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

}

