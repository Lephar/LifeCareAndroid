package fit.lifecare.lifecare.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import fit.lifecare.lifecare.MainActivity;
import fit.lifecare.lifecare.R;

import static fit.lifecare.lifecare.Notifications.NotificationChannels.CHANNEL_2_ID;
import static fit.lifecare.lifecare.Notifications.NotificationChannels.CHANNEL_6_ID;
import static fit.lifecare.lifecare.Notifications.NotificationChannels.CHANNEL_7_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    
    private static final String TAG = "MyFirebaseMsgService";
    
    private NotificationManager notificationManager;
    
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]
        
        // Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//
//            // Check if message contains a notification payload.
//            if (remoteMessage.getNotification() != null) {
//                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            }
//
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                    PendingIntent.FLAG_ONE_SHOT);
//
//            String channelId = "Default";
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
//                    .setSmallIcon(R.mipmap.ic_launcher_round)
//                    .setContentTitle("test")
//                    .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);
//            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
//                manager.createNotificationChannel(channel);
//            }
//            manager.notify(0, builder.build());
//
//            // Also if you intend on generating your own notifications as a result of a received FCM
//            // message, here is where that should be initiated. See sendNotification method below.
//        }
        
        // Check if message contains a notification payload.
        notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        
        
        String type;
        String body;
        String program_name;
        
        if (remoteMessage.getData() != null) {
            body = remoteMessage.getData().get("body");
            type = remoteMessage.getData().get("type");
            program_name = remoteMessage.getData().get("program_name");
            if (type.equals("Message")) {
                SendMessageNotification(body);
            } else {
                SendMealScheduleNotification(body, program_name);
            }
        }
        
        
    }
    // [END receive_message]
    
    
    // [START on_new_token]
    
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]
    
    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        
        
        //firebase instance variables
        FirebaseAuth mAuth;
        FirebaseDatabase mFirebaseDatabase;
        DatabaseReference mUserPersonalInfoDatabaseReference;
        
        //initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String currentUserId = mAuth.getCurrentUser().getUid();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mUserPersonalInfoDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                    .child(currentUserId).child("PersonalInfo");
            mUserPersonalInfoDatabaseReference.child("fcm_token").setValue(token);
        }
    }
    
    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.fcm_message_title))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
    
    private void SendMessageNotification(String body) {
        
        //firebase instance variables
        FirebaseAuth mAuth;
        FirebaseDatabase mFirebaseDatabase;
        DatabaseReference mUserPersonalInfoDatabaseReference;
        
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        notificationIntent.putExtra("start_where", "chat");
        
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 6,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_6_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("Lifecare")
                .setContentText(body + " yeni mesajınız var.")
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setContentIntent(pendingIntent)
                .build();
        
        notificationManager.notify(6, notification);
        
        //initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String currentUserId = mAuth.getCurrentUser().getUid();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mUserPersonalInfoDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                    .child(currentUserId).child("PersonalInfo");
            
            mUserPersonalInfoDatabaseReference.child("new_message").setValue(true);
        }
        
    }
    
    private void SendMealScheduleNotification(String body, String program_name) {
        
        //firebase instance variables
        FirebaseAuth mAuth;
        FirebaseDatabase mFirebaseDatabase;
        DatabaseReference mUserPersonalInfoDatabaseReference;
        
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        notificationIntent.putExtra("start_where", "meal_schedule");
        
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 7,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        String context_text;
        if(program_name != null) {
            context_text = program_name + " isimli programınız güncellendi";
        } else {
            context_text = body + " size özel yeni programınızı hazırladı.";
        }
        
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_7_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("Lifecare")
                .setContentText(context_text)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setContentIntent(pendingIntent)
                .build();
        
        notificationManager.notify(7, notification);
        
        //initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String currentUserId = mAuth.getCurrentUser().getUid();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mUserPersonalInfoDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                    .child(currentUserId).child("PersonalInfo");
            
            mUserPersonalInfoDatabaseReference.child("new_meal_schedule").setValue(true);
        }
        
    }
    
}
