package Notifications;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.R;
import com.example.myapplication.Recordings;

import AppContext.MyApplication;

public class Notifications {
    private static final String CHANNEL_ID = "sleepfantasy_channel";
    private final Context context = MyApplication.getAppContext();


    public void showLowStorageNotification() {
        ChannelCreator.createNotificationChannel(context);
        IntentRecordsVisualizerCreator();

        NotificationCompat.Builder builder = createNotificationBuilder("BAJO ESPACIO DE ALMACENAMIENTO",
                "Debido al bajo almacenamiento de tu dispositivo no se guardarán las grabaciones.");

        // Mostrar la notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    private NotificationCompat.Builder createNotificationBuilder(String title, String text) {
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.placeholder_ramon)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }
    private void IntentRecordsVisualizerCreator() {
        Intent intent = new Intent(context, Recordings.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }
}
