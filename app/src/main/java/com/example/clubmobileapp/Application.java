package com.example.clubmobileapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Application extends android.app.Application {
    public static final String CHANNEL_ONE_ID = "channelOne";
    public static final String CHANNEL_TWO_ID = "channelTwo";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannelOne = new NotificationChannel(CHANNEL_ONE_ID, "Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannelOne.setDescription("You will be notified of meetings to attend");

            NotificationChannel notificationChannelTwo = new NotificationChannel(CHANNEL_ONE_ID, "Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannelTwo.setDescription("You have a meeting to attend in a few moment time");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannelOne);
            manager.createNotificationChannel(notificationChannelTwo);
        }
    }
}
