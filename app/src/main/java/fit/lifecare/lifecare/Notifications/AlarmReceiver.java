package fit.lifecare.lifecare.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import fit.lifecare.lifecare.MainActivity;
import fit.lifecare.lifecare.R;

import static android.content.Context.NOTIFICATION_SERVICE;
import static fit.lifecare.lifecare.Notifications.NotificationChannels.CHANNEL_1_ID;
import static fit.lifecare.lifecare.Notifications.NotificationChannels.CHANNEL_2_ID;

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManagerCompat notificationManagerCompat;
    private NotificationManager notificationManager;
    private Context context;

    private PendingIntent pendingIntent;

    public AlarmReceiver() {

    }


    @Override
    public void onReceive(Context context, Intent intent) {

        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        this.context = context;

        String type = intent.getSerializableExtra("type").toString();

        Log.d("notasd", "bu :" + type);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        if (type.equals("su")) {

            pendingIntent = PendingIntent.getActivity(context, 1,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            sendNotificationForWater();

        } else {

            pendingIntent = PendingIntent.getActivity(context, 2,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            sendNotificationForProfile();
        }

    }


    private void sendNotificationForWater() {

        Notification notification = new NotificationCompat.Builder(context,CHANNEL_1_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Lifecare")
                .setContentText("Su içmeyi unutmayın")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(1, notification);
    }

    private void sendNotificationForProfile() {

        Notification notification = new NotificationCompat.Builder(context,CHANNEL_2_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("Lifecare")
                .setContentText("Profilinizi eksiksiz doldurun.")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(2, notification);
    }
}
