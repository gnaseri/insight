package com.insight.insight.ui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.insight.insight.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Manouchehr on 2/13/2015.
 */
public class AppUsage_Actv extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chart);
        //startService(new Intent(this, AppUsageSensor.class));

        //set Title of activity
        TextView tvTitle = (TextView) findViewById(R.id.tvTitleChart);
        tvTitle.setText(R.string.title_activity_wappusage);

        Date date = new Date();
        displayData(date);

    }

    private void displayData(Date date) {
        final TextView tvDate = (TextView) findViewById(R.id.tvDate);
        tvDate.setText(new SimpleDateFormat("MM/dd/yyyy").format(date));

        final TextView tvLastSync = (TextView) findViewById(R.id.tvLastSync);
        tvLastSync.setText("Last Sync: " + new SimpleDateFormat("MM/dd/yyyy hh:mm").format(date));

        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        final LinearLayout frameBox = (LinearLayout) findViewById(R.id.frameBox);

        // remove all added views before except linksbox and tvLastSync label
        frameBox.removeViewsInLayout(1, frameBox.getChildCount() - 2);

        FrameLayout chart = new FrameLayout(this);
        LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(getSizeInDP(190), getSizeInDP(170));
        cParams.gravity = (Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        chart.setLayoutParams(cParams);
        chart.setPadding(5, 5, 5, 5);
        chart.addView(createGraph(date));
        frameBox.addView(chart, 1);


        // add a cursor point to show the user the scroll feature ////////////////////////////////////////////////////////
        final AnimationSet aniSetCursor = new AnimationSet(true);
        final AlphaAnimation aniAlpha = new AlphaAnimation(1.0f, 0.0f);
        aniAlpha.setDuration(1500);
        aniAlpha.setRepeatCount(2);
        aniAlpha.setFillAfter(true);
        aniSetCursor.addAnimation(aniAlpha);

        TranslateAnimation aniMove = new TranslateAnimation(0.0f, 0.0f, -10.0f, 20.0f);          //  TranslateAnimation(xFrom, xTo, yFrom, yTo)
        aniMove.setDuration(1500);
        aniMove.setRepeatCount(2);
        aniSetCursor.addAnimation(aniMove);

        final ImageView linksCursor = (ImageView) findViewById(R.id.linksCursor);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics); // get screen properties ex. size
        RelativeLayout.LayoutParams linksCursorParams = (RelativeLayout.LayoutParams) linksCursor.getLayoutParams();
        linksCursorParams.setMargins(0, displayMetrics.heightPixels - 35, 0, 0); // set position of cursor in bottom of screen
        linksCursor.setTag(null);
        linksCursor.startAnimation(aniSetCursor);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY();
                if (scrollY > 10 && linksCursor.getTag() == null) {
                    aniAlpha.setRepeatCount(0);
                    linksCursor.startAnimation(aniAlpha);
                    linksCursor.setTag("displayed");
                }
            }
        });


        //render Links box //////////////////////////////////////////////////////////////////////////////////////////
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.setMargins(0, 0, 0, 0);

        final LinearLayout linksBox = (LinearLayout) findViewById(R.id.linksBox);
        linksBox.removeAllViews();
        linksBox.setLayoutParams(params);

        // create links to some dates
        for (int i = 0; i < 7; i++) {
            final Button btn1 = new Button(this);
            final Date tmpDate = new Date(date.getTime() - (i + 1) * 24 * 60 * 60 * 1000);
            btn1.setText(new SimpleDateFormat("MM/dd/yyyy").format(tmpDate));
            btn1.setBackgroundColor(getResources().getColor(R.color.chart_button_bgcolor));
            btn1.setBackground(getResources().getDrawable(R.drawable.listview_bg_title));
            btn1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30));
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    displayData(tmpDate);
                    scrollView.scrollTo(0, 0);
                }
            });
            linksBox.addView(btn1, params);
        }

    }

    private View createGraph(Date date) {
        HashMap<String, Integer> apps = new HashMap<String, Integer>();
        apps.put("Viber", 21);
        apps.put("Facebook", 34);
        apps.put("Chrome", 10);
        apps.put("Instagram", 14);
        apps.put("Skype", 1);
        apps.put("Gmail", 15);

        Log.i("App Usage", "In Create Chart");

        XYSeries series1 = new XYSeries("App Usage");

        XYSeriesRenderer renderer1 = new XYSeriesRenderer();
        renderer1.setLineWidth(getResources().getInteger(R.integer.chart_line_width));
        renderer1.setColor(getResources().getColor(R.color.chart_line_color));
        //renderer1.setDisplayBoundingPoints(true);
        //renderer1.setPointStyle(PointStyle.CIRCLE);
        //renderer1.setPointStrokeWidth(2);


        // create the multiple series renderer to control the graph
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer1);

        //filling the series with random values for Y:0-100 to X:0-24
        int i = 1;
        for (String app : apps.keySet()) {
            series1.add(i, apps.get(app));
            mRenderer.addXTextLabel(i, app);
            i += 1;
        }
        // add an extra record to all Axis be in a good order
        series1.add(i, 0);
        mRenderer.addXTextLabel(i, "");

        mRenderer.setXAxisMin(0);
        mRenderer.setXLabelsAlign(Paint.Align.LEFT);
        mRenderer.setXLabelsPadding(-22f);
        mRenderer.setXLabels(0);

        mRenderer.setYAxisMin(0);
        mRenderer.setYLabelsAlign(Paint.Align.LEFT);
        mRenderer.setYLabelsPadding(0);
        mRenderer.setYTitle("minutes");

        mRenderer.setShowAxes(false);
        mRenderer.setLabelsTextSize(getResources().getDimension(R.dimen.textsize_s1));

        mRenderer.setBarSpacing(0.10);

        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
        // Disable Pan on two axis
        mRenderer.setPanEnabled(false, false);
        mRenderer.setShowGrid(true);
        mRenderer.setShowGridY(true);
        mRenderer.setShowCustomTextGridY(true);
        mRenderer.setGridColor(getResources().getColor(R.color.chart_grids_color));
        mRenderer.setBackgroundColor(Color.WHITE);
        mRenderer.setMargins(new int[]{20,5,10,5});  //setMargins(right, top, left, bottom)! defaults(20,30,10,20)
        mRenderer.setMarginsColor(Color.WHITE);
        mRenderer.setAxesColor(Color.BLACK);
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setShowLegend(false);//hide info label
        mRenderer.setLabelsColor(getResources().getColor(R.color.chart_labels_color));
        mRenderer.setXLabelsColor(getResources().getColor(R.color.chart_labels_color));
        mRenderer.setYLabelsColor(0, getResources().getColor(R.color.chart_labels_color));
        mRenderer.setShowTickMarks(false);
        // Vertical bars
        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.VERTICAL);

        // add series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series1);
        GraphicalView chartView = ChartFactory.getBarChartView(this, dataset, mRenderer, BarChart.Type.DEFAULT);
        return chartView;
    }

    private int getSizeInDP(int x) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x, getResources().getDisplayMetrics());
    }


}
