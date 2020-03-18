package com.example.newboedoserver.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.newboedoserver.Common.Common;
import com.example.newboedoserver.Helper.NotificationHelper;
import com.example.newboedoserver.MainActivity;
import com.example.newboedoserver.Model.Token;
import com.example.newboedoserver.OrderStatus;
import com.example.newboedoserver.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        String tokenRefreshed=FirebaseInstanceId.getInstance().getToken();
        if (Common.currentUser != null)
            updateTokenToFirebase(tokenRefreshed);
    }

    private void updateTokenToFirebase(String tokenRefreshed) {
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token token = new Token(tokenRefreshed,true);
        tokens.child(Common.currentUser.getPhone()).setValue(token);

    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q)
        sendNotificationAPI29(remoteMessage);
            sendNotification(remoteMessage);
    }

    private void sendNotificationAPI29(RemoteMessage remoteMessage) {
        RemoteMessage.Notification  notification = remoteMessage.getNotification();
        String title=notification.getTitle();
        String content=notification.getBody();

        PendingIntent pendingIntent;


        Intent intent = new Intent(this, OrderStatus.class);
        intent.putExtra(Common.PHONE_TEXT,Common.currentUser.getPhone());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationHelper helper = new NotificationHelper(this);
        Notification.Builder builder =helper.getNewBoedoChannelNotification(title,content,pendingIntent,defaultSoundUri);

        helper.getManager().notify(new Random().nextInt(),builder.build());
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        String Notification_channel_id= "com.example.newboedoserver.Service.MyFirebaseMessagingService";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            NotificationChannel channel = new NotificationChannel(Notification_channel_id,
                    "Notification", NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription("newboedo");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);

            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }

        RemoteMessage.Notification notification=remoteMessage.getNotification();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder= new NotificationCompat.Builder(this,Notification_channel_id);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(),builder.build());
    }
}
