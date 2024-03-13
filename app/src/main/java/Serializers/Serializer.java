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
    public void serializeToXML(List<Sound> soundList) {
        String fileName = audiosFiles.getXMLPath();
        try (Writer writer = new FileWriter(fileName)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            writer.write("<soundList>\n");

            for (Sound sound : soundList) {
                writer.write("  <sound>\n");
                writer.write("    <second>" + sound.getSecond() + "</second>\n");
                writer.write("  </sound>\n");
            }

            writer.write("</soundList>\n");
            System.out.println("Serialización exitosa en " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void test(List<SleepCycle> sleepCycles) {
        String fileName = audiosFiles.test();
        try (Writer writer = new FileWriter(fileName)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            writer.write("<cycleList>\n");

            for (SleepCycle cycle : sleepCycles) {
                writer.write("  <sleep>\n");
                writer.write("    <cycle>" + cycle.getSleepCycle() + "</sleep>\n");
                writer.write("    <date>" + cycle.getDateTime() + "</date>\n");
                writer.write("    <mrc>" + cycle.getMrcData() + "</mrc>\n");
                writer.write("    <sddn>" + cycle.getSdnn() + "</sddn>\n");
                writer.write("  </sleep>\n");
            }

            writer.write("</cycleList>\n");
            System.out.println("Serialización exitosa en " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
