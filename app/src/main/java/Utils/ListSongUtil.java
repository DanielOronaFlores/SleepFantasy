package Utils;

import java.util.ArrayList;
import java.util.List;

import Music.Song;

public class ListSongUtil {
    public List<Song> getSelectedSongs(List<Song> songs, List<Boolean> checkedList) {
        List<Song> selectedSongs = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++) {
            if (checkedList.get(i)) {
                selectedSongs.add(songs.get(i));
            }
        }
        return selectedSongs;
    }
}
