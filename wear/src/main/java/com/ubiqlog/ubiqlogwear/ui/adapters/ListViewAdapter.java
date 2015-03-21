package com.ubiqlog.ubiqlogwear.ui.adapters;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubiqlog.ubiqlogwear.R;
import com.ubiqlog.ubiqlogwear.ui.MainListItem;

/**
 * Created by CM on 2/4/15.
 */
public class ListViewAdapter extends WearableListView.Adapter {
    private MainListItem[] mDataset;
    private final Context mContext;
    private final LayoutInflater mInflater;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListViewAdapter(Context context, MainListItem[] dataset) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDataset = dataset;
    }

    // Provide a reference to the type of views you're using
    public static class ItemViewHolder extends WearableListView.ViewHolder {
        private TextView textView;
        private ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            // find the text view within the custom item's layout
            textView = (TextView) itemView.findViewById(R.id.name);
            imageView = (ImageView) itemView.findViewById(R.id.circle);
        }
    }

    // Create new views for list items
    // (invoked by the WearableListView's layout manager)
    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // Inflate our custom layout for list items
        return new ItemViewHolder(mInflater.inflate(R.layout.list_item, null));
    }

    // Replace the contents of a list item
    // Instead of creating new views, the list tries to recycle existing ones
    // (invoked by the WearableListView's layout manager)
    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder,
                                 int position) {
        // retrieve the text view
        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        TextView view = itemHolder.textView;
        // replace text contents with title
        view.setText(mDataset[position].getTitle());

        //retrieve imageView
        ImageView imageView = itemHolder.imageView;
        if (mDataset[position].getImgId() != null) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(mDataset[position].getImgId());
        }
        else
            imageView.setVisibility(View.GONE);
        // replace list item's metadata
        //holder.itemView.setTag(position);

        //store the object as the tag
        holder.itemView.setTag(mDataset[position]);
    }

    // Return the size of your dataset
    // (invoked by the WearableListView's layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
