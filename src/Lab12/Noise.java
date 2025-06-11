package Lab12;

import java.util.Random;
import Chart.Chart;
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

        XYSeries series = new XYSeries("Noise");

        for (int i = 0; i < signal.length; i++) {
            double t = i * time / signal.length;
            double g = Math.exp(-beta * t) * Math.max(0, 1 - t / t0);
            modulatedSignal[i] = signal[i] * g;
            series.add(t, g);
        }
        java.io.File chartFile = new java.io.File("src/Lab12/plots/tlumienie_" + "_" + Double.toString(beta) + ".png");
        if (!chartFile.exists()) {
            Chart.saveChart(series, "i", "signal", "src/Lab12/plots/tlumienie_" + "_" + Double.toString(beta) + ".png");
        }

        return modulatedSignal;
    }

}

