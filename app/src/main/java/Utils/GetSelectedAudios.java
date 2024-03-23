package Utils;

import java.util.ArrayList;
import java.util.List;

import Models.Audio;

public class GetSelectedAudios {
    public List<Audio> getSelectedSongs(List<Audio> songs, List<Boolean> checkedList) {
        List<Audio> selectedSongs = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++) {
            if (checkedList.get(i)) {
                selectedSongs.add(songs.get(i));
            }
        }
        return selectedSongs;
    }
}
