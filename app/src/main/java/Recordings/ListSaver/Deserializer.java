package Recordings.ListSaver;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Deserializer {
    public static List<Sound> loadFromFile(Context context, String fileName) {
        try (FileInputStream fis = context.openFileInput(fileName)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }

            String xmlData = baos.toString();
            return Deserializer.deserialize(xmlData);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public static List<Sound> deserialize(String xmlData) {
        List<Sound> soundsList = new ArrayList<>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlData));

            int eventType = parser.getEventType();
            Sound currentSound = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("sound".equals(parser.getName())) {
                            currentSound = new Sound(parser.getNamespaceCount(0));
                        } else if ("second".equals(parser.getName())) {
                            assert currentSound != null;
                            currentSound.setSecond(Integer.parseInt(parser.nextText()));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("sound".equals(parser.getName())) {
                            soundsList.add(currentSound);
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return soundsList;
    }
}
