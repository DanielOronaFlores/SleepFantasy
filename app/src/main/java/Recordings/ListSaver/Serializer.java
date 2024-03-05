package Recordings.ListSaver;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import AppContext.MyApplication;

public class Serializer {
    public static void saveToFile(Context context, List<Sound> soundsList) {
        String fileName = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + File.separator + "sounds.xml";
        Log.d("Serializer", "saveToFile: " + fileName);
        String serializedData = serialize(soundsList);
        if (serializedData != null) {
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                Log.d("Serializer", "saveToFile: " + serializedData);
                fos.write(serializedData.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String serialize(List<Sound> soundsList) {
           try {
                XmlSerializer serializer = Xml.newSerializer();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                serializer.setOutput(baos, "UTF-8");
                serializer.startDocument(null, true);
                serializer.startTag(null, "sounds");

                for (Sound sound : soundsList) {
                    writeSound(serializer, sound);
                }

                serializer.endTag(null, "sounds");
                serializer.endDocument();
                return baos.toString();
           } catch (IOException e) {
                e.printStackTrace();
                return null;
           }
    }
    private static void writeSound(XmlSerializer serializer, Sound sound) throws IOException {
        serializer.startTag(null, "sound");
        serializer.startTag(null, "second");
        serializer.text(String.valueOf(sound.getSecond()));
        serializer.endTag(null, "second");
        serializer.endTag(null, "sound");
    }
}
