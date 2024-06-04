package SleepEvaluator;

import java.util.Arrays;
import java.util.List;

import AppContext.MyApplication;
import Calculators.SleepData;
import Database.DataAccess.AvatarDataAccess;
import Database.DataAccess.ProbabilitiesDataAccess;
import Database.DataUpdates.AvatarDataUpdate;
import Database.DatabaseConnection;
import Files.AudiosPaths;
import GameManagers.Challenges.ChallengesManager;
import GameManagers.Challenges.ChallengesUpdater;
import GameManagers.ExperienceManager;
import GameManagers.Missions.MissionsUpdater;
import GameManagers.Monsters.MonsterConditions;
import GameManagers.Monsters.MonstersManager;
import Models.Sound;
import Serializers.Deserializer;
import SleepEvaluator.Trainer.PrioriCategories;
import Tips.Tips;
import Utils.PercentageConverter;
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
    private int timeInBed;

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
    private final double[] finalCategoriesProbabilities = new double[7]; // 7 categories

    public SleepEvaluator() {
        probabilitiesDataAccess = new ProbabilitiesDataAccess(connection);
    }

    private void setRanges() {
        ranges[0] = RangesValues.totalSleepTime(totalSleepTime);
        ranges[1] = RangesValues.lightSleepTime(lightSleepTime);
        ranges[2] = RangesValues.deepSleepTime(deepSleepTime);
        ranges[3] = RangesValues.remSleepTime(remSleepTime);
        ranges[4] = RangesValues.efficiency(efficiency);
        ranges[5] = RangesValues.awakenings(awakenings);
        ranges[6] = RangesValues.suddenMovements(suddenMovements);
        ranges[7] = RangesValues.positionChanges(positionChanges);
    }

    private void getInitialAttributeProbabilitiesPerRange(float[] attribute, int range, String attributeName) {
        for (int i = 0; i < attribute.length; i++) {
            String category = "category" + (i + 1);
            attribute[i] = probabilitiesDataAccess.getProbability(category, range, attributeName);
        }
    }

    private void generateCategoryProbabilities(int attributeID, float[] attribute) {
        System.arraycopy(attribute, 0, categoryProbabilities[attributeID], 0, attribute.length);
    }

    private void calculateFinalAttributeProbabilities() {
        float[] prioriCategories = PrioriCategories.getPrioriProbabilities();

        for (int column = 0; column < categoryProbabilities[0].length; column++) {
            float mult = 1;
            for (float[] categoryProbability : categoryProbabilities) {
                mult *= categoryProbability[column];
            }
            mult *= prioriCategories[column];
            finalCategoriesProbabilities[column] = mult;
        }
    }

    private int evaluateSleep() {
        lightSleepTime = PercentageConverter.convertToPercentage(lightSleepTime, totalSleepTime);
        deepSleepTime = PercentageConverter.convertToPercentage(deepSleepTime, totalSleepTime);
        remSleepTime = PercentageConverter.convertToPercentage(remSleepTime, totalSleepTime);

        setRanges();

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
        double maxProbability = 0;
        int category = 0;
        for (int i = 0; i < finalCategoriesProbabilities.length; i++) {
            if (finalCategoriesProbabilities[i] > maxProbability) {
                maxProbability = finalCategoriesProbabilities[i];
                System.out.println("Probabilidad Maxima: " + maxProbability);
                category = i + 1;
            }
        }
        return category;
    }

    private void addExperience(int category) {
        int experience;

        experience = switch (category) {
            case 1 -> 100;
            case 2 -> 80;
            case 3 -> 60;
            case 4 -> 40;
            case 5 -> 20;
            case 6 -> 10;
            default -> 0;
        };

        ExperienceManager experienceManager = new ExperienceManager();
        experienceManager.addExperience(experience);
    }

    private void updateMissions(int category, int vigilTime) {
        MissionsUpdater missionsUpdater = new MissionsUpdater();

        missionsUpdater.updateMission1((int) totalSleepTime);
        missionsUpdater.updateMission2((int) awakenings);
        missionsUpdater.updateMission3(efficiency);

        missionsUpdater.updateMission6(category);

        missionsUpdater.updateMission8(remSleepTime);
        missionsUpdater.updateMission9((int) positionChanges);
        missionsUpdater.updateMission10(vigilTime);
        missionsUpdater.updateMission11((int) positionChanges);
        missionsUpdater.updateMission12(lightSleepTime);

        AvatarDataAccess avatarDataAccess = new AvatarDataAccess(connection);
        int oldCategory = avatarDataAccess.getCharacterPhase();
        missionsUpdater.updateMission14(category, oldCategory);

        int totalEvents = (int) awakenings + (int) suddenMovements + (int) positionChanges;
        missionsUpdater.updateMission15(totalEvents);

        missionsUpdater.updateMission16((int) suddenMovements);
        missionsUpdater.updateMission17(deepSleepTime);

        missionsUpdater.updateMission20(timeInBed);
    }

    public void evaluate(int vigilTime, int lightSleepTime, int deepSleepTime, int remSleepTime, int awakenings, int suddenMovements, int positionChanges, boolean[] monsterConditions) {
        this.lightSleepTime = lightSleepTime;
        this.deepSleepTime = deepSleepTime;
        this.remSleepTime = remSleepTime;
        this.awakenings = awakenings;
        this.suddenMovements = suddenMovements;
        this.positionChanges = positionChanges;

        totalSleepTime = SleepData.getTotalSleepTime(lightSleepTime, deepSleepTime, remSleepTime);
        timeInBed = SleepData.getTimeInBed(vigilTime, (int) totalSleepTime);
        efficiency = SleepData.getSleepEfficiency(totalSleepTime, timeInBed);

        int category = evaluateSleep();

        AvatarDataUpdate avatarDataUpdate = new AvatarDataUpdate(connection);
        avatarDataUpdate.updateCharacterPhase((byte) category);

        ChallengesUpdater challengesUploader = new ChallengesUpdater();
        challengesUploader.updateCategoryRecord(category);

        updateMissions(category, vigilTime);
        addExperience(category);

        if (MonsterConditions.isInsomnia((int) efficiency)) {
            monsterConditions[0] = true;
            System.out.println("Monstruos: ha aparecido un monstruo por insomnio");
        }
        
        SecondsCounter secondsCounter = new SecondsCounter();
        List<Sound> soundsList = Deserializer.deserializeFromXML(AudiosPaths.getListSoundsPath());
        int loudSoundsSeconds = secondsCounter.getTotalSeconds(soundsList);
        System.out.println("Sonidos fuertes: " + loudSoundsSeconds);
        if (MonsterConditions.isLoudSound(loudSoundsSeconds)) {
            monsterConditions[1] = true;
            System.out.println("Monstruos: ha aparecido un monstruo por ruido fuerte");
        }

        MonstersManager monstersManager = new MonstersManager();
        monstersManager.updateMonster(monsterConditions);

        Tips tips = new Tips();
        tips.updateTip();

        ChallengesUpdater challengesUpdater = new ChallengesUpdater();
        challengesUpdater.updateSleepingConditions(); // Actualiza las condiciones de sueño de desafios

        ChallengesManager challengesManager = new ChallengesManager();
        challengesManager.manageChallenges();
    }
}