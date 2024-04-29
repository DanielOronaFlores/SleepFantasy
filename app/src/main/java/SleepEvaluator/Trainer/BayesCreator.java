package SleepEvaluator.Trainer;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

import AppContext.MyApplication;
import Database.DataUpdates.ProbabilitiesDataUpdate;
import Database.DatabaseConnection;
import SleepEvaluator.RangesValues;

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
        int row = 0;
        switch (id) {
            case 0:
                row = RangesValues.totalSleepTime(value) - 1;
                break;
            case 1:
                row = RangesValues.lightSleepTime(value) - 1;
                break;
            case 2:
                row = RangesValues.deepSleepTime(value) - 1;
                break;
            case 3:
                row = RangesValues.remSleepTime(value) - 1;
                break;
            case 4:
                row = RangesValues.efficiency(value) - 1;
                break;
            case 5:
                row = RangesValues.awakenings(value) - 1;
                break;
            case 6:
                row = RangesValues.suddenMovements(value) - 1;
                break;
            case 7:
                row = RangesValues.positionChanges(value) - 1;
                break;
        }
        attributeValues[row][column]++;
    }

    private static String getAttributeName(int attribute) {
        switch (attribute) {
            case 0:
                return "totalSleepTime";
            case 1:
                return "lightSleepTime";
            case 2:
                return "deepSleepTime";
            case 3:
                return "remSleepTime";
            case 4:
                return "efficiency";
            case 5:
                return "awakenings";
            case 6:
                return "suddenMovements";
            case 7:
                return "positionChanges";
            default:
                return "";
        }
    }

    private static int calculateSumColumn(float[][] attributeValues, int column) {
        int sum = 0;
        for (int i = 0; i < 7; i++) {
            sum += attributeValues[i][column];
        }
        return sum;
    }

    private static void calculateFrequencyProbabilities() {
        totalInstances = deserializer.countInstances();

        ArrayList<String> categoryValues, attributeValues;

        categoryValues = deserializer.getValueForElement("category");
        printArrayList(categoryValues);

        for (int i = 0; i < 8; i++) {
            attributeValues = deserializer.getValueForElement(getAttributeName(i));
            System.out.println("----" + getAttributeName(i) + "----");
            printArrayList(categoryValues);

            for (int j = 0; j < totalInstances; j++) {
                float value = Float.parseFloat(attributeValues.get(j));
                int category = Integer.parseInt(categoryValues.get(j));

                System.out.println("Category: " + category + " Value: " + value);
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

        printALL();
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

    private static void calculateConditionalProbabilities() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                frequencyTotalSleepTime[i][j] /= calculateSumColumn(frequencyTotalSleepTime, j);
                frequencyLightSleepTime[i][j] /= calculateSumColumn(frequencyLightSleepTime, j);
                frequencyDeepSleepTime[i][j] /= calculateSumColumn(frequencyDeepSleepTime, j);
                frequencyRemSleepTime[i][j] /= calculateSumColumn(frequencyRemSleepTime, j);
                frequencyEfficiency[i][j] /= calculateSumColumn(frequencyEfficiency, j);
                frequencyAwakenings[i][j] /= calculateSumColumn(frequencyAwakenings, j);
                frequencySuddenMovements[i][j] /= calculateSumColumn(frequencySuddenMovements, j);
                frequencyPositionChanges[i][j] /= calculateSumColumn(frequencyPositionChanges, j);
            }
        }
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
        System.out.println("----Calcular frecuencias de probabilidades----");
        calculateFrequencyProbabilities();
        printALL();

        setLaPlaceSmoothing();
        calculateConditionalProbabilities();
        saveProbabilities();
    }


    private static void printALL() {
        System.out.println("----Total Sleep Time----");
        printMatrix(frequencyTotalSleepTime);

        System.out.println("----Light Sleep Time----");
        printMatrix(frequencyLightSleepTime);

        System.out.println("----Deep Sleep Time----");
        printMatrix(frequencyDeepSleepTime);

        System.out.println("----Rem Sleep Time----");
        printMatrix(frequencyRemSleepTime);

        System.out.println("----Efficiency----");
        printMatrix(frequencyEfficiency);

        System.out.println("----Awakenings----");
        printMatrix(frequencyAwakenings);

        System.out.println("----Sudden Movements----");
        printMatrix(frequencySuddenMovements);

        System.out.println("----Position Changes----");
        printMatrix(frequencyPositionChanges);
    }

    private static void printMatrix(float[][] matrix) {
        for (float[] floats : matrix) {
            for (float aFloat : floats) {
                System.out.print(aFloat + "\t");
            }
            System.out.println();
        }
    }

    private static void printArrayList(ArrayList<String> list) {
        for (String v : list) {
            System.out.print(v + "\t");
        }
        System.out.println();
    }
}
