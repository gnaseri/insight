package com.ubiqlog.ubiqlogwear.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class IOManager{

    public static final String LOG_TAG = IOManager.class.getSimpleName();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy");
	
	public void logData( Context context, ArrayList<String> data) {
        Log.d(LOG_TAG,"Started Saving Data");
        FileWriter writer;


        File logFile = new File (context.getFilesDir().getAbsolutePath() + "/log_" +
                dateFormat.format(new Date()) + ".txt");

        //Setup File for writing
        try {
            writer = new FileWriter(logFile,true);

            for (String s : data){
                writer.append(s + System.getProperty("line.separator"));
            }

            writer.flush();
            writer.close();

            Log.d (LOG_TAG, "Finished Writing to file");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	/*public void logError(String msg) {
		PrintWriter printWr;
		Date a = new Date (System.currentTimeMillis());
		String errorDate = a.getDate()+"-"+a.getMonth()+"-"+a.getYear();
		File errorFile = new File(Setting.Instance(null).getLogFolder(), "error_"+errorDate+".txt");
		try {
			printWr = new PrintWriter(new FileWriter(errorFile, true));
			printWr.append(msg + System.getProperty("line.separator"));
			printWr.flush();
			printWr.close();
			printWr = null;
		} catch (Exception ex) {
			Log.e("IOManager.logError", ex.getMessage(), ex);
		}
	}
    */
}