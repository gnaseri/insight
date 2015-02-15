package com.ubiqlog.ubiqlogwear.ui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ubiqlog.ubiqlogwear.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Manouchehr on 2/13/2015.
 */
public class wHeartRate extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chart);

        //set Title of activity
        TextView tvTitle = (TextView) findViewById(R.id.tvTitleChart);
        tvTitle.setText(R.string.title_activity_wheartrate);

        Date date = new Date();
        displayData(date);

    }

    private void displayData(Date date) {
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        final FrameLayout chart = (FrameLayout) findViewById(R.id.chart);
        chart.addView(createGraph(date));

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
        Log.i("Heart Rate", "In Create Chart");
        // We start creating the XYSeries to plot the temperature
        XYSeries series1 = new XYSeries("Heart Rate");


        // We start filling the series
        // with random values for Y:0-100 to X:0-24
        Random rand = new Random();
        for (int i = 1; i < 23; i++) {
            series1.add(i,rand.nextInt(50));
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

        mRenderer.addXTextLabel(0, "00:00");
        mRenderer.addXTextLabel(6, "06:00");
        mRenderer.addXTextLabel(12, "12:00");
        mRenderer.addXTextLabel(18, "18:00");
        mRenderer.addXTextLabel(23, "23:59");

        mRenderer.setXAxisMin(0);
        mRenderer.setXAxisMax(23);
        mRenderer.setXLabelsAlign(Paint.Align.LEFT);
        mRenderer.setXLabels(0);

        mRenderer.setYAxisMin(0);
        mRenderer.setYLabelsAlign(Paint.Align.CENTER);

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
        GraphicalView chartView = ChartFactory.getBarChartView(this, dataset, mRenderer, BarChart.Type.DEFAULT);
        return chartView;
    }
}
