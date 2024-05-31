package Serializers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import Models.Sound;

public class  Deserializer {
    public static List<Sound> deserializeFromXML(String fileName) {
        List<Sound> soundList = new ArrayList<>();

        try {
            File file = new File(fileName);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("sound");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    int second = Integer.parseInt(element.getElementsByTagName("second").item(0).getTextContent());

                    Sound sound = new Sound(second);
                    soundList.add(sound);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soundList;
    }
}
