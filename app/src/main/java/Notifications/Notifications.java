package Notifications;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.ShowReward;

import AppContext.MyApplication;

public class Notifications {

    public static void showLowStorageNotification() {
        Context context = MyApplication.getAppContext();

        NotificationUtils.createNotificationChannel(context);
        NotificationCompat.Builder builder = NotificationUtils.createNotificationBuilder("BAJO ESPACIO DE ALMACENAMIENTO",
                "Debido al bajo almacenamiento de tu dispositivo no se guardarán las grabaciones.");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }

    @SuppressLint("WearRecents")
    public static void showRewardNotification(int rewardId) {
        Context context = MyApplication.getAppContext();

        Intent intent = new Intent(context, ShowReward.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("rewardId", rewardId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        NotificationUtils.createNotificationChannel(context);

        NotificationCompat.Builder builder = NotificationUtils.createNotificationBuilder("¡HAS GANADO UNA RECOMPENSA!",
                "¡Haz clic aquí para descubrir que es!");

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(2, builder.build());
        }
    }

    public static void showTipNotification(String tip) {
        Context context = MyApplication.getAppContext();

        NotificationUtils.createNotificationChannel(context);
        NotificationCompat.Builder builder = NotificationUtils.createNotificationBuilder("SleepFantasy recomienda:",
                tip);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(3, builder.build());
        }
    }

    public static void showMissingSensorsNotification() {
        Context context = MyApplication.getAppContext();

        NotificationUtils.createNotificationChannel(context);
        NotificationCompat.Builder builder = NotificationUtils.createNotificationBuilder("¡Faltan sensores!",
                "No será posible realizar el rastero sin los sensores necesarios.");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(4, builder.build());
        }
    }

    public static void showMissingDataNotification() {
        Context context = MyApplication.getAppContext();

        NotificationUtils.createNotificationChannel(context);
        NotificationCompat.Builder builder = NotificationUtils.createNotificationBuilder("¡Error al guardar tu ciclo de sueños!",
                "Parece que no pudimos registrar una fase de sueño. Por favor, intenta nuevamente.");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(5, builder.build());
        }
    }

    public static Notification getNotification() {
        Context context = MyApplication.getAppContext();

        NotificationUtils.createNotificationChannel(context);
        NotificationCompat.Builder builder = NotificationUtils.createNotificationBuilder("Sleep Tracker Service",
                "SleepFantasy está corriendo.");

        return builder.build();
    }
}
