package com.ubiqlog.ubiqlogwear.utils;

/**
 * Created by prajnashetty on 10/30/14.
 * This class is not being used
 */
public class IOManager_BU {
/*
   // private String filename = "MySampleFile.txt";
    private String filepath = "Ubiqlog";
    File myInternalFile;
    String datenow = new String();

    private Context mContext;

    public IOManager_BU(Context context) {
        mContext = context;
    }

    public void logData(ArrayList<String> inArr) {
        FileWriter writer;
        datenow = DateFormat.getDateInstance(DateFormat.SHORT).format(System.currentTimeMillis());
        SimpleDateFormat dateformat = new SimpleDateFormat("M-d-yyyy");
        //datenow = DateFormat.getDateInstance(DateFormat.SHORT).format(System.currentTimeMillis());
        //datenow = datenow.replace("/", "-");
        Log.i("IO-Manager", "--- Start writing to log");
        try {
//            File logFile = new File(Setting.Instance(null).getLogFolder() + "/" + "log_" + dateformat.format(new Date()) + ".txt"); //datenow+ ".txt");
//            writer = new FileWriter(logFile, true);
//            if (!logFile.exists()) {
//                logFile.createNewFile();
//            }
//            Iterator<String> it = inArr.iterator();
//            while (it.hasNext()) {
//                String aaa = it.next();
//                writer.append(aaa+ System.getProperty("line.separator"));
//            }
//            writer.flush();
//            writer.close();
//            writer = null;

//            ContextWrapper contextWrapper = new ContextWrapper(mContext);
//            File directory = contextWrapper.getDir(filepath, Context.MODE_PRIVATE);
//            myInternalFile = new File(directory , filename);

            FileOutputStream fos = mContext.openFileOutput("log_" + dateformat.format(new Date()) + ".txt",Context.MODE_PRIVATE);
            Log.i("IO-Manager", "FileName = "+"log_" + dateformat.format(new Date()) + ".txt");
            Iterator<String> it = inArr.iterator();
            while (it.hasNext()) {
                String aaa = it.next();
                fos.write((aaa+System.getProperty("line.separator")).toString().getBytes());
                //writer.append(aaa+ System.getProperty("line.separator"));
            }
            //fos.write(myInputText.getText().toString().getBytes());
            fos.close();

            Log.i("IO-Manager", "--- Finished writing to log");

        } catch (Exception e) {

            Log.e("DataAggregator", "--------Failed to write in a file-----"+ e.getMessage() + "; Stack: " +  Log.getStackTraceString(e));

        }
    }

    public void logError(String msg) {
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