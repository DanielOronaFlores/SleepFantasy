package Rewards;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.myapplication.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import AppContext.MyApplication;

public class SoundsRewards {
    private static final int[] rawSoundResources = {
            R.raw.notification_alert,
            R.raw.notification_coin,
            R.raw.notification_death,
            R.raw.notification_happy,
            R.raw.notification_heal,
            R.raw.notification_hidden,
            R.raw.notification_lightsaber,
            R.raw.notification_punch,
            R.raw.notification_ring,
            R.raw.notification_shine,
            R.raw.notification_star,
            R.raw.notification_windows,
            R.raw.notification_xen,
            R.raw.notification_yoda,
            R.raw.notification_yoshi
    };

    private static void copyRawSoundToMusicFolder(int rawResourceId, String fileName) {
        Context context = MyApplication.getAppContext();
        InputStream inputStream = context.getResources().openRawResource(rawResourceId);
        File musicFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File destinationFile = new File(musicFolder, fileName);
        System.out.println("Destination file: " + destinationFile.getAbsolutePath());
        try {
            OutputStream outputStream;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                outputStream = Files.newOutputStream(destinationFile.toPath());
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.close();
                inputStream.close();
                System.out.println("Sound file copied to Music folder");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error copying sound file to Music folder");
        }
    }

    public static void giveRewardSound(int id, int totalRewards) {
        int rewardId = rawSoundResources[totalRewards - id];
        String fileName = "notification_" + rewardId + ".mp3";

        copyRawSoundToMusicFolder(rewardId, fileName);
    }
}
