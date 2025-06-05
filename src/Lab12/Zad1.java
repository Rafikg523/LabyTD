package Lab12;

import Chart.Chart;
import org.jfree.data.xy.XYSeries;

import java.util.Random;

public class Zad1  {
    public static void main(String[] args) {
        int n = 70;
        Random random = new Random();
        int[] rng = new int[n];
        for (int i = 0; i < n; i++) {
            rng[i] = random.nextInt(2);
        }

        int[] data = new int[]{1,1,0,1,1,0,0,1,1,0,0};

        int[] raw = data;

        Modulator[] modulators = new Modulator[3];
        modulators[0] = new ASK(2.0, 10.0, 1.0, 2.0, 500);
        modulators[1] = new PSK(2.0, 10.0, 500);
        modulators[2] = new FSK(2.0, 20.0, 500);

        Coder[] coders = new Coder[2];
        coders[0] = new Hamming1(7, 4);
        coders[1] = new Hamming2(15, 11);

        Noise noise = new Noise();

        for (int m = 0; m < 3; m++) {
            Modulator modulator = modulators[m];
            for (int c = 0; c < 2; c++) {
                Coder coder = coders[c];
                String title = "Modulator: " + modulator.getName() + ", Coder: " + coder.getName();
                System.out.println(title);

                XYSeries series = new XYSeries(title);

                int BERcount = 100;
                for (int a = 0; a < BERcount; a++) {
                    double alpha = a / 0.5;
                    int[] encodedSignal = coder.encode(raw);
                    double[] modulatedSignal = modulator.modulate(encodedSignal);
                    double[] noisySignal = noise.addWhiteNoise(modulatedSignal, alpha);
                    int[] demodulatedSignal = modulator.demodulate(noisySignal);
                    int[] decodedSignal = coder.decode(demodulatedSignal);

                    int bitErrors = 0;
                    for (int i = 0; i < raw.length; i++) {
                        if (raw[i] != decodedSignal[i]) {
                            bitErrors++;
                        }
                    }
                    double BER = (double) bitErrors / raw.length * 100.0;
                    series.add(alpha, BER);
                }

                Chart.saveChart3(series, "alpha", "BER %", "src/Lab12/plots/" + modulator.getName() + "_" + coder.getName() + ".png");

            }
        }
    }
}