package com.example.newboedoserver.Helper;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.newboedoserver.R;

public class NotificationHelper extends ContextWrapper {
    private static final String NEW_BOEDO_ID="com.example.newboedoserver.Boedo";
    private static final String NEW_BOEDO_NAME="New Boedo";

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q)
            createChannel();

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void createChannel() {
        NotificationChannel newboedoChannel = new NotificationChannel(NEW_BOEDO_ID,
                NEW_BOEDO_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        newboedoChannel.enableLights(false);
        newboedoChannel.enableVibration(true);
        newboedoChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(newboedoChannel);
    }

    public NotificationManager getManager() {
        if (manager==null)
            manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }

    @TargetApi(Build.VERSION_CODES.Q)
    public android.app.Notification.Builder getNewBoedoChannelNotification(String title, String body, PendingIntent contentIntent, Uri soundUri) {
        return new Notification.Builder(getApplicationContext(), NEW_BOEDO_ID)
                .setContentIntent(contentIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(soundUri)
                .setAutoCancel(false);
    }

    @TargetApi(Build.VERSION_CODES.Q)
    public Notification.Builder edtEatItChannelNotification(String title, String body, Uri soundUri) {
        return new Notification.Builder(getApplicationContext(),NEW_BOEDO_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(soundUri)
                .setAutoCancel(false);
    }
}
