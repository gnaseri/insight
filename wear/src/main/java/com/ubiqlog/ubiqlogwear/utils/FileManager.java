package com.ubiqlog.ubiqlogwear.utils;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by User on 2/17/15.
 */
public class FileManager {
    private Context context;
    public FileManager (Context context){
        this.context = context;
    }

    public File getFileDir(){
        File dir = new File(context.getFilesDir().getAbsolutePath() +  "/log_");
        return dir;
    }

    public void createZipFromFileDir (File dir){
        File[] files = dir.listFiles();

        String zipFileLocation = context.getFilesDir().getAbsolutePath() + ".zip";

        try {
            FileOutputStream fos = new FileOutputStream(zipFileLocation);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (int i = 0; i < files.length; i++){
                System.out.println("Adding file: " + files[i].getName());

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

}
