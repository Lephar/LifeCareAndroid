package fit.lifecare.lifecare.Notifications;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationChannels extends Application {


    public static final String CHANNEL_1_ID = "channel_for_water_reminder";
    public static final String CHANNEL_2_ID = "channel_for_profile_reminder";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            NotificationChannel notificationChannel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "channelWater",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationChannel1.setDescription("Channel for water reminder");


            NotificationChannel notificationChannel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "channelProfile",
                    NotificationManager.IMPORTANCE_LOW
                    );
            notificationChannel2.setDescription("Channel for water reminder");


            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel1);
            notificationManager.createNotificationChannel(notificationChannel2);
        }
    }
}
