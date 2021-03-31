package com.example.finalwebsockettest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

//https://code.tutsplus.com/tutorials/android-o-how-to-use-notification-channels--cms-28616
//Nastavení chanelu pro notifikace a vzhled samotné notifikace

public class NotificationUtils extends ContextWrapper {

    private NotificationManager mManager;
    public static final String ANDROID_CHANNEL_ID = "com.example.finalwebsockettest.ANDROID";
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationUtils(Context base) {
        super(base);
        createChannels();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels() {

        // Vytvoří android channel
        NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        androidChannel.enableLights(true);
        androidChannel.enableVibration(true);
        androidChannel.setLightColor(Color.GREEN);
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        getManager().createNotificationChannel(androidChannel);

    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    //Nastavení notifikace o vlhkosti

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getAndroidChannelNotification(int moisture) {
        return new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Hey, your plant needs water!")
                .setContentText("Moisture: " + moisture + "%")
                .setColor(Color.GREEN)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setPriority(Notification.PRIORITY_DEFAULT);
    }
}