package SortingAlgorithm;

public class RangesValues {
    public static int totalSleepTime(float value) {
        if (value >= 471 && value <= 490) {
            return 1;
        }
        if (value >= 381 && value <= 470) {
            return 2;
        }
        if (value >= 341 && value <= 380) {
            return 3;
        }
        if (value >= 281 && value <= 340) {
            return 4;
        }
        if (value >= 221 && value <= 280) {
            return 5;
        }
        if (value >= 151 && value <= 220) {
            return 6;
        }
        return 7; // value <= 120 || value >= 491
    }

    public static int efficiency(float value) {
        if (value >= 91 && value <= 100) {
            return 1;
        }
        if (value >= 81 && value <= 90) {
            return 2;
        }
        if (value >= 71 && value <= 80) {
            return 3;
        }
        if (value >= 61 && value <= 70) {
            return 4;
        }
        if (value >= 51 && value <= 60) {
            return 5;
        }
        if (value >= 41 && value <= 50) {
            return 6;
        }
        return 7; // value <= 40
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

    public static int positionChanges(float value) {
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

    public static int lightSleepTime(float value) {
        if (value >= 50 && value <= 60) {
            return 1;
        }
        if (value >= 40 && value <= 49) {
            return 2;
        }
        if (value >= 30 && value <= 39) {
            return 3;
        }
        if (value >= 20 && value <= 29) {
            return 4;
        }
        if (value >= 10 && value <= 19) {
            return 5;
        }
        if (value >= 5 && value <= 9) {
            return 6;
        }
        return 7; // value <= 4
    }

    public static int deepSleepTime(float value) {
        if (value >= 21 && value <= 25) {
            return 1;
        }
        if (value >= 16 && value <= 20) {
            return 2;
        }
        if (value >= 10 && value <= 15) {
            return 3;
        }
        if (value >= 6 && value <= 9) {
            return 4;
        }
        if (value >= 3 && value <= 5) {
            return 5;
        }
        if (value >= 1 && value <= 2) {
            return 6;
        }
        return 7; // value <= 0
    }

    public static int remSleepTime(float value) {
        if (value >= 21 && value <= 25) {
            return 1;
        }
        if (value >= 16 && value <= 20) {
            return 2;
        }
        if (value >= 10 && value <= 15) {
            return 3;
        }
        if (value >= 6 && value <= 9) {
            return 4;
        }
        if (value >= 3 && value <= 5) {
            return 5;
        }
        if (value >= 1 && value <= 2) {
            return 6;
        }
        return 7; // value <= 0
    }
}
