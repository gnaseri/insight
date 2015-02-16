package com.ubiqlog.ubiqlogwear.Services;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;

/**
 * Created by User on 2/15/15.
 */
public class GoogleFitConnection implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = HeartRate.class.getSimpleName();
    public GoogleApiClient mClient;
    private Context mContext;

    private static final String FITNESS_IN_RES = "is_in_resolution";

    protected static final int REQUEST_FITNESS_RES = 2;

    public GoogleFitConnection(Context context) {
        this.mContext = context;
    }

    public GoogleApiClient buildFitClient() {
        mClient = new GoogleApiClient.Builder(mContext)
                .addApi(Fitness.API)
                .addScope(Fitness.SCOPE_ACTIVITY_READ)
                .addScope(Fitness.SCOPE_BODY_READ)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        return mClient;

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Connected");
        //do things

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG, "Connection Failed");
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(
                    result.getErrorCode(), (android.app.Activity) mContext, 0, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            retryFitnessConnect();
                        }
                    }).show();
        } else {
            retryFitnessConnect();
        }

    }

    private void retryFitnessConnect() {
        if (!mClient.isConnecting()) {
            mClient.connect();
        }
    }
}
