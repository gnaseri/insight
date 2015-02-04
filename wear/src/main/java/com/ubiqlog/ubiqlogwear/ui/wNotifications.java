package com.ubiqlog.ubiqlogwear.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ubiqlog.ubiqlogwear.R;
import com.ubiqlog.ubiqlogwear.utils.MultipleListItemsAdapter;
import com.ubiqlog.ubiqlogwear.utils.RowData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class wNotifications extends Activity {
    private MultipleListItemsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datalist);

        //set Title of activity
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.title_activity_wnotifications);

        displayDataList(new Date());
    }

    public void displayDataList(Date date) {
        mAdapter = new MultipleListItemsAdapter(this);

        //generate sample data
        for (RowData rData : generateTestData(date)) {
            mAdapter.addItem(rData);
        }

        //generate sample dates(past three days) for links buttons
        for (int i = 0; i < 3; i++)
            mAdapter.addLinkButtonItem(new SimpleDateFormat("MM/dd/yyyy").format(new Date(date.getTime() - (i + 1) * 24 * 60 * 60 * 1000)));

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

    public List<RowData> generateTestData(Date time) {
        List<RowData> rDatas = new ArrayList<RowData>();
        String status[] = {"Facebook", "Viber", "Battery", "Email", "Unknown"};
        for (int j = 0; j < 10; j++) {
            Random randomGenerator = new Random();
            RowData rData = new RowData(new Date(time.getTime() + j * 3 * 60 * 1000), status[randomGenerator.nextInt(3)], R.drawable.baricons_bullet);
            rDatas.add(rData);
        }
        return rDatas;

    }
}