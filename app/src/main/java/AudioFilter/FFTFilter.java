package AudioFilter;

import android.util.Log;

import org.jtransforms.fft.DoubleFFT_1D;

public class FFTFilter {
    private final int sampleRate;
    private final int samplesPerSegment;

    public FFTFilter(int sampleRate, int samplesPerSegment) {
        this.sampleRate = sampleRate;
        this.samplesPerSegment = samplesPerSegment;
    }

    public void applyFFT(double[] samples) {
        DoubleFFT_1D fft = new DoubleFFT_1D(samples.length);
        fft.realForward(samples);
        //Log.d("Audio", "FFT aplicada");
    }

    public void deleteHighFrequencies(double[] samples, int cutoffFrequency) {
        for (int i = 0; i < samplesPerSegment / 2; i++) {
            double frequencyHz = i * sampleRate / samplesPerSegment;
            if (frequencyHz >= cutoffFrequency) {
                samples[2 * i] = 0; // Real
                samples[2 * i + 1] = 0; // Imaginary
            }
        }
    }
}
