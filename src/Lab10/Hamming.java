package Lab10;

import java.util.ArrayList;
import java.util.List;

public class Hamming {
    int dataBitsNumber;
    int redundantBitsNumber;
    int totalNumberOfBits;
    int[] redundatBitsIds;

    public Hamming(int dataBitsNumber, int redundantBitsNumber) {
        this.dataBitsNumber = dataBitsNumber;
        this.redundantBitsNumber = redundantBitsNumber;
        this.totalNumberOfBits = dataBitsNumber + redundantBitsNumber;
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

    public int[] encoder(int[] raw) {
        int[] bits = new int[raw.length];
        for (int i = 0; i < raw.length; i++) {
            bits[i] = raw[raw.length - 1 - i];
        }
        int[] result = new int[totalNumberOfBits];

        int idBits = 0;
        int idChecked = 0;
        for (int i = 0; i < totalNumberOfBits; i++) {
            if (idChecked < redundantBitsNumber && redundatBitsIds[idChecked] == i) {
                idChecked++;
                continue;
            };
            result[i] = bits[idBits++];
        }

        for (int i = 0; i < redundantBitsNumber; i++) {
            int parityBit = getParityBit(i, result);
            result[redundatBitsIds[i]] = parityBit;
        }

        int[] encoded = new int[totalNumberOfBits];
        for (int i = 0; i < result.length; i++) {
            encoded[i] = result[result.length - 1 - i];
        }

        return encoded;
    }

    public int[] decoder(int[] encoded){
        return null;
    }
}
