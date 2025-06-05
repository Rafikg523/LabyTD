package Lab12;

import Chart.Chart;
import org.jfree.data.xy.XYSeries;

import java.util.Arrays;
import java.util.Random;

public class Zad1  {
    public static void main(String[] args) {
        int N  = 44;
        Random random = new Random();
        int[] rng = new int[N];
        for (int i = 0; i < N; i++) {
            rng[i] = random.nextInt(2);
        }

        int[] data = new int[]{1,1,0,1,1,0,0,1,1,0,0};

        int[] raw = rng;

        int fs = 500;
        double Tb = 1.0;
        double W = 10.0;
        Modulator[] modulators = new Modulator[3];
        modulators[0] = new ASK(Tb, W, 1.0, 0.5, fs);
        modulators[1] = new PSK(Tb, W, fs);
        modulators[2] = new FSK(Tb, W, fs);

        Coder[] coders = new Coder[2];
        coders[0] = new Hamming1(7, 4);
        coders[1] = new Hamming2(15, 11);

        Noise noise = new Noise();

        String[] noiseTypes = {"White", "Weird"};
        double[][] alpha = new double[2][10];
        alpha[0] = new double[]{0.0, 0.333, 0.666, 1.0, 1.333, 1.666, 2.0, 2.333, 2.666, 3.0};
        alpha[1] = new double[]{0.0, 0.222, 0.444, 0.666, 0.888, 1.111, 1.333, 1.555, 1.777, 2.0};


        for (Modulator modulator : modulators) {
            for (Coder coder : coders) {
                for (int n = 0; n < noiseTypes.length; n++) {
                    String noiseType = noiseTypes[n];
                    String title = "Modulator: " + modulator.getName() + ", Coder: " + coder.getName() + ", Noise: " + noiseType;
                    System.out.println(title);

                    XYSeries series = new XYSeries(title);

                    for (int a = 0; a < 10; a++) {
                        int[] encodedSignal = coder.encode(raw);
                        double[] modulatedSignal = modulator.modulate(encodedSignal);
                        double[] noisySignal;
                        if (n == 1) {
                            noisySignal = noise.addWeirdNoise(modulatedSignal, alpha[n][a], fs);
                        } else {
                            noisySignal = noise.addWhiteNoise(modulatedSignal, alpha[n][a]);
                        }

                        int[] demodulatedSignal = modulator.demodulate(noisySignal);
                        int[] decodedSignal = coder.decode(demodulatedSignal);

                        int bitErrors = 0;
                        for (int i = 0; i < raw.length; i++) {
                            if (raw[i] != decodedSignal[i]) {
                                bitErrors++;
                            }
                        }
                        double BER = (double) bitErrors / raw.length * 100.0;
                        series.add(alpha[n][a], BER);
                    }

                    Chart.saveChart3(series, "alpha", "BER %", "src/Lab12/plots/" + modulator.getName() + "_" + coder.getName() + "_" + noiseType + ".png");
                }
            }
        }
    }
}