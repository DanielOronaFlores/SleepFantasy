package SoundEcofilter;

public class FrequencyFilter {
    private final int samplesPerSegment;
    private final double[] samples;
    float sampleRate;

    public FrequencyFilter(int samplesPerSegment, double[] samples, float sampleRate) {
        this.samplesPerSegment = samplesPerSegment;
        this.samples = samples;
        this.sampleRate = sampleRate;
    }

    public void filterLowerFrequenciesByHertz(int hertz) {
        for (int i = 0; i < samplesPerSegment / 2; i++) {
            double frequencyHz = i * sampleRate / samplesPerSegment;
            if (frequencyHz >= hertz) {
                deleteRealComponent(i);
                deleteImaginaryComponent(i);
            }
        }
    }

    public double[] getSamples() {
        return samples;
    }

    private void deleteRealComponent(int i) {
        samples[2 * i] = 0;
    }
    private void deleteImaginaryComponent(int i) {
        samples[2 * i + 1] = 0;
    }
}