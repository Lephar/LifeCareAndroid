package fit.lifecare.lifecare.Notifications;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.google.firebase.database.FirebaseDatabase;

public class NotificationChannels extends Application {


    public static final String CHANNEL_1_ID = "channel_for_water_reminder";
    public static final String CHANNEL_2_ID = "channel_for_profile_reminder";
    public static final String CHANNEL_3_ID = "channel_for_breakfast_reminder";
    public static final String CHANNEL_4_ID = "channel_for_lunch_reminder";
    public static final String CHANNEL_5_ID = "channel_for_dinner_reminder";
    public static final String CHANNEL_6_ID = "channel_for_chats";

    @Override
    public void onCreate() {
        super.onCreate();
    
        // enable offline capabilities of firebase
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            NotificationChannel notificationChannel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "channelWater",
                    NotificationManager.IMPORTANCE_LOW
            );
            notificationChannel1.setDescription("Channel for water reminder");


            NotificationChannel notificationChannel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "channelProfile",
                    NotificationManager.IMPORTANCE_DEFAULT
                    );
            notificationChannel2.setDescription("Channel for water reminder");
    
            NotificationChannel notificationChannel3 = new NotificationChannel(
                    CHANNEL_3_ID,
                    "channelBreakfast",
                    NotificationManager.IMPORTANCE_LOW
            );
            notificationChannel3.setDescription("Channel for breakfast reminder");
    
            NotificationChannel notificationChannel4 = new NotificationChannel(
                    CHANNEL_4_ID,
                    "channelLunch",
                    NotificationManager.IMPORTANCE_LOW
            );
            notificationChannel4.setDescription("Channel for lunch reminder");
    
            NotificationChannel notificationChannel5 = new NotificationChannel(
                    CHANNEL_5_ID,
                    "channelDinner",
                    NotificationManager.IMPORTANCE_LOW
            );
            notificationChannel5.setDescription("Channel for dinner reminder");
    
            NotificationChannel notificationChannel6 = new NotificationChannel(
                    CHANNEL_6_ID,
                    "channelChat",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel6.setDescription("Channel for chat messages");


            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel1);
            notificationManager.createNotificationChannel(notificationChannel2);
            notificationManager.createNotificationChannel(notificationChannel3);
            notificationManager.createNotificationChannel(notificationChannel4);
            notificationManager.createNotificationChannel(notificationChannel5);
            notificationManager.createNotificationChannel(notificationChannel6);
        }
    }
}
