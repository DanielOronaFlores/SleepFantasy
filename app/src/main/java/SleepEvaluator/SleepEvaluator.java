package SleepEvaluator;

import java.util.List;

import AppContext.MyApplication;
import Calculators.SleepData;
import Database.DataAccess.AvatarDataAccess;
import Database.DataAccess.ProbabilitiesDataAccess;
import Database.DataUpdates.AvatarDataUpdate;
import Database.DatabaseConnection;
import Files.AudiosPaths;
import GameManagers.Challenges.ChallengesUpdater;
import GameManagers.ExperienceManager;
import GameManagers.Missions.MissionsUpdater;
import GameManagers.Monsters.MonsterConditions;
import GameManagers.Monsters.MonstersManager;
import Models.Sound;
import Serializers.Deserializer;
import SleepEvaluator.Trainer.PrioriCategories;
import Tips.Tips;
import Utils.SecondsCounter;

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
    private final float[] totalSleepTimeProbailities = new float[7];
    private final float[] lightSleepTimeProbabilities = new float[7];
    private final float[] deepSleepTimeProbabilities = new float[7];
    private final float[] remSleepTimeProbabilities = new float[7];
    private final float[] efficiencyProbabilities = new float[7];
    private final float[] awakeningsProbabilities = new float[7];
    private final float[] suddenMovementsProbabilities = new float[7];
    private final float[] positionChangesProbabilities = new float[7];

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

    private int evaluateSleep() {
        lightSleepTime = PercentageConverter.convertToPercentage(lightSleepTime, totalSleepTime);
        deepSleepTime = PercentageConverter.convertToPercentage(deepSleepTime, totalSleepTime);
        remSleepTime = PercentageConverter.convertToPercentage(remSleepTime, totalSleepTime);

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

        //Evaluate Sleep
        float maxProbability = 0;
        int category = 0;
        for (int i = 0; i < finalCategoriesProbabilities.length; i++) {
            if (finalCategoriesProbabilities[i] >= maxProbability) {
                maxProbability = finalCategoriesProbabilities[i];
                category = i + 1;
            }
        }
        return category;
    }

    public void evaluate(int vigilTime, int lightSleepTime, int deepSleepTime, int remSleepTime, int awakenings, int suddenMovements, int positionChanges, boolean[] monsterConditions) {
        this.lightSleepTime = lightSleepTime;
        this.deepSleepTime = deepSleepTime;
        this.remSleepTime = remSleepTime;
        this.awakenings = awakenings;
        this.suddenMovements = suddenMovements;
        this.positionChanges = positionChanges;

        System.out.println("----------------- Evaluación de sueño -----------------");

        totalSleepTime = SleepData.getTotalSleepTime(lightSleepTime, deepSleepTime, remSleepTime);
        int timeInBed = SleepData.getTimeInBed(vigilTime, (int) totalSleepTime);
        efficiency = SleepData.getSleepEfficiency(totalSleepTime, timeInBed);

        int category = evaluateSleep();

        System.out.println("Tiempo total: " + totalSleepTime);
        System.out.println("Tiempo en cama: " + timeInBed);
        System.out.println("Eficiencia: " + efficiency);
        System.out.println("Categoria: " + category);

        AvatarDataUpdate avatarDataUpdate = new AvatarDataUpdate(connection);
        avatarDataUpdate.updateCharacterPhase((byte) category);

        ChallengesUpdater challengesUploader = new ChallengesUpdater(connection);
        challengesUploader.updateCategoryRecord(category);

        updateMissions(category, vigilTime);
        addExperience(category);

        if (MonsterConditions.isInsomnia((int) efficiency)) {
            monsterConditions[0] = true;
            System.out.println("Monstruos: ha aparecido un monstruo por insomnio");
        }
        
        SecondsCounter secondsCounter = new SecondsCounter();
        List<Sound> soundsList = Deserializer.deserializeFromXML(AudiosPaths.getListSoundsPath());
        int loudSoundsMinutes = secondsCounter.getTotalSeconds(soundsList) * 60;
        if (MonsterConditions.isLoudSound(loudSoundsMinutes)) {
            monsterConditions[1] = true;
            System.out.println("Monstruos: ha aparecido un monstruo por ruido fuerte");
        }

        // appearingMonsters = {insomnia, loudSound, anxiety, nightmare, somnambulism}
        MonstersManager monstersManager = new MonstersManager();
        monstersManager.updateMonster(monsterConditions);

        Tips tips = new Tips();
        tips.updateTip();
    }

    private void addExperience(int category) {
        ExperienceManager experienceManager = new ExperienceManager();

        switch (category) {
            case 1:
                experienceManager.addExperience(100);
                break;
            case 2:
                experienceManager.addExperience(80);
                break;
            case 3:
                experienceManager.addExperience(60);
                break;
            case 4:
                experienceManager.addExperience(40);
                break;
            case 5:
                experienceManager.addExperience(20);
                break;
            case 6:
                experienceManager.addExperience(10);
                break;
        }
    }

    private void updateMissions(int category, int vigilTime) {
        MissionsUpdater missionsUpdater = new MissionsUpdater();

        missionsUpdater.updateMission1((int) totalSleepTime);
        missionsUpdater.updateMission2((int) awakenings);
        missionsUpdater.updateMission3(efficiency);
        missionsUpdater.updateMission6(category);
        missionsUpdater.updateMission8(PercentageConverter.convertToPercentage(remSleepTime, totalSleepTime));
        missionsUpdater.updateMission9((int) positionChanges);
        missionsUpdater.updateMission10(vigilTime);
        missionsUpdater.updateMission11((int) positionChanges);
        missionsUpdater.updateMission12(PercentageConverter.convertToPercentage(lightSleepTime, totalSleepTime));

        AvatarDataAccess avatarDataAccess = new AvatarDataAccess(connection);
        int currentCategory = avatarDataAccess.getCharacterPhase();
        missionsUpdater.updateMission14(category, currentCategory);

        int totalEvents = (int) awakenings + (int) suddenMovements + (int) positionChanges;
        missionsUpdater.updateMission15(totalEvents);

        missionsUpdater.updateMission16((int) totalSleepTime);
        missionsUpdater.updateMission17(PercentageConverter.convertToPercentage(deepSleepTime, totalSleepTime));
    }
}