package com.ubiqlog.ubiqlogwear.ui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ubiqlog.ubiqlogwear.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Manouchehr on 2/13/2015.
 */
public class wActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chart);

        //set Title of activity
        TextView tvTitle = (TextView) findViewById(R.id.tvTitleChart);
        tvTitle.setText(R.string.title_activity_wactivity);

        Date date = new Date();
        displayData(date);

    }

    private void displayData(Date date) {
        boolean isEnabled_walk = true;
        boolean isEnabled_run = true;
        boolean isEnabled_vehicle = true;
        boolean isEnabled_bicycle = true;

        List<String> activities_list = new ArrayList<String>();
        if (isEnabled_walk) activities_list.add("Walk");
        if (isEnabled_run) activities_list.add("Run");
        if (isEnabled_vehicle) activities_list.add("Vehicle");
        if (isEnabled_bicycle) activities_list.add("Bicycle");

        if (activities_list.size() <= 0) return;

        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        final LinearLayout frameBox = (LinearLayout) findViewById(R.id.frameBox);


        // remove all added views before except linksbox
        frameBox.removeViewsInLayout(0, frameBox.getChildCount() - 1);

        FrameLayout chart;
        LinearLayout.LayoutParams cParams;

        for (int i = 0; i < activities_list.size(); i++) {
            chart = new FrameLayout(this);
            chart.removeAllViews();
            cParams = new LinearLayout.LayoutParams(getSizeInDP(190), getSizeInDP(50));
            cParams.gravity = (Gravity.TOP | Gravity.CENTER_HORIZONTAL);

            boolean showFooter = false;
            boolean showHeader = false;

            if (i == 0) {
                //first item
                showHeader = true;
                cParams.setMargins(0, 0, 0, -12);

            } else if (i == activities_list.size() - 1) {
                //last item
                showFooter = true;
                cParams.setMargins(0, -12, 0, 0);

            } else {
                //middle items
                cParams.setMargins(0, -12, 0, -12);

            }
            chart.setLayoutParams(cParams);
            chart.setPadding(12, -5, 5, -5);
            chart.addView(createGraph(date, activities_list.get(i), showFooter, showHeader));
            frameBox.addView(chart, i);
        }

        final TextView tvDate = new TextView(this);
        tvDate.setText(new SimpleDateFormat("MM/dd/yyyy").format(date));
        tvDate.setTextSize(getSizeInDP(8));

        //  frameBox.addView(tvDate, 0);

        //render Links box
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.setMargins(0, 0, 0, 0);

        final LinearLayout linksBox = (LinearLayout) findViewById(R.id.linksBox);
        linksBox.setLayoutParams(params);

        // create links to some dates
        for (int i = 0; i < 7; i++) {
            final Button btn1 = new Button(this);
            final Date tmpDate = new Date(date.getTime() - (i + 1) * 24 * 60 * 60 * 1000);
            btn1.setText(new SimpleDateFormat("MM/dd/yyyy").format(tmpDate));
            btn1.setBackgroundColor(getResources().getColor(R.color.chart_button_bgcolor));
            btn1.setBackground(getResources().getDrawable(R.drawable.listview_bg_title));
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

    private View createGraph(Date date, String title, boolean isVisibleFooter, boolean isVisibleHeader) {
        Log.i("Activity " + title, "In Create Chart");
        // We start creating the XYSeries to plot the temperature
        XYSeries series1 = new XYSeries("Activity " + title);


        // We start filling the series
        // with random values for Y:0-30 to X:0-24
        Random rand = new Random();
        for (int i = 1; i < 23; i++) {
            series1.add(i, rand.nextInt(30));
        }

        XYSeriesRenderer renderer1 = new XYSeriesRenderer();
        renderer1.setLineWidth(getResources().getInteger(R.integer.chart_line_width));
        renderer1.setColor(getResources().getColor(R.color.chart_line_color));
        //renderer1.setDisplayBoundingPoints(true);
        //renderer1.setPointStyle(PointStyle.CIRCLE);
        //renderer1.setPointStrokeWidth(2);


        // Now we add our series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series1);

        // Finaly we create the multiple series renderer to control the graph
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer1);
        mRenderer.setYAxisMin(0);
        mRenderer.setYLabels(0);
        mRenderer.addYTextLabel(0, title);
        mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setYLabelsPadding(-1f);

        mRenderer.setXAxisMin(0);
        mRenderer.setXAxisMax(23);

        if (isVisibleHeader)
            mRenderer.setChartTitle(new SimpleDateFormat("MM/dd/yyyy").format(date));

        if (isVisibleFooter) {
            mRenderer.addXTextLabel(0, "00:00");
            mRenderer.addXTextLabel(12, "12:00");
            mRenderer.addXTextLabel(23, "23:59");
        }


        mRenderer.setBarSpacing(0.25);

        mRenderer.setXLabelsAlign(Paint.Align.CENTER);
        mRenderer.setXLabels(0);
        mRenderer.setShowAxes(false);
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
        // Disable Pan on two axis
        mRenderer.setPanEnabled(false, false);
        mRenderer.setShowGrid(false);
        mRenderer.setBackgroundColor(Color.WHITE);
        mRenderer.setMarginsColor(Color.WHITE);
        mRenderer.setAxesColor(Color.BLACK);
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setShowLegend(false);//hide info label
        mRenderer.setLabelsColor(getResources().getColor(R.color.chart_labels_color));
        mRenderer.setXLabelsColor(getResources().getColor(R.color.chart_labels_color));
        mRenderer.setYLabelsColor(0, getResources().getColor(R.color.chart_labels_color));
        GraphicalView chartView = ChartFactory.getBarChartView(this, dataset, mRenderer, BarChart.Type.DEFAULT);
        return chartView;
    }

    private int getSizeInDP(int x) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x, getResources().getDisplayMetrics());
    }
}
