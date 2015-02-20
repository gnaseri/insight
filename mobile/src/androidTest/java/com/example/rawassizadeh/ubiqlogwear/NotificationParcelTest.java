package com.example.rawassizadeh.ubiqlogwear;

import android.test.InstrumentationTestCase;

/**
 * Created by User on 2/19/15.
 */

/* If i figure out how to create a mock notification, can be tested here. */
public class NotificationParcelTest extends InstrumentationTestCase {
   /* private NotificationParcel np;
    private StatusBarNotification sbn;
    private Parcel in;

   @Override
    protected void setUp() throws Exception{
       super.setUp();
       Notification n = new Notification()
       sbn = new StatusBarNotification("PKG","BASE",11,"TAG",1,1,1,)
       np = new NotificationParcel(sbn);
       np.ticketText = "Hi this is a test";
       np.EXTRA_TITLE = "TITLE TEST";
       // leaving the rest null to assert it will work

       in = Parcel.obtain();



   }

    public void testRecreate(){
        np.writeToParcel(in,0);
        assert(in != null);

        NotificationParcel recreated = NotificationParcel.CREATOR.createFromParcel(in);
        Log.d("TEST",recreated.EXTRA_TEXT);
        assertEquals(recreated.ticketText.equals(np.ticketText), recreated.ticketText.equals(np.ticketText));
    }
    */
}
