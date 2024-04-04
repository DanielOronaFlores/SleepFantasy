package Serializers;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import Files.AudiosPaths;
import Models.SleepCycle;
import Models.Sound;

public class Serializer {
    AudiosPaths audiosFiles = new AudiosPaths();
    public void serializeSoundsToXML(List<Sound> soundList) {
        String fileName = audiosFiles.getListSoundsPath();
        try (Writer writer = new FileWriter(fileName)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            writer.write("<soundList>\n");

            for (Sound sound : soundList) {
                writer.write("  <sound>\n");
                writer.write("    <second>" + sound.getSecond() + "</second>\n");
                writer.write("  </sound>\n");
            }

            writer.write("</soundList>\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
