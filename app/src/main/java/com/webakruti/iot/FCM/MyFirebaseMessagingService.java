package com.webakruti.iot.FCM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.webakruti.iot.CameraListActivity;
import com.webakruti.iot.MainActivity;
import com.webakruti.iot.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "NotificationModel Body: " + remoteMessage.getNotification().getBody());
            // handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().get("message"));

            try {
                if (remoteMessage.getData().toString() != null) {

                    // key to parse

                    //{"image":"http:\/\/healthyagingmission.com\/webgurukul\/images\/camera3.png","is_background":true,
                    // "payload":{"cover":"1","move":"1","camname":"AV81","location":"175, Ramsen Streets, Brooklyn","open":"1"},
                    // "title":"Camera IOT","message":"Camera AV81, 175, Ramsen Streets, Brooklyn is moved, cover & open",
                    // "timestamp":"2019-02-25 10:43:46"}

                    //remoteMessage.getData().containsKey("message");
                    String subtitle = remoteMessage.getData().get("message");
                    String title = remoteMessage.getData().get("title");
                    // String body = remoteMessage.getData().get("body");

                    handleNotification(subtitle, title);

                }

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String subtitle, String title) {

        Intent resultIntent = new Intent(getApplicationContext(), CameraListActivity.class);// here we can navigated activity name
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        int defaults = 0;
        //int icon = R.mipmap.camera2;
        int icon = R.mipmap.app_icon;

        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getApplicationContext());
        showSmallNotificationWithImageAsIcon(mBuilder, icon, title, subtitle, System.currentTimeMillis(), resultPendingIntent, defaults);

    }

    private void showSmallNotificationWithImageAsIcon(NotificationCompat.Builder mBuilder, int icon, String title, String message, long timeStamp, PendingIntent resultPendingIntent, int defaults) {


        Notification notification;

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle();
        inboxStyle.setBigContentTitle(title);
        inboxStyle.bigText(message);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("my_channel_01",
                    "CameraIOT",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);

            notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(resultPendingIntent)
                    .setDefaults(defaults)
                    .setStyle(inboxStyle)
                    .setWhen(timeStamp)
                    .setSmallIcon(getNotificationIcon())
                    .setContentText(message)
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.app_icon))
                    .setChannelId("my_channel_01")
                    .build();
        } else {

            notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(resultPendingIntent)
                    .setDefaults(defaults)
                    .setStyle(inboxStyle)
                    .setWhen(timeStamp)
                    .setSmallIcon(getNotificationIcon())
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.app_icon))
                    .build();
        }
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(m, notification);
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        //return useWhiteIcon ? R.mipmap.camera2 : R.mipmap.small_app_icon;
        return useWhiteIcon ? R.mipmap.app_icon : R.mipmap.small_app_icon;
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

  /*  @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);

        Log.e(TAG, "");
        if (intent.getExtras() != null) {
            String value = intent.getExtras().getString("gcm.notification.body");
            Log.d("", "Key: " + " gcm.notification.body " + " Value: " + value);
        }
    }*/
}
