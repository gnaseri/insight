package com.ubiqlog.ubiqlogwear.test;

import android.os.Environment;
import android.test.InstrumentationTestCase;

import java.io.File;

/**
 * Created by User on 2/24/15.
 */
public class StorageTest extends InstrumentationTestCase {

    private File file;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        file = new File(Environment.getExternalStorageDirectory() + "/ubiqlog");

    }

    public void testFile(){
        file.mkdirs();

        assertEquals(true, file.exists());
    }
}
