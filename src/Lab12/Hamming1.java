package Lab12;

import java.util.Arrays;

public class Hamming1 extends Coder {
    int dataBitsNumber;
    int redundantBitsNumber;
    int totalNumberOfBits;
    int[] redundatBitsIds;

    public Hamming1(int total, int data) {
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

    public int[] encode(int[] raw) {
        int blockNumber = (int) Math.ceil((double) raw.length / dataBitsNumber);
        int[] result = new int[blockNumber * totalNumberOfBits];

        for (int i = 0; i < blockNumber; i++) {
            int[] block = new int[dataBitsNumber];
            for (int j = 0; j < dataBitsNumber; j++) {
                int index = i * dataBitsNumber + j;
                if (index < raw.length) {
                    block[j] = raw[index];
                } else {
                    block[j] = 0;
                }
            }

            int idBlock = 0;
            int idChecked = 0;
            for (int j = 0; j < totalNumberOfBits; j++) {
                if (idChecked < redundantBitsNumber && redundatBitsIds[idChecked] == j) {
                    idChecked++;
                    continue;
                }
                result[i * totalNumberOfBits + j] = block[idBlock++];
            }

            for (int j = 0; j < redundantBitsNumber; j++) {
                int[] currentBlock = Arrays.copyOfRange(result, i * totalNumberOfBits, (i + 1) * totalNumberOfBits);
                int parityBit = getParityBit(j, currentBlock);
                result[i * totalNumberOfBits + redundatBitsIds[j]] = parityBit;
            }
        }
        return result;
    }


    public int[] decode(int[] encoded) {
        int blockNumber = (int) Math.ceil((double) encoded.length / totalNumberOfBits);
        int[] result = new int[blockNumber * dataBitsNumber];

        for (int i = 0; i < blockNumber; i++) {
            int[] block = new int[totalNumberOfBits];
            for (int j = 0; j < totalNumberOfBits; j++) {
                int index = i * totalNumberOfBits + j;
                if (index < encoded.length) {
                    block[j] = encoded[index];
                } else {
                    block[j] = 0;
                }
            }

            int controlSum = 0;
            for (int j = 0; j < redundantBitsNumber; j++) {
                int parity = getParityBit(j, block);
                if (parity != 0) {
                    controlSum += (1 << j);
                }
            }

            if (controlSum > 0) {
                if (controlSum <= totalNumberOfBits) {
                    System.err.println("Błędny bit: " + (i * totalNumberOfBits + controlSum - 1));
                    block[controlSum - 1] ^= 1;

                } else {
                    System.err.println("Suma kontrolna przekracza liczbę bitów w bloku");
                    return null;
                }
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
                result[i * dataBitsNumber + idResult++] = block[idBlock++];
            }
        }

        return result;
    }


}
