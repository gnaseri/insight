package com.ubiqlog.ubiqlogwear.ui.Layouts;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ubiqlog.ubiqlogwear.R;

/**
 * Created by rawassizadeh on 12/10/14.
 */


/* This class is used to fade the icon and name that aren't selected */
public class WearableListItemLayout extends LinearLayout
        implements WearableListView.OnCenterProximityListener {

    private ImageView mCircle;
    private TextView mName;

    private final float mFadedTextAlpha;
    private final int mFadedCircleColor;
    private final int mChosenCircleColor;

    public WearableListItemLayout(Context context) {
        this(context, null);
    }

    public WearableListItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WearableListItemLayout(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);

        mFadedTextAlpha = getResources()
                .getInteger(R.integer.action_text_faded_alpha) / 100f;
        mFadedCircleColor = getResources().getColor(R.color.grey);
        mChosenCircleColor = getResources().getColor(R.color.blue);
    }

    // Get references to the icon and text in the item layout definition
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // These are defined in the layout file for list items
        // (see next section)
        mCircle = (ImageView) findViewById(R.id.circle);
        mName = (TextView) findViewById(R.id.name);
    }

    @Override
    public void onCenterPosition(boolean animate) {
        mName.setAlpha(1f);
        mCircle.setScaleX(1.5f);
        mCircle.setScaleY(1.5f);
        mCircle.setAlpha(1f);
        //((GradientDrawable) mCircle.getDrawable()).setColor(mChosenCircleColor);
    }

    @Override
    public void onNonCenterPosition(boolean animate) {
        //((GradientDrawable) mCircle.getDrawable()).setColor(mFadedCircleColor);
        mName.setAlpha(mFadedTextAlpha);
        mCircle.setScaleX(1f);
        mCircle.setScaleY(1f);
        mCircle.setAlpha(mFadedTextAlpha);

    }
}