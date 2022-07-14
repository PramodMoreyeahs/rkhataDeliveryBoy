package in.moreyeahs.livspace.delivery.fcm;

/**
 * Created by Amit Gupta on 03-03-2017.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.utilities.RxBus;
import in.moreyeahs.livspace.delivery.utilities.SharePrefs;

import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            System.out.println("Message data payload: " + remoteMessage.getData());
        }
        String body = "";
        String title = "";
        try {
            JSONObject object = new JSONObject(remoteMessage.getData());
            if (object.has("notify_type") && !TextUtils.isEmpty(object.getString("notify_type"))) {
                switch (object.getString("notify_type")) {
                    case "return":
                        SharePrefs.getInstance(getApplicationContext()).putBoolean(SharePrefs.HAS_RETURN_ORDER, true);
                        break;
                    case "returnOrder":
                        SharePrefs.getInstance(getApplicationContext()).putBoolean(SharePrefs.HAS_RETURN_ORDER, true);
                        break;
                }
            } else {
                if (object.has("body")) {
                    body = object.getString("body");
                }
                if (object.has("title")) {
                    title = object.getString("title");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setNotification(body, title);
    }


    private void setNotification(String messageBody, String title) {
        try {
            RxBus.getInstance().sendEvent(title);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getResources().getString(R.string.app_name))
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setContentInfo(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setChannelId(getResources().getString(R.string.app_name));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.app_name),
                        getResources().getString(R.string.app_name),
                        NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableLights(true);
                mChannel.enableVibration(true);
                mChannel.setLightColor(Color.YELLOW);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(mChannel);
            }

            NotificationManagerCompat notificationManagerCompat =
                    NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify((int) System.currentTimeMillis(), builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}