package SoundEcofilter;

public class BytesConverter {

    public BytesConverter() {
    }

    public byte[] samplesToBytes(double[] samples) {
        byte[] buffer = new byte[samples.length * 2];
        for (int i = 0; i < samples.length; i++) {
            short sampleValue = (short) (samples[i] * 32767.0);
            buffer[i * 2] = (byte) sampleValue;
            buffer[i * 2 + 1] = (byte) (sampleValue >> 8);
        }
        return buffer;
    }
}
