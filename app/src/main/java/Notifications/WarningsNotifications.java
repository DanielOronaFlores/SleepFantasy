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
import com.example.myapplication.recordsVisualizer;

import AppContext.MyApplication;

public class WarningsNotifications {
    private static final String CHANNEL_ID = "sleepfantasy_channel";
    private final Context context = MyApplication.getAppContext();

    public void showLowStorageWarning() {
        ChannelCreator.createNotificationChannel(context);
        IntentCreator();

        // Construir la notificación utilizando el CHANNEL_ID
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.placeholder_ramon)
                .setContentTitle("BAJO ESPACIO DE ALMACENAMIENTO")
                .setContentText("El espacio de almacenamiento de tu dispositivo está llegando a su límite. " +
                        "No se podrán guardar más grabaciones hasta que liberes espacio.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Mostrar la notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    private void IntentCreator() {
        Intent intent = new Intent(context, recordsVisualizer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }
}
