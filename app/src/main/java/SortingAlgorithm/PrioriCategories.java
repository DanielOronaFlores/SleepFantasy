package SortingAlgorithm;

public class PrioriCategories {
    public static float[] getPrioriProbabilities() {
        Instances deserializer = new Instances();
        int totalInstances = deserializer.countInstances();
        float[] prioriCategory = new float[7];

        for (int i = 0; i < 7; i++) {
            int categoryTotal = deserializer.countCategory(String.valueOf(i + 1));
            prioriCategory[i] = (float) categoryTotal / totalInstances;
        }

        return prioriCategory;
    }
}
