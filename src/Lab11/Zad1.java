package Lab11;

import java.util.Arrays;
import java.util.Random;

public class Zad1 {
    public static void main(String[] args) {
        int n = 70;
        Random random = new Random();
        int[] rng = new int[n];
        for (int i = 0; i < n; i++) {
            rng[i] = random.nextInt(2);
        }

        Hamming ham = new Hamming(15,11);

        int[] data = new int[]{1,1,0,1,1,0,0,1,1,0,0};

        int[] raw = rng;

        System.out.println("Encoding:");
        int[] encoded = ham.encoder(raw);

        System.out.println("Decoding:");
        int[] decoded = ham.decoder(encoded);

        System.out.println("Wynik:");
        System.out.println("raw:     " + Arrays.toString(raw));
        System.out.println("decoded: " + Arrays.toString(decoded));

        boolean git = true;
        for (int i = 0; i < raw.length; i++) {
            if(raw[i] != decoded[i]) {
                git = false;
                break;
            }
        }

        if (git) {
            System.out.println("Tablice takie same");
        } else {
            System.out.println("Tablice roznia sie");
        }
    }
}
