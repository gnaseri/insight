package com.insight.insight.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.insight.insight.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by Manouchehr on 2/4/2015.
 */
public class MultipleListItemsAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_LinkButton = 1;
    private static final int TYPE_MAX_COUNT = 2;

    private ArrayList mData = new ArrayList();
    private LayoutInflater mInflater;
    private Activity activity;
    private TreeSet mLinkButtonsSet = new TreeSet();

    public MultipleListItemsAdapter(Activity activity) {
        this.activity = activity;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final RowData item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addLinkButtonItem(final String item) {
        mData.add(item);
        // save mLinkButtonsSet position
        mLinkButtonsSet.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mLinkButtonsSet.contains(position) ? TYPE_LinkButton : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            //cache views for performance
            holder = new ViewHolder();
            switch (type) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.activity_datalist_row, null);
                    holder.item_StatusImage = (ImageView) convertView.findViewById(R.id.ivStatus);
                    holder.item_Time = (TextView) convertView.findViewById(R.id.tvTime);
                    holder.item_Date = (TextView) convertView.findViewById(R.id.tvDate);
                    holder.item_Title = (TextView) convertView.findViewById(R.id.tvTitle);
                    break;

                case TYPE_LinkButton:
                    convertView = mInflater.inflate(R.layout.activity_linkslist_row, null);
                    holder.link_Text = (TextView) convertView.findViewById(R.id.tvLinkText);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //map data to views
        switch (type) {
            case TYPE_ITEM:
                RowData rData = (RowData) mData.get(position);
                holder.item_Title.setText(rData.statusText);
                holder.item_Date.setText(new SimpleDateFormat("MM/dd/yyyy").format(rData.time));
                holder.item_Time.setText(new SimpleDateFormat("hh:mm").format(rData.time));
                holder.item_StatusImage.setBackground(activity.getResources().getDrawable(getBackgroundImage(getCount(), position)));
                holder.item_StatusImage.setPadding(6, 6, 6, 6);
                if (Integer.valueOf(rData.icon).equals(null))
                    holder.item_StatusImage.setImageResource(R.drawable.ic_bar_bullet);
                else
                    holder.item_StatusImage.setImageResource(rData.icon);
                break;

            case TYPE_LinkButton:
                String linkText = (String) mData.get(position);
                holder.link_Text.setText(linkText);
                holder.link_Text.setTag(linkText);//set tag property to discover which button clicked on activity side
                break;
        }
        return convertView;
    }

    public static class ViewHolder {
        // Data Items views
        public TextView item_Time;
        public TextView item_Date;
        public TextView item_Title;
        public ImageView item_StatusImage;

        //LinkButton views
        public TextView link_Text;
    }

    public int getBackgroundImage(int count, int position) {
        if (position == 0)
            return R.drawable.ic_bar_start;
        else if (position == count - mLinkButtonsSet.size() - 1)
            return R.drawable.ic_bar_end;
        else
            return R.drawable.ic_bar_mid;
    }
}

