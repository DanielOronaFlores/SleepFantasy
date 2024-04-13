package Notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.R;

import AppContext.MyApplication;

public class NotificationUtils {
    private static final String CHANNEL_ID = "sleepfantasy_channel";
    private static final CharSequence CHANNEL_NAME = "SleepFantasy Channel";
    private static final String CHANNEL_DESCRIPTION = "Channel for SleepFantasy notifications.";
    private static final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_LOW;

    public static NotificationCompat.Builder createNotificationBuilder(String title, String text) {
        return new NotificationCompat.Builder(MyApplication.getAppContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.placeholder_ramon)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    CHANNEL_IMPORTANCE
            );

            notificationChannel.setDescription(CHANNEL_DESCRIPTION);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }
}