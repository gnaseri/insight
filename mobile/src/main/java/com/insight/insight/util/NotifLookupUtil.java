package com.insight.insight.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by CM on 3/10/15.
 */

/* Connect and parse data from the Google Play Store for the pkg supplied */
public class NotifLookupUtil {
    public static final String gPlayURL = "http://play.google.com/store/apps/details?id=";
    public static final String TAG = NotifLookupUtil.class.getSimpleName();

    //Folders
    public static final String sa_completeDir = "/SAComplete";
    public static final String mapDir = "/PkgMap";

    //File names
    public static final String completeFileName ="TMP_SACompleted.txt";
    public static final String mapFileName = "map.txt";

    static File notifFile;
    static String line;

    /* Scan the entire sent file, Fill in the genres in a temporary folder*/

    public static void handleNotifLookup (Context context, String filepath){
        notifFile = new File (filepath);
        try {
            BufferedReader br = new BufferedReader(new FileReader(notifFile));
            String genre;
            String pkgName;
            String url;
            String[] decoded;
            while ((line = br.readLine()) != null){
                decoded = decodeNotification(line);
                pkgName = decoded[1];
                if ((genre = isMapped(context,pkgName)) == null){
                    url = gPlayURL + pkgName;

                    genre = new PkgLookupUtil.HtmlGrab().execute(url).get();
                    writeGenreToMapFile(context,pkgName,genre);
                }
                String reEncoded = reEncodeString(decoded, genre);

                //Write new string to file
                writeEncodedToFile(context,reEncoded);

                Log.d(TAG, "PkgGenre:" + genre);


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    /**
     * tokens:
     * 0 : sensorname
     * 1 : pkgname
     * 2 : date
     * 3 : genre
     * 4: tmpTime
     * @param line
     * @return
     */
    private static String[] decodeNotification(String line){
        String[] tokens = line.split(","); //Split by comma csv encoded
        String sensorName = tokens[0];
        String pkgName = tokens[1];
        String date = tokens[2];
        String genre = tokens[3];
        String tmpTime = tokens[4];

        return new String[]{tokens[0],tokens[1],tokens[2],tokens[3],tokens[4]};

    }
    private static String reEncodeString(String[] decoded, String genre){
        StringBuilder encoded = new StringBuilder("");
        encoded.append(decoded[0] + "," + decoded[1] + "," + decoded[2] + ",");
        encoded.append(genre + ",");
        encoded.append(decoded[4]);

        return encoded.toString();
    }

    private static void writeEncodedToFile(Context context, String encoded){
        File dir = new File (context.getFilesDir().getAbsolutePath() + sa_completeDir);
        dir.mkdirs();

        File file = new File (dir, completeFileName);
        try {
            FileWriter fw = new FileWriter(file,true);
            fw.append(encoded);
            fw.append('\n');
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void writeGenreToMapFile(Context context, String pkgname, String genre){
        File dir = new File (context.getFilesDir().getAbsolutePath() + mapDir);
        dir.mkdirs();

        File file = new File (dir, mapFileName);
        try {
            FileWriter writer = new FileWriter(file,true);
            writer.append(pkgname + ";" + genre);
            writer.append("\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String isMapped(Context context, String pkgName){
        File file = new File (context.getFilesDir() + mapDir + "/" + mapFileName);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null){
                String[] tokens = line.split(";");
                if (tokens[0].equals(pkgName)){
                    Log.d(TAG, "Genre from map:" + tokens[1]);
                    return tokens[1];
                }

            }
        } catch (FileNotFoundException e) {
           Log.d(TAG, "file not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
