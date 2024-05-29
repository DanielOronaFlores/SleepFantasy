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
        System.out.println("----------Rangos-------------");
        System.out.println("totalSleepTime: " + totalSleepTime);
        System.out.println("lightSleepTime: " + lightSleepTime);
        System.out.println("deepSleepTime: " + deepSleepTime);
        System.out.println("remSleepTime: " + remSleepTime);
        System.out.println("efficiency: " + efficiency);
        System.out.println("awakenings: " + awakenings);
        System.out.println("suddenMovements: " + suddenMovements);
        System.out.println("positionChanges: " + positionChanges);

        ranges[0] = RangesValues.totalSleepTime(totalSleepTime);
        ranges[1] = RangesValues.lightSleepTime(lightSleepTime);
        ranges[2] = RangesValues.deepSleepTime(deepSleepTime);
        ranges[3] = RangesValues.remSleepTime(remSleepTime);
        ranges[4] = RangesValues.efficiency(efficiency);
        ranges[5] = RangesValues.awakenings(awakenings);
        ranges[6] = RangesValues.suddenMovements(suddenMovements);
        ranges[7] = RangesValues.positionChanges(positionChanges);

        System.out.println("----------Rangos-------------");
        System.out.println(Arrays.toString(ranges));
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
        System.out.println("-----------Probabilidades Priori--------------");
        System.out.println(Arrays.toString(prioriCategories));
        System.out.println("----------------------------------------------");

        for (int column = 0; column < categoryProbabilities[0].length; column++) {
            float mult = 1;
            for (float[] categoryProbability : categoryProbabilities) {
                //System.out.println("Probabilidad: " + categoryProbability[column]);
                mult *= categoryProbability[column];
            }
            //System.out.println("Multiplicación: " + mult);
            mult *= prioriCategories[column];
            finalCategoriesProbabilities[column] = mult;
        }
    }

    private int evaluateSleep() {
        lightSleepTime = PercentageConverter.convertToPercentage(lightSleepTime, totalSleepTime);
        deepSleepTime = PercentageConverter.convertToPercentage(deepSleepTime, totalSleepTime);
        remSleepTime = PercentageConverter.convertToPercentage(remSleepTime, totalSleepTime);

        setRanges();

        // Aqui solo se obtienen las probabilidades iniciales de cada atributo de la base de datos
        System.out.println("----------------Probabilidades de cada atributo----------------");
        getInitialAttributeProbabilitiesPerRange(totalSleepTimeProbailities, ranges[0], "totalSleepTime");
        getInitialAttributeProbabilitiesPerRange(lightSleepTimeProbabilities, ranges[1], "lightSleepTime");
        getInitialAttributeProbabilitiesPerRange(deepSleepTimeProbabilities, ranges[2], "deepSleepTime");
        getInitialAttributeProbabilitiesPerRange(remSleepTimeProbabilities, ranges[3], "remSleepTime");
        getInitialAttributeProbabilitiesPerRange(efficiencyProbabilities, ranges[4], "efficiency");
        getInitialAttributeProbabilitiesPerRange(awakeningsProbabilities, ranges[5], "awakenings");
        getInitialAttributeProbabilitiesPerRange(suddenMovementsProbabilities, ranges[6], "suddenMovements");
        getInitialAttributeProbabilitiesPerRange(positionChangesProbabilities, ranges[7], "positionChanges");
        printAll();

        // Generate the category probabilities
        generateCategoryProbabilities(0, totalSleepTimeProbailities);
        generateCategoryProbabilities(1, lightSleepTimeProbabilities);
        generateCategoryProbabilities(2, deepSleepTimeProbabilities);
        generateCategoryProbabilities(3, remSleepTimeProbabilities);
        generateCategoryProbabilities(4, efficiencyProbabilities);
        generateCategoryProbabilities(5, awakeningsProbabilities);
        generateCategoryProbabilities(6, suddenMovementsProbabilities);
        generateCategoryProbabilities(7, positionChangesProbabilities);

        //Imprimir las probabilidades de cada categoria
        printProba();

        //Calculate the final probabilities
        calculateFinalAttributeProbabilities();
        System.out.println("Probabilidades Finales: " + Arrays.toString(finalCategoriesProbabilities));

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

    private void printProba() {
        System.out.println("----------------Probabilidades categoryProbabilities----------------");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 7; j++) {
                System.out.print(categoryProbabilities[i][j] + "\t");
            }
            System.out.println(); // Saltar a la siguiente línea después de imprimir una fila completa
        }
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
        timeInBed = SleepData.getTimeInBed(vigilTime, (int) totalSleepTime);
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
        int loudSoundsSeconds = secondsCounter.getTotalSeconds(soundsList);
        System.out.println("Sonidos fuertes: " + loudSoundsSeconds);
        if (MonsterConditions.isLoudSound(loudSoundsSeconds)) {
            monsterConditions[1] = true;
            System.out.println("Monstruos: ha aparecido un monstruo por ruido fuerte");
        }

        // appearingMonsters = {insomnia, loudSound, anxiety, nightmare, somnambulism}
        MonstersManager monstersManager = new MonstersManager();
        monstersManager.updateMonster(monsterConditions);

        Tips tips = new Tips();
        tips.updateTip();

        ChallengesUpdater challengesUpdater = new ChallengesUpdater(connection);
        challengesUpdater.updateSleepingConditions(); // Actualiza las condiciones de sueño de desafios

        ChallengesManager challengesManager = new ChallengesManager();
        challengesManager.manageChallenges();
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
        System.out.println("-------------------Misiones-------------------");
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

        System.out.println("Time in Bed: " + timeInBed);
        missionsUpdater.updateMission20(timeInBed);
    }

    private void printAll() {
        printArray(totalSleepTimeProbailities);
        printArray(lightSleepTimeProbabilities);
        printArray(deepSleepTimeProbabilities);
        printArray(remSleepTimeProbabilities);
        printArray(efficiencyProbabilities);
        printArray(awakeningsProbabilities);
        printArray(suddenMovementsProbabilities);
        printArray(positionChangesProbabilities);
    }

    private static void printArray(float[] array) {
        System.out.print("[");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
}