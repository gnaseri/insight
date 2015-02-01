package com.ubiqlog.ubiqlogwear.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ubiqlog.ubiqlogwear.R;
import com.ubiqlog.ubiqlogwear.ui.dummy.MyListAdapter;


public class wNotifications extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barlist);

        //set Titlebar Title
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.title_activity_wnotifications);

        //set Titlebar Logo
        ImageView tvTitleLogo = (ImageView) findViewById(R.id.ivTitleLogo);
        tvTitleLogo.setImageResource(R.drawable.notificn2);

        //test data
        String data[] = new String[]{"Facebook", "Email", "Battery", "Email", "Email", "Viber"};

        //map data to layout
        ListView list = (ListView) findViewById(R.id.listView);
        MyListAdapter adapter = new MyListAdapter(this, data);
        list.setAdapter(adapter);

    }

}