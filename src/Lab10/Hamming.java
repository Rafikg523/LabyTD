package Lab10;

import java.util.ArrayList;
import java.util.List;

public class Hamming {
    int dataBitsNumber;
    int redundantBitsNumber;
    int totalNumberOfBits;
    int[] redundatBitsIds;

    public Hamming(int total, int data) {
        this.dataBitsNumber = data;
        this.redundantBitsNumber = total - data;
        this.totalNumberOfBits = total;
        this.redundatBitsIds = getRedundatBitsIds();
    }

    public int[] getRedundatBitsIds() {
        int[] result = new int[redundantBitsNumber];
        for (int i = 0; i < redundantBitsNumber; i++) {
            result[i] = (1 << i) - 1;
        }
        return result;
    }

    public int getParityBit(int id, int[] code) {
        int parity = 0;
        for (int i = 0; i < code.length; i++) {
            if (((i + 1) & (1 << id)) != 0) {
                parity += code[i];
            }
        }

        return parity % 2;
    }

    public int[][] encoder(int[] raw) {
        int blockNumber = (int) Math.ceil(raw.length / dataBitsNumber);
        int[][] result = new int[blockNumber][totalNumberOfBits];

        for (int i = 0; i < blockNumber; i++) {
            int[] block = new int[dataBitsNumber];
            for (int j = 0; j < dataBitsNumber; j++) {
                block[j] = raw[i * dataBitsNumber + j];
            }
            int idBlock = 0;
            int idChecked = 0;
            for (int j = 0; j < totalNumberOfBits; j++) {
                if (idChecked < redundantBitsNumber && redundatBitsIds[idChecked] == j) {
                    idChecked++;
                    continue;
                };
                result[i][j] = block[idBlock++];
            }

            for (int j = 0; j < redundantBitsNumber; j++) {
                int parityBit = getParityBit(j, result[i]);
                result[i][redundatBitsIds[j]] = parityBit;
            }
        }
        return result;
    }

    public int[][] decoder(int[] encoded) {
        int blockNumber = (int) Math.ceil((double) encoded.length / totalNumberOfBits);
        int[][] result = new int[blockNumber][dataBitsNumber];

        for (int i = 0; i < blockNumber; i++) {
            int[] block = new int[totalNumberOfBits];
            for (int j = 0; j < totalNumberOfBits; j++) {
                block[j] = encoded[i * totalNumberOfBits + j];
            }

            int controlSum = 0;
            for (int j = 0; j < redundantBitsNumber; j++) {
                int expectedParity = getParityBit(j, block);
                if (expectedParity != 0) {
                    controlSum += (1 << j);
                }
            }

            if (controlSum > 0) {
                if (controlSum <= totalNumberOfBits) {
                    System.err.println("Suma kontrolna przekracza ilosc bitów w bloku");
                    return null;
                }
                System.err.println("Bledny bit: " + (i * totalNumberOfBits + controlSum - 1));
                block[controlSum - 1] ^= 1;
            }

            int idResult = 0;
            int idBlock = 0;
            int idChecked = 0;
            while (idBlock < totalNumberOfBits) {
                if (idChecked < redundantBitsNumber && redundatBitsIds[idChecked] == idBlock) {
                    idChecked++;
                    idBlock++;
                    continue;
                }

                result[i][idResult++] = block[idBlock++];
            }
        }

        return result;
    }

}
