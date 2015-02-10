package com.ubiqlog.ubiqlogwear.sensors;

import android.util.Log;

import com.ubiqlog.ubiqlogwear.core.DataAcquisitor;
import com.ubiqlog.ubiqlogwear.utils.CSVEncodeDecode;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by User on 2/9/15.
 */
public class ActivityDataHelper {
    private static final String LOG_TAG = ActivityDataHelper.class.getSimpleName();

    public static class Step {
        private int culmStepAmt;
        private Date date;

        public Step(int culmStepAmt, Date date) {
            this.culmStepAmt = culmStepAmt;
            this.date = date;
        }

        public int getCulmStepAmt() {
            return this.culmStepAmt;
        }

        public Date getDate() {
            return this.date;
        }

    }

    /* This StepList will be used to determine when the user has stopped walking
       If the time between the last Inserted Element and the element being inserted
       differs by SensorConstants.StepListInterval, add to DataAcquistor buff,
       Otherwise, Update last inserted
     */
    public static class StepList {
        ArrayList<Step> stepBuff;

        public StepList() {
            stepBuff = new ArrayList<Step>();
        }

        public synchronized void insert(Step s) {
            if (stepBuff.isEmpty()) {
                stepBuff.add(s);
            } else {
                if (stepBuff.size() == 1) {
                    //Start time has been inserted
                    stepBuff.add(s);
                } else {
                    if (isStillWalking(s)) {
                        //Update buffer
                        stepBuff.add(1,s);
                    } else {
                        //form Encoded String
                        Date startTime = stepBuff.get(0).getDate();
                        Date endTime = stepBuff.get(1).getDate();
                        int culmStepAmnt = stepBuff.get(1).getCulmStepAmt();
                        int diffInStep = stepBuff.get(1).getCulmStepAmt() - stepBuff.get(0).getCulmStepAmt();

                        String encoded = CSVEncodeDecode.encodeStepActivity(startTime,
                                                                   endTime,culmStepAmnt,diffInStep);
                        Log.d(LOG_TAG,encoded);
                        //Send to DataAcquisitor
                        DataAcquisitor.dataBuffer.add(encoded);
                        stepBuff.clear();



                    }

                }
            }
        }


        /*This method should only be called if there is a startTime and lastUpdated */
        private boolean isStillWalking(Step s) {
            if (stepBuff.size() < 2){
                try {
                    throw new IllegalAccessException();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            Date lastUpdateDate = stepBuff.get(1).getDate();
            long diffInMillis = lastUpdateDate.getTime() - s.getDate().getTime();
            long absDiff = Math.abs(diffInMillis);
            Log.d(LOG_TAG, "Diff in millis:" + absDiff);
            if (absDiff > SensorConstants.WALKDETECTION_INTERVAL) {
                return false;
            } else
                return true;

        }
    }
}
