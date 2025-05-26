package Lab10;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Zad1 {

    public static void main(String[] args) {
        int n = 40;
        Random random = new Random();
        int[] rng = new int[n];
        for (int i = 0; i < n; i++) {
            rng[i] = random.nextInt(2);
        }

        Hamming ham = new Hamming(7,4);

        int[][] encoded = ham.encoder(rng);


        for (int i = 0; i < encoded.length; i++) {
            System.out.println(i + 1 + ". " + Arrays.toString(encoded[i]));

        }
        System.out.println();

        int totalLength = encoded.length * encoded[0].length;
        int[] flattened = new int[totalLength];
        int index = 0;

        for (int i = 0; i < encoded.length; i++) {
            for (int j = 0; j < encoded[i].length; j++) {
                flattened[index++] = encoded[i][j];
            }
        }

        Hamming ham2 = new Hamming(7,4);

        flattened[23] = flattened[23] == 1 ? 0 : 1;
        //flattened[24] = flattened[24] == 1 ? 0 : 1;

        int[][] decoded = ham2.decoder(flattened);

        int totalLength2 = decoded.length * decoded[0].length;
        int[] flattened2 = new int[totalLength2];
        int index2 = 0;

        for (int i = 0; i < decoded.length; i++) {
            for (int j = 0; j < decoded[i].length; j++) {
                flattened2[index2++] = decoded[i][j];
            }
        }

        System.out.println(Arrays.toString(rng));
        System.out.println(Arrays.toString(flattened2));

    }


}
