package com.ubiqlog.ubiqlogwear.ui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.ubiqlog.ubiqlogwear.R;
import com.ubiqlog.ubiqlogwear.sensors.ActivityDataHelper;

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
import java.util.concurrent.TimeUnit;

/**
 * Created by Manouchehr on 2/13/2015.
 */
public class wActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = this.getClass().getSimpleName();
    private GoogleApiClient mFitnessClient;
    private ActivityDataHelper.StepList stepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chart);

        Date date = new Date();

        buildFitnessActivity();
        stepList = new ActivityDataHelper.StepList(this);


        //set Title of activity
        TextView tvTitle = (TextView) findViewById(R.id.tvTitleChart);
        tvTitle.setText(R.string.title_activity_wactivity);

        displayData(date);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mFitnessClient != null) {
            mFitnessClient.connect();
        }
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

        final TextView tvDate = (TextView) findViewById(R.id.tvDate);
        tvDate.setText(new SimpleDateFormat("MM/dd/yyyy").format(date));

        final TextView tvLastSync = (TextView) findViewById(R.id.tvLastSync);
        tvLastSync.setText("Last Sync: " + new SimpleDateFormat("MM/dd/yyyy hh:mm").format(date));

        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        final LinearLayout frameBox = (LinearLayout) findViewById(R.id.frameBox);

        // remove all added views before except linksbox and tvLastSync label
        frameBox.removeViewsInLayout(1, frameBox.getChildCount() - 2);

        FrameLayout chart;
        LinearLayout.LayoutParams cParams;

        for (int i = 0; i < activities_list.size(); i++) {
            chart = new FrameLayout(this);
            chart.removeAllViews();
            cParams = new LinearLayout.LayoutParams(getSizeInDP(190), getSizeInDP(50));
            cParams.gravity = (Gravity.TOP | Gravity.CENTER_HORIZONTAL);

            boolean showFooter = false;

            if (i == 0) {
                //first item
                cParams.setMargins(0, 0, 0, -10);// setMargins(left, top, right, bottom)

            } else if (i == activities_list.size() - 1) {
                //last item
                showFooter = true;
                cParams.setMargins(0, -12, 0, -5);

            } else {
                //middle items
                cParams.setMargins(0, -12, 0, -10);

            }
            chart.setLayoutParams(cParams);
            chart.setPadding(15, -5, 5, -5); // setPadding(left, top, right, bottom)
            chart.addView(createGraph(date, activities_list.get(i), showFooter));
            frameBox.addView(chart, i + 1);
        }


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

    private View createGraph(Date date, String title, boolean isVisibleFooter) {
        Log.i("Activity " + title, "In Create Chart");

        XYSeries series1 = new XYSeries("Activity " + title);

        // filling the series with random values for Y:0-30 to X:0-24
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


        // add series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series1);

        // create the multiple series renderer to control the graph
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer1);
        mRenderer.setYAxisMin(0);
        mRenderer.setYLabels(0);
        mRenderer.addYTextLabel(0, title); // set Title in the middle of chart
        mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setYLabelsPadding(-1f);

        mRenderer.setXAxisMin(0);
        mRenderer.setXAxisMax(23);

        if (isVisibleFooter) {
            mRenderer.addXTextLabel(0, "00:00");
            mRenderer.addXTextLabel(12, "12:00");
            mRenderer.addXTextLabel(23, "23:59");
        }


        mRenderer.setBarSpacing(0.25);

        mRenderer.setXLabelsAlign(Paint.Align.CENTER);
        mRenderer.setXLabels(0);
        mRenderer.setShowAxes(false);
        mRenderer.setLabelsTextSize(getResources().getDimension(R.dimen.textsize_s1));

        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins

        mRenderer.setPanEnabled(false, false);// Disable Pan on two axis
        mRenderer.setShowGrid(false);
        mRenderer.setBackgroundColor(Color.WHITE);
        mRenderer.setMargins(new int[]{5, 38, 10, 30}); //setMargins(top, left, bottom, right) defaults(20,30,10,20)
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


    private void buildFitnessActivity() {
        mFitnessClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.API)
                .addScope(Fitness.SCOPE_ACTIVITY_READ)
                .addScope(Fitness.SCOPE_BODY_READ_WRITE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Connected to Fitness API");
        invokeFitnessApi();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.getErrorCode());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(wActivity.this, "Connect account with handheld device", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void invokeFitnessApi() {
        setupSensorRequest();

    }

    private void setupSensorRequest() {
        SensorRequest req = new SensorRequest.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .setSamplingRate(1, TimeUnit.SECONDS)
                .build();

        PendingResult<Status> regResult =
                Fitness.SensorsApi.add(mFitnessClient, req, new DataSourceListener());

    }

    private class DataSourceListener implements OnDataPointListener {
        @Override
        public void onDataPoint(DataPoint dataPoint) {
            for (Field field : dataPoint.getDataType().getFields()) {
                final Value val = dataPoint.getValue(field); //Culm amount of steps
                if (val != null) {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityDataHelper.Step newStep = new ActivityDataHelper.Step(val.asInt(), new Date());

                            //This method writes to file when walking gap conditions are met
                            stepList.insert(newStep);
                            Toast.makeText(wActivity.this, "Steps" + val.asInt(), Toast.LENGTH_LONG).show();
                            /*
                            if (mTextView != null) {
                                mTextView.setText(val.asInt() + " steps");

                            }*/
                        }
                    });
                }
                Log.d(TAG, "Detected datapoint field: " + field.getName());
                Log.d(TAG, "Detected datapoint value: " + val);
            }
        }
    }

}
