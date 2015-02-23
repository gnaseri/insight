package com.ubiqlog.ubiqlogwear.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ubiqlog.ubiqlogwear.R;
import com.ubiqlog.ubiqlogwear.sensors.AppUsageSensor2;

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
public class wAppUsage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chart);
        startService(new Intent(this, AppUsageSensor2.class));

        //set Title of activity
        TextView tvTitle = (TextView) findViewById(R.id.tvTitleChart);
        tvTitle.setText(R.string.title_activity_wappusage);

        Date date = new Date();
        displayData(date);

    }

    private void displayData(Date date) {
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        final LinearLayout frameBox = (LinearLayout) findViewById(R.id.frameBox);

        // remove all added views before except linksbox
        frameBox.removeViewsInLayout(0, frameBox.getChildCount() - 1);

        FrameLayout chart = new FrameLayout(this);
        LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(getSizeInDP(190), getSizeInDP(170));
        cParams.gravity = (Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        chart.setLayoutParams(cParams);
        chart.setPadding(5, 5, 5, 5);
        chart.addView(createGraph(date));
        frameBox.addView(chart, 0);

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

        // We start creating the XYSeries to plot the temperature
        XYSeries series1 = new XYSeries("App Usage");

        XYSeriesRenderer renderer1 = new XYSeriesRenderer();
        renderer1.setLineWidth(getResources().getInteger(R.integer.chart_line_width));
        renderer1.setColor(getResources().getColor(R.color.chart_line_color));
        //renderer1.setDisplayBoundingPoints(true);
        //renderer1.setPointStyle(PointStyle.CIRCLE);
        //renderer1.setPointStrokeWidth(2);


        // Finaly we create the multiple series renderer to control the graph
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer1);

        // We start filling the series
        // with random values for Y:0-100 to X:0-24
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
        mRenderer.setXLabels(0);

        mRenderer.setYAxisMin(0);
        mRenderer.setYLabelsAlign(Paint.Align.CENTER);
        mRenderer.setYTitle("minutes");

        mRenderer.setShowAxes(false);
        mRenderer.setLabelsTextSize(getResources().getDimension(R.dimen.textsize_s1));

        mRenderer.setBarSpacing(0.10);

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

        // Vertical bars
        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.VERTICAL);

        mRenderer.setChartTitle(new SimpleDateFormat("MM/dd/yyyy").format(date));

        // Now we add our series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series1);
        GraphicalView chartView = ChartFactory.getBarChartView(this, dataset, mRenderer, BarChart.Type.DEFAULT);
        return chartView;
    }

    private int getSizeInDP(int x) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x, getResources().getDisplayMetrics());
    }
}
