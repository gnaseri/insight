package com.ubiqlog.ubiqlogwear.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.ubiqlog.ubiqlogwear.R;

/**
 * Created by Manouchehr on 3/10/2015.
 */
public class PredictionNotification {

    // show(this,"Caution","You are using battery lower than average!");
    public void show(Activity context, String title, String description) {
        Bitmap background = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_notifications);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(description)
                        .setPriority(2)
                        .setLargeIcon(background);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);

        // Build the notification and issues it with notification manager.
        notificationManager.notify(001, notificationBuilder.build());
    }
}
