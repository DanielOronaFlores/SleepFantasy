package SleepEvaluator;

public class RangesValues {
    public static int totalSleepTime(float value) {
        if (value >= 420 && value <= 540) { // 1
            return 1;
        }
        if (value >= 360 && value <= 420) { // 2
            return 2;
        }
        if (value >= 540 && value <= 600) { // 3
            return 3;
        }
        if (value >= 300 && value <= 360) { // 4
            return 4;
        }
        if (value >= 600 && value <= 660) { // 5
            return 5;
        }
        if (value >= 151 && value <= 220) { // 6
            return 6;
        }
        return 7; // value <= 120 || value >= 491
    }

    public static int efficiency(float value) {
        if (value >= 85 && value <= 100) { // 1
            return 1;
        }
        if (value >= 75 && value < 85) { // 2
            return 2;
        }
        if (value >= 65 && value < 75) { // 3
            return 3;
        }
        if (value >= 55 && value < 65) { // 4
            return 4;
        }
        if (value >= 45 && value <= 45) { // 5
            return 5;
        }
        if (value >= 35 && value <= 45) { // 6
            return 6;
        }
        return 7; // value < 35
    }

    public static int awakenings(float value) {
        if (value >= 0 && value <= 1) {
            return 1;
        }
        if (value >= 2 && value <= 3) {
            return 2;
        }
        if (value >= 4 && value <= 5) {
            return 3;
        }
        if (value >= 6 && value <= 7) {
            return 4;
        }
        if (value >= 8 && value <= 9) {
            return 5;
        }
        if (value >= 10 && value <= 11) {
            return 6;
        }
        return 7; // value >= 12
    }

    public static int suddenMovements(float value) {
        if (value >= 0 && value < 1) { // 1
            return 1;
        }
        if (value >= 1 && value < 2) { // 2
            return 2;
        }
        if (value >= 2 && value < 3) { // 3
            return 3;
        }
        if (value >= 3 && value < 4) { // 4
            return 4;
        }
        if (value >= 4 && value < 5) { // 5
            return 5;
        }
        if (value >= 5 && value < 6) { // 6
            return 6;
        }
        return 7; // value > 6
    }

    public static int positionChanges(float value) {
        if (value >= 0 && value <= 3) { // 1
            return 1;
        }
        if (value >= 3 && value <= 5) { // 2
            return 2;
        }
        if (value >= 5 && value <= 7) { // 3
            return 3;
        }
        if (value >= 8 && value <= 11) { // 4
            return 4;
        }
        if (value >= 11 && value <= 15) { // 5
            return 5;
        }
        if (value >= 11 && value <= 20) {
            return 6;
        }
        return 7; // value > 20
    }

    public static int lightSleepTime(float value) {
        if (value >= 50 && value <= 60) { // 1
            return 1;
        }
        if (value >= 40 && value <= 49) { // 2
            return 2;
        }
        if (value >= 60 && value <= 70) { // 3
            return 3;
        }
        if (value >= 30 && value <= 40) { // 4
            return 4;
        }
        if (value >= 70 && value <= 80) { // 5
            return 5;
        }
        if (value >= 20 && value <= 30) { // 6
            return 6;
        }
        return 7; // value mayor a 80 o menor a 20
    }

    public static int deepSleepTime(float value) {
        if (value >= 21 && value <= 25) { // 1
            return 1;
        }
        if (value >= 16 && value <= 20) { // 2
            return 2;
        }
        if (value >= 25 && value <= 30) { // 3
            return 3;
        }
        if (value >= 10 && value <= 15) { // 4
            return 4;
        }
        if (value >= 20 && value <= 30) { // 5
            return 5;
        }
        if (value >= 5 && value <= 10) {
            return 6;
        }
        return 7; //mayor a 35 o menor a 5
    }

    public static int remSleepTime(float value) {
        if (value >= 21 && value <= 25) { // 1
            return 1;
        }
        if (value >= 16 && value <= 20) { // 2
            return 2;
        }
        if (value >= 25 && value <= 30) { // 3
            return 3;
        }
        if (value >= 10 && value <= 15) { // 4
            return 4;
        }
        if (value >= 20 && value <= 30) { // 5
            return 5;
        }
        if (value >= 5 && value <= 10) {
            return 6;
        }
        return 7; //mayor a 35 o menor a 5
    }
}
