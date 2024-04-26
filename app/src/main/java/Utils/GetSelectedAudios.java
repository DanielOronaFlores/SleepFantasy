package Utils;

import java.util.ArrayList;
import java.util.List;

import Models.Audio;

public class GetSelectedAudios {
    public List<Audio> getSelectedAudios(List<Audio> Audios, List<Boolean> checkedList) {
        List<Audio> selectedAudios = new ArrayList<>();
        for (int i = 0; i < Audios.size(); i++) {
            if (checkedList.get(i)) {
                selectedAudios.add(Audios.get(i));
            }
        }
        return selectedAudios;
    }
}
