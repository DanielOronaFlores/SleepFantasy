package SortingAlgorithm;
import java.io.Console;
import java.util.Arrays;

import AppContext.MyApplication;
import Database.DataAccess.ProbabilitiesDataAccess;
import Database.DatabaseConnection;

public class SleepEvaluator {
    private final DatabaseConnection connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
    private final ProbabilitiesDataAccess probabilitiesDataAccess;
    private float totalSleepTime;
    private float lightSleepTime;
    private float deepSleepTime;
    private float remSleepTime;
    private float efficiency;
    private float awakenings;
    private float suddenMovements;
    private float positionChanges;

    // Probabilities per Attribute & Category
    private float[] totalSleepTimeProbailities = new float[7];
    private float[] lightSleepTimeProbabilities = new float[7];
    private float[] deepSleepTimeProbabilities = new float[7];
    private float[] remSleepTimeProbabilities = new float[7];
    private float[] efficiencyProbabilities = new float[7];
    private float[] awakeningsProbabilities = new float[7];
    private float[] suddenMovementsProbabilities = new float[7];
    private float[] positionChangesProbabilities = new float[7];

    private final int[] ranges = new int[8];
    private final float[][] categoryProbabilities = new float[8][7]; // 8 attributes, 7 categories
    private final float[] finalCategoriesProbabilities = new float[7]; // 7 categories
    public SleepEvaluator() {
        probabilitiesDataAccess = new ProbabilitiesDataAccess(connection);
    }

    private void setRanges() {
        ranges[0] = RangesValues.awakenings(totalSleepTime);
        ranges[1] = RangesValues.suddenMovements(lightSleepTime);
        ranges[2] = RangesValues.positionChanges(deepSleepTime);
        ranges[3] = RangesValues.totalSleepTime(remSleepTime);
        ranges[4] = RangesValues.lightSleepTime(efficiency);
        ranges[5] = RangesValues.deepSleepTime(awakenings);
        ranges[6] = RangesValues.remSleepTime(suddenMovements);
        ranges[7] = RangesValues.efficiency(positionChanges);
    }
    private void getInitialAttributeProbabilitiesPerRange(float[] attribute, int range, String attributeName) {
        for (int i = 0; i < attribute.length; i++) {
            String category = "category" + (i + 1);
            attribute[i] = probabilitiesDataAccess.getProbability(category, range, attributeName);
        }
    }
    private void generateCategoryProbabilities(int attributeID, float[] attribute) {
        for (int i = 0; i < attribute.length; i++) {
            categoryProbabilities[attributeID][i] = attribute[i];
        }
    }

    private void calculateFinalAttributeProbabilities() {
       for (int i = 0; i < finalCategoriesProbabilities.length; i++) {
            float mult = 1;
            for (int j = 0; j < categoryProbabilities[0].length; j++) {
                mult *= categoryProbabilities[i][j];
            }
            mult *= PrioriCategories.getPrioriProbabilities()[i];
           finalCategoriesProbabilities[i] = mult;
        }
    }

    public void evaluate(int totalSleepTime, int lightSleepTime, int deepSleepTime, int remSleepTime, int efficiency, int awakenings, int suddenMovements, int positionChanges) {
        this.totalSleepTime = totalSleepTime;
        this.lightSleepTime = PercentageConverter.convertToPercentage(lightSleepTime, totalSleepTime);
        this.deepSleepTime = PercentageConverter.convertToPercentage(deepSleepTime, totalSleepTime);
        this.remSleepTime = PercentageConverter.convertToPercentage(remSleepTime, totalSleepTime);
        this.efficiency = efficiency;
        this.awakenings = awakenings;
        this.suddenMovements = suddenMovements;
        this.positionChanges = positionChanges;

        setRanges();
        connection.openDatabase();

        // Get the initial probabilities for each attribute
        getInitialAttributeProbabilitiesPerRange(totalSleepTimeProbailities, ranges[0], "totalSleepTime");
        getInitialAttributeProbabilitiesPerRange(lightSleepTimeProbabilities, ranges[1], "lightSleepTime");
        getInitialAttributeProbabilitiesPerRange(deepSleepTimeProbabilities, ranges[2], "deepSleepTime");
        getInitialAttributeProbabilitiesPerRange(remSleepTimeProbabilities, ranges[3], "remSleepTime");
        getInitialAttributeProbabilitiesPerRange(efficiencyProbabilities, ranges[4], "efficiency");
        getInitialAttributeProbabilitiesPerRange(awakeningsProbabilities, ranges[5], "awakenings");
        getInitialAttributeProbabilitiesPerRange(suddenMovementsProbabilities, ranges[6], "suddenMovements");
        getInitialAttributeProbabilitiesPerRange(positionChangesProbabilities, ranges[7], "positionChanges");

        // Generate the category probabilities
        generateCategoryProbabilities(0, totalSleepTimeProbailities);
        generateCategoryProbabilities(1, lightSleepTimeProbabilities);
        generateCategoryProbabilities(2, deepSleepTimeProbabilities);
        generateCategoryProbabilities(3, remSleepTimeProbabilities);
        generateCategoryProbabilities(4, efficiencyProbabilities);
        generateCategoryProbabilities(5, awakeningsProbabilities);
        generateCategoryProbabilities(6, suddenMovementsProbabilities);
        generateCategoryProbabilities(7, positionChangesProbabilities);

        //Calculate the final probabilities
        calculateFinalAttributeProbabilities();

        //Print in the console the probabilities
        for (int i = 0; i < finalCategoriesProbabilities.length; i++) {
            System.out.println("Category " + (i + 1) + ": " + finalCategoriesProbabilities[i]);
        }
    }
}