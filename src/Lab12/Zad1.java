package Lab12;

import java.util.Arrays;
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
        modulators[1] = new PSK();
        modulators[2] = new FSK();

        Coder[] coders = new Coder[2];
        coders[0] = new Hamming1(7, 4);
        coders[1] = new Hamming2(15, 11);

        for (int m = 0; m < 1; m++) {
            Modulator modulator = modulators[m];
            for (int c = 0; c < 2; c++) {

                Coder coder = coders[c];
                int[] encodedSignal = coder.encode(raw);
                double[] modulatedSignal = modulator.modulate(encodedSignal);
                int[] demodulatedSignal = modulator.demodulate(modulatedSignal);
                int[] decodedSignal = coder.decode(demodulatedSignal);

                boolean isEqual = true;
                for (int i = 0; i < raw.length; i++) {
                    if (raw[i] != decodedSignal[i]) {
                        isEqual = false;
                        break;
                    }
                }
                if (isEqual) {
                    System.out.println("Takie same!");
                } else {
                    System.out.println("Różne!");
                }
                System.out.println();
            }
        }
    }
}
