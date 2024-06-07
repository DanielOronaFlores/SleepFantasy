package SleepEvaluator.Trainer;

import android.annotation.SuppressLint;

import java.util.ArrayList;

import AppContext.MyApplication;
import Database.DataUpdates.ProbabilitiesDataUpdate;
import Database.DatabaseConnection;
import SleepEvaluator.RangesValues;

@SuppressLint("StaticFieldLeak")
public class BayesCreator {
    private static final Instances deserializer = new Instances();
    private static final float[][] frequencyTotalSleepTime = new float[7][7];
    private static final float[][] frequencyLightSleepTime = new float[7][7];
    private static final float[][] frequencyDeepSleepTime = new float[7][7];
    private static final float[][] frequencyRemSleepTime = new float[7][7];
    private static final float[][] frequencyEfficiency = new float[7][7];
    private static final float[][] frequencyAwakenings = new float[7][7];
    private static final float[][] frequencySuddenMovements = new float[7][7];
    private static final float[][] frequencyPositionChanges = new float[7][7];
    private static int totalInstances;


    private static void setFrequency(int category, float value, float[][] attributeValues, int id) {
        int column = category - 1;
        int row = switch (id) {
            case 0 -> RangesValues.totalSleepTime(value) - 1;
            case 1 -> RangesValues.lightSleepTime(value) - 1;
            case 2 -> RangesValues.deepSleepTime(value) - 1;
            case 3 -> RangesValues.remSleepTime(value) - 1;
            case 4 -> RangesValues.efficiency(value) - 1;
            case 5 -> RangesValues.awakenings(value) - 1;
            case 6 -> RangesValues.suddenMovements(value) - 1;
            case 7 -> RangesValues.positionChanges(value) - 1;
            default -> 0;
        };
        attributeValues[row][column]++;
    }

    private static String getAttributeName(int attribute) {
        return switch (attribute) {
            case 0 -> "totalSleepTime";
            case 1 -> "lightSleepTime";
            case 2 -> "deepSleepTime";
            case 3 -> "remSleepTime";
            case 4 -> "efficiency";
            case 5 -> "awakenings";
            case 6 -> "suddenMovements";
            case 7 -> "positionChanges";
            default -> "";
        };
    }

    private static float calculateSumColumn(float[][] attributeValues, int column) {
        float sum = 0;

        for (int i = 0; i < 7; i++) {
            sum += attributeValues[i][column];
        }
        //System.out.println("Suma de columna " + column + ": " + sum);
        return sum;
    }

    private static void calculateFrequencyProbabilities() {
        totalInstances = deserializer.countInstances();

        ArrayList<String> categoryValues, attributeValues;
        categoryValues = deserializer.getValueForElement("category");

        for (int i = 0; i < 8; i++) {
            attributeValues = deserializer.getValueForElement(getAttributeName(i));

            for (int j = 0; j < totalInstances; j++) {
                float value = Float.parseFloat(attributeValues.get(j));
                int category = Integer.parseInt(categoryValues.get(j));

                switch (i) {
                    case 0:
                        setFrequency(category, value, frequencyTotalSleepTime, i);
                        break;
                    case 1:
                        setFrequency(category, value, frequencyLightSleepTime, i);
                        break;
                    case 2:
                        setFrequency(category, value, frequencyDeepSleepTime, i);
                        break;
                    case 3:
                        setFrequency(category, value, frequencyRemSleepTime, i);
                        break;
                    case 4:
                        setFrequency(category, value, frequencyEfficiency, i);
                        break;
                    case 5:
                        setFrequency(category, value, frequencyAwakenings, i);
                        break;
                    case 6:
                        setFrequency(category, value, frequencySuddenMovements, i);
                        break;
                    case 7:
                        setFrequency(category, value, frequencyPositionChanges, i);
                        break;
                }
            }
        }
    }

    private static void setLaPlaceSmoothing() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                frequencyTotalSleepTime[i][j]++;
                frequencyLightSleepTime[i][j]++;
                frequencyDeepSleepTime[i][j]++;
                frequencyRemSleepTime[i][j]++;
                frequencyEfficiency[i][j]++;
                frequencyAwakenings[i][j]++;
                frequencySuddenMovements[i][j]++;
                frequencyPositionChanges[i][j]++;
            }
        }
    }

    private static void setConditionalProbabilities(float[][] attributeValues) {
        for (int column = 0; column < 7; column++) {
            float sumPerColumn = calculateSumColumn(attributeValues, column);
            for (int i = 0; i < 7; i++) {
                attributeValues[i][column] /= sumPerColumn;
            }
        }
    }

    private static void calculateConditionalProbabilities() {
        setConditionalProbabilities(frequencyTotalSleepTime);
        setConditionalProbabilities(frequencyLightSleepTime);
        setConditionalProbabilities(frequencyDeepSleepTime);
        setConditionalProbabilities(frequencyRemSleepTime);
        setConditionalProbabilities(frequencyEfficiency);
        setConditionalProbabilities(frequencyAwakenings);
        setConditionalProbabilities(frequencySuddenMovements);
        setConditionalProbabilities(frequencyPositionChanges);
    }

    private static void saveProbabilities() {
        DatabaseConnection connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        ProbabilitiesDataUpdate probabilitiesDataUpdate = new ProbabilitiesDataUpdate(connection);

        probabilitiesDataUpdate.addProbabilities(0, frequencyTotalSleepTime);
        probabilitiesDataUpdate.addProbabilities(1, frequencyLightSleepTime);
        probabilitiesDataUpdate.addProbabilities(2, frequencyDeepSleepTime);
        probabilitiesDataUpdate.addProbabilities(3, frequencyRemSleepTime);
        probabilitiesDataUpdate.addProbabilities(4, frequencyEfficiency);
        probabilitiesDataUpdate.addProbabilities(5, frequencyAwakenings);
        probabilitiesDataUpdate.addProbabilities(6, frequencySuddenMovements);
        probabilitiesDataUpdate.addProbabilities(7, frequencyPositionChanges);
    }

    public static void createProbabilities() {
        calculateFrequencyProbabilities();
        setLaPlaceSmoothing();
        calculateConditionalProbabilities();
        saveProbabilities();
    }
}
