package SleepEvaluator.Trainer;

import android.content.Context;
import android.util.Xml;

import com.example.myapplication.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import AppContext.MyApplication;

public class Instances {
    private final Context context = MyApplication.getAppContext();

    private int countOccurrences(String tagName, String id) {
        int count = 0;
        InputStream inputStream = context.getResources().openRawResource(R.raw.dataset);
        XmlPullParser parser = Xml.newPullParser();

        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && tagName.equals(parser.getName())) {
                    if (id != null) {
                        String value = parser.nextText();
                        if (id.equals(value)) {
                            count++;
                        }
                    } else {
                        count++;
                    }
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return count;
    }
    private ArrayList<String> getElementValues(String elementName) {
        InputStream inputStream = context.getResources().openRawResource(R.raw.dataset);
        XmlPullParser parser = Xml.newPullParser();
        ArrayList<String> values = new ArrayList<>();

        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            String currentElementName = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    currentElementName = parser.getName();
                } else if (eventType == XmlPullParser.TEXT && currentElementName != null && currentElementName.equals(elementName)) {
                    String text = parser.getText().trim();
                    String[] numbers = text.split("\\s*,\\s*");
                    for (String number : numbers) {
                        if (!number.isEmpty()) {
                            values.add(number);
                        }
                    }
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return values;
    }


    public int countCategory(String id) {
        return countOccurrences("category", id);
    }
    public int countInstances() {
        return countOccurrences("instance", null);
    }
    public ArrayList<String> getValueForElement(String elementName) {
        return getElementValues(elementName);
    }
}
