package com.insight.insight.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
/**
 * Created by prajnashetty on 10/30/14.
 * This class is not being used.
 */

public class CleanUpService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
   /* public CleanUpService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate()
    {
        //cleanUp();
    }

    private void cleanUp ()
    {
        FileWriter writer;
        String datenow = DateFormat.getDateInstance(DateFormat.SHORT).format(System.currentTimeMillis());
        SimpleDateFormat dateformat = new SimpleDateFormat("M-d-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        try {
            File logDir = new File(Setting.Instance(null).getLogFolder());
            File[] directoryListing = logDir.listFiles();
            if (directoryListing != null) {
                for (File file : directoryListing) {
                    String fileName = file.getName();
                    String fileDateString = fileName.substring((fileName.indexOf('_')+1),fileName.indexOf('.'));
                    Date fileDate = dateformat.parse(fileDateString);
                    if (fileDate.before(cal.getTime()))
                        file.delete();
                }
            }
            Log.i("Cleanup", "--- Finished deleting files older than a week");

        } catch (Exception e) {

            Log.e("DataAggregator", "--------Failed to write in a file-----"+ e.getMessage() + "; Stack: " +  Log.getStackTraceString(e));

        }
    }
*/
}
