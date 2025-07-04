package Lab10;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Zad1 {

    public static void main(String[] args) {
        int n = 50;
        Random random = new Random();
        int[] rng = new int[n];
        for (int i = 0; i < n; i++) {
            rng[i] = random.nextInt(2);
        }

        Hamming ham = new Hamming(7,4);

        int[] data = new int[]{1,1,0,1};

        int[] raw = rng;

        int[] encoded = ham.encoder(raw);
        System.out.println("encoded: " + Arrays.toString(encoded));

        encoded[2] ^= 1;
        encoded[15] ^= 1;
        encoded [30] ^= 1;

        int[] decoded = ham.decoder(encoded);

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
