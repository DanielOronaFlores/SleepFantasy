package Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Models.Sound;

public class SecondsCounter {
    public List<List<Integer>> getConsecutiveSeconds(List<Sound> soundList) {
        List<List<Integer>> consecutiveNumbersList = new ArrayList<>();

        if (soundList == null || soundList.isEmpty()) {
            return consecutiveNumbersList; // Lista vacía, no hay secuencias consecutivas.
        }

        List<Integer> consecutiveNumbers = new ArrayList<>();
        consecutiveNumbers.add(soundList.get(0).getSecond());

        for (int i = 0, j = 1; j < soundList.size(); i++, j++) {
            int currentSecond = soundList.get(i).getSecond();
            int nextSecond = soundList.get(j).getSecond();

            if (currentSecond != nextSecond) {
                if (Math.abs(currentSecond - nextSecond) == 1) {
                    consecutiveNumbers.add(nextSecond);
                } else {
                    if (consecutiveNumbers.size() >= 3) {
                        System.out.println("Consecutive numbers (list): " + consecutiveNumbers);
                        consecutiveNumbersList.add(new ArrayList<>(consecutiveNumbers));
                    }
                    consecutiveNumbers.clear();
                    consecutiveNumbers.add(nextSecond);
                }
            }
        }

        return consecutiveNumbersList;
    }

    public int getTotalSeconds(List<Sound> soundList) {
        int totalSeconds = 0;

        for (Sound sound : soundList) {
            totalSeconds += sound.getSecond();
        }

        return totalSeconds;
    }
}
