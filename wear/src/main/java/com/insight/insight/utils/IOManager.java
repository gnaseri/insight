package com.insight.insight.utils;

import android.os.Environment;
import android.util.Log;

import com.insight.insight.common.Setting;
import com.insight.insight.core.DataAcquisitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/* Created by CM & MN */
public class IOManager {

    public static final String LOG_TAG = IOManager.class.getSimpleName();

    public void logData(DataAcquisitor dataAcq, boolean append) {

        if (dataAcq.getDataBuffer().size() <= 0) {
            return;
        }
        File dirs = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/insight/" + dataAcq.getFolderName());
        dirs.mkdirs();

        File logFile = new File(dirs, Setting.filenameFormat.format(new Date()) + ".txt");

        try {
            FileWriter writer = new FileWriter(logFile, append);

            for (String s : dataAcq.getDataBuffer()) {
                writer.append(s + System.getProperty("line.separator"));

            }
            writer.flush();
            writer.close();
            Log.d(LOG_TAG, "Finished writing to file");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public ArrayList<String> getFiles(String path) {
        ArrayList<String> lst = new ArrayList<>();
        File dirs = new File(path);
        for (File d : dirs.listFiles()) {
            if (d.isDirectory()) {
                lst.add("<" + d.getName() + ">");
            } else if (d.isFile()) {
                lst.add("   - " + d.getName() + " [" + d.length() + "] bytes");
            }
        }
        return lst;
    }

    public String getDataFolderFullPath(String dataFolderName) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Setting.APP_FOLDER + "/" + dataFolderName + "/";
    }

    /**
     * @param folder Folder name e.g. 'BatterySensor', 'Bluetooth', 'Notif', 'HeartRate'
     * @param count  Maximum count of files to return
     * @return Limited amount of *.txt files ordered by lastModified date
     */
    public File[] getLastFilesInDir(String folder, int count) {
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

        File dir = new File(getDataFolderFullPath(folder));
        File[] files = dir.listFiles(myFilter);
        if (files != null) {
            // sort files list on lastModified date
            Arrays.sort(files, new Comparator() {
                public int compare(Object o1, Object o2) {
                    if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                        return -1;
                    } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                }
            });

            // return limited count of files ordered by lastModified date
            if (files.length <= count)
                return files;
            else {
                File[] result = new File[count];
                System.arraycopy(files, 0, result, 0, result.length);
                return result;
            }

        } else
            return null;
    }


    public String getFileContent(File file) {
        String sCurrentLine;
        String sContent = "\n";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            while ((sCurrentLine = br.readLine()) != null) {
                sContent += sCurrentLine + "\n";
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sContent;
    }

    public String getFileContent(String filename) {
        String sCurrentLine;
        String sContent = "\n";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filename));
            while ((sCurrentLine = br.readLine()) != null) {
                sContent += sCurrentLine + "\n";
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sContent;
    }

    public Date parseDataFilename2Date(String dataFilename) {
        String fileDate = dataFilename.toLowerCase().replace(".txt", ""); // remove .txt postfix from filename
        try {
            return Setting.filenameFormat.parse(fileDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] convertFileToBytes(File file) {
        byte[] bytes = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            return bytes;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileInputStream fIn = null;
        FileOutputStream fOut = null;
        FileChannel source = null;
        FileChannel destination = null;
        try {
            fIn = new FileInputStream(sourceFile);
            source = fIn.getChannel();
            fOut = new FileOutputStream(destFile);
            destination = fOut.getChannel();
            long transfered = 0;
            long bytes = source.size();
            while (transfered < bytes) {
                transfered += destination.transferFrom(source, 0, source.size());
                destination.position(transfered);
            }
        } finally {
            if (source != null) {
                source.close();
            } else if (fIn != null) {
                fIn.close();
            }
            if (destination != null) {
                destination.close();
            } else if (fOut != null) {
                fOut.close();
            }
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