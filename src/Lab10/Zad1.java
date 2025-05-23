package Lab10;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Zad1 {

    public static void main(String[] args) {
        int[] raw = new int[]{1, 0, 1, 1, 0, 0, 1};

        Hamming ham = new Hamming(7,4);

        int[] encoded = ham.encoder(raw);


        for (int i = 0; i < encoded.length; i++) {
            System.out.print(encoded[i]);
        }
        System.out.println();

    }


}
