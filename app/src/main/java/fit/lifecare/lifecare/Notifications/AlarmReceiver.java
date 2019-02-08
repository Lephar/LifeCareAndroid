package fit.lifecare.lifecare.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import fit.lifecare.lifecare.MainActivity;
import fit.lifecare.lifecare.R;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static fit.lifecare.lifecare.Notifications.NotificationChannels.CHANNEL_1_ID;
import static fit.lifecare.lifecare.Notifications.NotificationChannels.CHANNEL_2_ID;
import static fit.lifecare.lifecare.Notifications.NotificationChannels.CHANNEL_3_ID;
import static fit.lifecare.lifecare.Notifications.NotificationChannels.CHANNEL_4_ID;
import static fit.lifecare.lifecare.Notifications.NotificationChannels.CHANNEL_5_ID;

public class AlarmReceiver extends BroadcastReceiver {
    
    private NotificationManager notificationManager;
    
    // shared preferences
    private SharedPreferences preferences;
    
    public AlarmReceiver() {
    
    }
    
    
    @Override
    public void onReceive(Context context, Intent intent) {
        
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        
        String type = intent.getSerializableExtra("type").toString();
        
        Log.d("notasd", "bu :" + type);
        
        //initialize shared preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        
        boolean showNotification = preferences.getBoolean("SHOW_NOTIFICATION", true);
        
        if (showNotification) {
            if (type.equals("su")) {
                
                Intent notificationIntent = new Intent(context, MainActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                
                notificationIntent.putExtra("start_where", "main_screen");
                
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 1,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                sendNotify(pendingIntent, context, CHANNEL_1_ID, context.getString(R.string.dont_forget_drink), 1);
                
            } else if (type.equals("breakfast")) {
                
                Intent notificationIntent = new Intent(context, MainActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                
                notificationIntent.putExtra("start_where", "meal_schedule");
                
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 3,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                
                sendNotify(pendingIntent, context, CHANNEL_3_ID, context.getString(R.string.breakfast_reminder), 3);
                
            } else if (type.equals("lunch")) {
                
                Intent notificationIntent = new Intent(context, MainActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                
                notificationIntent.putExtra("start_where", "meal_schedule");
                
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 4,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                
                sendNotify(pendingIntent, context, CHANNEL_4_ID, context.getString(R.string.lunch_reminder), 4);
                
            } else if (type.equals("dinner")) {
                
                Intent notificationIntent = new Intent(context, MainActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                
                notificationIntent.putExtra("start_where", "meal_schedule");
                
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 5,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                
                sendNotify(pendingIntent, context, CHANNEL_5_ID, context.getString(R.string.dinner_reminder), 5);
                
            } else {
                
                Intent notificationIntent = new Intent(context, MainActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                
                notificationIntent.putExtra("start_where", "profile");
                
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 2,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                sendNotify(pendingIntent, context, CHANNEL_2_ID, context.getString(R.string.dont_forget_profile), 2);
            }
        }
        
    }
    
    private void sendNotify(PendingIntent pendingIntent, Context context, String channel_id, String context_text, int id) {
        
        Notification notification = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Lifecare")
                .setContentText(context_text)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        
        notificationManager.notify(id, notification);
    }
    
    
//    private void sendNotificationForWater(PendingIntent pendingIntent, Context context) {
//
//        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentTitle("Lifecare")
//                .setContentText(context.getString(R.string.dont_forget_drink))
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent)
//                .build();
//
//        notificationManager.notify(1, notification);
//    }
//
//    private void sendNotificationForProfile(PendingIntent pendingIntent, Context context) {
//
//        Notification notification = new NotificationCompat.Builder(context, CHANNEL_2_ID)
//                .setSmallIcon(R.mipmap.ic_launcher_foreground)
//                .setContentTitle("Lifecare")
//                .setContentText(context.getString(R.string.dont_forget_profile))
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent)
//                .build();
//
//        notificationManager.notify(2, notification);
//    }
}
