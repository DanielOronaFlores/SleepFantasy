package Utils;

import java.util.ArrayList;
import java.util.List;

import Models.Audio;

public class GetSelectedAudios {
    public static List<Audio> getSelectedAudios(List<Audio> audios, List<Boolean> checkedList) {
        List<Audio> selectedAudios = new ArrayList<>();
        for (int i = 0; i < audios.size(); i++) {
            if (checkedList.get(i)) {
                selectedAudios.add(audios.get(i));
                 //if (selectedAudios.size() > 7) {
                    //break;
                 //}
            }
        }
        return selectedAudios;
    }
}
