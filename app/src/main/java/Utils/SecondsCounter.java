package Utils;

import android.util.Log;

import java.util.ArrayList;
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

            if (Math.abs(currentSecond - nextSecond) == 1) {
                consecutiveNumbers.add(nextSecond);
            } else {
                consecutiveNumbersList.add(new ArrayList<>(consecutiveNumbers));
                consecutiveNumbers.clear();
                consecutiveNumbers.add(nextSecond);
            }
        }

        // Agregar la última secuencia consecutiva
        consecutiveNumbersList.add(new ArrayList<>(consecutiveNumbers));

        Log.d("ConsecutiveNumbers", "Consecutive numbers: " + consecutiveNumbersList.size());
        return consecutiveNumbersList;
    }

}
