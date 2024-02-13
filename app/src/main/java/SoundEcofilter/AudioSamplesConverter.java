package SoundEcofilter;

public class AudioSamplesConverter {
    private final int samplesPerSegment;
    private final byte[] buffer;
    private final double[] samples;

    public AudioSamplesConverter(int samplesPerSegment, byte[] buffer, double[] samples) {
        if (samplesPerSegment <= 0 || buffer == null || samples == null || buffer.length != samplesPerSegment * 2 || samples.length != samplesPerSegment) {
            throw new IllegalArgumentException("Parámetros de entrada inválidos");
        }

        this.samplesPerSegment = samplesPerSegment;
        this.buffer = buffer;
        this.samples = samples;
    }

    public void convertSamples() {
        for (int i = 0; i < samplesPerSegment; i++) {
            samples[i] = ((buffer[i * 2 + 1] << 8) | (buffer[i * 2] & 0xFF)) / 32768.0;
        }
    }

    public double[] getSamples() {
        return samples;
    }
}