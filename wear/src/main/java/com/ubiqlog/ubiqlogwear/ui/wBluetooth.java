package com.ubiqlog.ubiqlogwear.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ubiqlog.ubiqlogwear.R;
import com.ubiqlog.ubiqlogwear.common.Setting;
import com.ubiqlog.ubiqlogwear.utils.IOManager;
import com.ubiqlog.ubiqlogwear.utils.JSONUtil;
import com.ubiqlog.ubiqlogwear.utils.MultipleListItemsAdapter;
import com.ubiqlog.ubiqlogwear.utils.RowData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class wBluetooth extends Activity {
    private static final String LOG_TAG = wBluetooth.class.getSimpleName();
    private MultipleListItemsAdapter mAdapter;

    JSONUtil jsonUtil = new JSONUtil();
    IOManager ioManager = new IOManager();
    File[] lastDataFilesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datalist);


        //set Title of activity
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.title_activity_wbluetooth);

        Date date;
        lastDataFilesList = ioManager.getLastFilesInDir(Setting.dataFilename_Bluetooth, Setting.linksButtonCount);
        if (lastDataFilesList != null && lastDataFilesList.length > 0)
            date = ioManager.parseDataFilename2Date(lastDataFilesList[0].getName());
        else
            date = new Date();

        displayDataList(date);
    }

    public void displayDataList(Date date) {
        mAdapter = new MultipleListItemsAdapter(this);

        //generate sample data
        for (RowData rData : getDataFromFile(date)) {
            mAdapter.addItem(rData);
        }

        // create links to datefiles
        for (File file : lastDataFilesList) {
            final Date tmpDate = ioManager.parseDataFilename2Date(file.getName());
            mAdapter.addLinkButtonItem(new SimpleDateFormat("MM/dd/yyyy").format(tmpDate));
        }

        //set adapter to listView
        ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(mAdapter);

        // list item's 'clicable' property on activity is disabled. User can only click on 'LinkButtons'
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedDate = view.findViewById(R.id.tvLinkText).getTag().toString();
                try {
                    Date date = new SimpleDateFormat("MM/dd/yyyy").parse(selectedDate);
                    displayDataList(date);

                } catch (ParseException e) {
                }
            }
        });
    }

    public List<RowData> getDataFromFile(Date date) {
        List<RowData> rDatas = new ArrayList<RowData>();

        try {
            String sCurrentLine;
            BufferedReader br = new BufferedReader(new FileReader(ioManager.getDataFolderFullPath(Setting.dataFilename_Bluetooth) + Setting.filenameFormat.format(date) + ".txt"));
            while ((sCurrentLine = br.readLine()) != null) {
                Object[] decodedRow = jsonUtil.decodeBT(sCurrentLine); // [0]:Date, [1]:State
                if (decodedRow != null) {
                    Date rowDate = (Date) decodedRow[0];
                    String rowState = String.valueOf(decodedRow[1]);
                    RowData rData = new RowData(rowDate, rowState, R.drawable.ic_bar_bullet);
                    rDatas.add(rData);
                    // Log.d(">>", "ts:" + rowDate.toString() + ", st:" + rowState);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rDatas;
    }
}