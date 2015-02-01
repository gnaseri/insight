package com.ubiqlog.ubiqlogwear.ui.dummy;

/**
 * Created by Manouchehr on 1/22/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ubiqlog.ubiqlogwear.R;

public class MyListAdapter extends BaseAdapter {

    private Activity activity;
    private Object[] data;
    private static LayoutInflater inflater = null;

    public MyListAdapter(Activity a, Object[] d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View iRow = convertView;
        if (convertView == null)
            iRow = inflater.inflate(R.layout.activity_barlist_row, null);

        ImageView item_StatusImage = (ImageView) iRow.findViewById(R.id.ivStatus);
        TextView item_Time = (TextView) iRow.findViewById(R.id.tvTime);
        TextView item_Date = (TextView) iRow.findViewById(R.id.tvDate);
        TextView item_Title = (TextView) iRow.findViewById(R.id.tvTitle);


        //map data to views: data[position].time, data[position].date, data[position].text, data[position].icon
        item_Time.setText("23:55");
        item_Date.setText("01-02-2015");
        item_Title.setText(data[position].toString());


        //set status image's background
        item_StatusImage.setBackground(activity.getResources().getDrawable(getBackgroundImage(getCount(), position)));

        //display a status icon beside of each event
        //#if (data[position].icon == null)
        item_StatusImage.setImageResource(R.drawable.baricons_bullet);
        //#else
        //#    item_StatusImage.setImageResource(data[position].icon);

        item_StatusImage.setPadding(6, 6, 6, 6);

        return iRow;
    }

    public int getBackgroundImage(int count, int position) {
        if (position == 0)
            return R.drawable.baricons_start;
        else if (position == count - 1)
            return R.drawable.baricons_end;
        else
            return R.drawable.baricons_mid;


    }
}