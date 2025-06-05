package Lab12;

import java.util.Random;

public class Noise {
    public double[] addWhiteNoise(double[] signal, double alpha) {
        Random random = new Random();
        double[] modulatedSignal = new double[signal.length];
        for (int i = 0; i < signal.length; i++) {
            double noise = random.nextGaussian() * alpha;
            modulatedSignal[i] = signal[i] + noise;
        }
        return modulatedSignal;
    }
}
