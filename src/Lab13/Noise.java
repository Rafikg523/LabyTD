package Lab13;

import java.util.Random;

import org.jfree.data.xy.XYSeries;

public class Noise {
    public double[] addWhiteNoise(double[] signal, double alpha) {
        Random random = new Random();
        double[] modulatedSignal = new double[signal.length];

        for (int i = 0; i < signal.length; i++) {
            double noise = random.nextGaussian() * alpha ;
            modulatedSignal[i] = signal[i] + noise;
        }
        return modulatedSignal;
    }

    public double[] addWeirdNoise(double[] signal, double beta, int fs, String name) {
        double[] modulatedSignal = new double[signal.length];
        double time = (double) signal.length / fs;
        double t0 = time * 0.95;

        for (int i = 0; i < signal.length; i++) {
            double t = i * time / signal.length;
            double g = Math.exp(-beta * t) * Math.max(0, 1 - t / t0);
            modulatedSignal[i] = signal[i] * g;
        }

        return modulatedSignal;
    }

    public double[] addImpulseNoise(double[] signal, int K, int fs) {
        Random random = new Random();
        double[] modulatedSignal = new double[signal.length];

        double time = (double) signal.length / fs;

        int[] r = new int[K];
        for (int i = 0; i < K; i++) {
            r[i] = random.nextInt(signal.length);
        }

        double[] A = new double[K];
        for (int i = 0; i < K; i++) {
            A[i] = random.nextDouble() * 2 - 1;
        }

        int ri = 0;
        for (int i = 0; i < signal.length; i++) {
            double t = i * time / signal.length;
            if (K != 0 && i == r[ri]) {
                modulatedSignal[i] = signal[i] + A[ri];
                ri++;
                if (ri >= K) {
                    break;
                }
            } else {
                modulatedSignal[i] = signal[i];
            }
        }
        return modulatedSignal;
    }

}

