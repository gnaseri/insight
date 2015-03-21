package com.ubiqlog.ubiqlogwear.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.ubiqlog.ubiqlogwear.R;


public class GooglePlayServicesActivity extends Activity  {
    private static final String TAG = GooglePlayServicesActivity.class.getSimpleName();

    private static final String FITNESS_IN_RES = "is_in_resolution";

    /**
     * Request code for auto Google Play Services error resolution.
     */
    protected static final int REQUEST_FITNESS_RES = 2;

    /**
     * Google API client.
     */

    private GoogleApiClient mFitnessClient;

    private TextView statusTv;
    /**
     * Determines if the client is in a resolution state, and
     * waiting for resolution intent to return.
     */
    private boolean mFitnessIsInRes;

    /**
     * Called when the activity is starting. Restores the activity state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mFitnessIsInRes = savedInstanceState.getBoolean(FITNESS_IN_RES,false);
        }
        setContentView(R.layout.mobile_fitness_authorize_activity);

        statusTv = (TextView) findViewById(R.id.status_tv);
    }

    /**
     * Called when the Activity is made visible.
     * A connection to Play Services need to be initiated as
     * soon as the activity is visible. Registers {@code ConnectionCallbacks}
     * and {@code OnConnectionFailedListener} on the
     * activities itself.
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (mFitnessClient == null){
            buildFitnessClient();
        }
        mFitnessClient.connect();
    }

    /**
     * Called when activity gets invisible. Connection to Play Services needs to
     * be disconnected as soon as an activity is invisible.
     */
    @Override
    protected void onStop() {
        if (mFitnessClient != null){
            mFitnessClient.disconnect();
        }
        super.onStop();
    }

    /**
     * Saves the resolution state.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FITNESS_IN_RES,mFitnessIsInRes);
    }

    /**
     * Handles Google Play Services resolution callbacks.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_FITNESS_RES:
                retryFitnessConnect();
                break;
        }
    }

    private void buildFitnessClient(){
        mFitnessClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.API)
                .addScope(Fitness.SCOPE_ACTIVITY_READ)
                .addScope(Fitness.SCOPE_BODY_READ_WRITE)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.d(TAG, "Fitness client connected");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                statusTv.setText("Connected handheld to fit api");
                            }
                        });

                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d(TAG, "Fitness client suspended");
                        retryFitnessConnect();

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG, "Fitness Connection failed");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                statusTv.setText("Failed to Connect handheld to fit api");
                            }
                        });

                        if (!result.hasResolution()) {
                            // Show a localized error dialog.
                            GooglePlayServicesUtil.getErrorDialog(
                                    result.getErrorCode(), GooglePlayServicesActivity.this, 0, new OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            retryFitnessConnect();
                                        }
                                    }).show();
                            return;
                        }
                        if (mFitnessIsInRes){
                            return;
                        }

                        try {
                            result.startResolutionForResult(GooglePlayServicesActivity.this, REQUEST_FITNESS_RES);
                        } catch (SendIntentException e) {
                            Log.e(TAG, "Exception while starting resolution activity", e);
                            retryFitnessConnect();
                        }
                    }
                })
                .build();
    }
    public void retryFitnessConnect(){
        mFitnessIsInRes = false;
        if (!mFitnessClient.isConnecting()) {
            mFitnessClient.connect();
        }
    }
}
