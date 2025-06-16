package Lab13;

import Lab13.Coder;

import java.util.Arrays;
import java.util.Random;

public class Hamming2 extends Coder {
    int dataBitsNumber;
    int redundantBitsNumber;
    int totalNumberOfBits;
    private int[] redundatBitsIds;
    private int[][] G;
    private int[][] H;

    public Hamming2(int total, int data) {
        this.dataBitsNumber = data;
        this.redundantBitsNumber = total - data;
        this.totalNumberOfBits = total;
        this.redundatBitsIds = getRedundatBitsIds();
        this.G = generateG();
        this.H = generateH();
    }

    private int[] getRedundatBitsIds() {
        int[] result = new int[redundantBitsNumber];
        for (int i = 0; i < redundantBitsNumber; i++) {
            result[i] = (1 << i) - 1;
        }
        return result;
    }

    private boolean isRedundant(int pos) {
        for (int i : redundatBitsIds) if (i == pos) return true;
        return false;
    }

    private int getParityBit(int id, int[] code) {
        int parity = 0;
        for (int i = 0; i < code.length; i++) {
            if (((i + 1) & (1 << id)) != 0) {
                parity += code[i];
            }
        }

        return parity % 2;
    }


    private int[][] generateG() {
        int[][] G = new int[dataBitsNumber][totalNumberOfBits];
        int dataId = 0;
        for (int i = 0; i < totalNumberOfBits; i++) {
            if (!isRedundant(i)) {
                G[dataId++][i] = 1;
            }
        }
        for (int row = 0; row < dataBitsNumber; row++) {
            for (int i = 0; i < redundantBitsNumber; i++) {
                int col = redundatBitsIds[i];
                dataId = 0;
                for (int j = 0; j < totalNumberOfBits; j++) {
                    if (!isRedundant(j)) {
                        if (dataId == row) {
                            if (((j + 1) & (1 << i)) != 0) G[row][col] = 1;
                        }
                        dataId++;
                    }
                }
            }
        }
        return G;
    }

    private int[][] generateH() {
        int[][] H = new int[redundantBitsNumber][totalNumberOfBits];
        for (int col = 0; col < totalNumberOfBits; col++) {
            int v = col + 1;
            for (int row = 0; row < redundantBitsNumber; row++) {
                H[row][col] = (v >> row) & 1;
            }
        }
        return H;
    }

    public int[] encode(int[] raw) {
        int blockNumber = (int) Math.ceil((double) raw.length / dataBitsNumber);
        int[] result = new int[blockNumber * totalNumberOfBits];

        for (int i = 0; i < blockNumber; i++) {
            int[] block = new int[dataBitsNumber];
            int[] encodedBlock = new int[totalNumberOfBits];

            for (int j = 0; j < dataBitsNumber; j++) {
                int index = i * dataBitsNumber + j;
                if (index < raw.length) {
                    block[j] = raw[index];
                }
                else block[j] = 0;
            }

            for (int j = 0; j < totalNumberOfBits; j++) {
                int sum = 0;
                for (int k = 0; k < dataBitsNumber; k++) {
                    sum += block[k] * G[k][j];
                }
                encodedBlock[j] = sum % 2;
            }

//            System.out.println("Block: " + i);
//            System.out.println("Raw:     " + Arrays.toString(block));
//            System.out.println("Encoded: " + Arrays.toString(encodedBlock));
//            System.out.println();

//            Random random = new Random();
//            int id = random.nextInt(totalNumberOfBits);
//            encodedBlock[id] ^= 1;

            for (int k = 0; k < totalNumberOfBits; k++) {
                result[i * totalNumberOfBits + k] = encodedBlock[k];
            }
        }
        return result;
    }


    public int[] decode(int[] encoded) {
        int blockNumber = encoded.length / totalNumberOfBits;
        int[] result = new int[blockNumber * dataBitsNumber];
        int correctedBits = 0;

        for (int i = 0; i < blockNumber; i++) {
            int[] block = Arrays.copyOfRange(encoded, i * totalNumberOfBits, (i + 1) * totalNumberOfBits);
            int[] s = new int[redundantBitsNumber];

//            System.out.println("Block: " + i);
//            System.out.println("Encoded:   " + Arrays.toString(block));

            for (int row = 0; row < redundantBitsNumber; row++) {
                int sum = 0;
                for (int col = 0; col < totalNumberOfBits; col++) {
                    sum += H[row][col] * block[col];
                }
                s[row] = sum % 2;
            }

            int errPos = 0;
            for (int j = 0; j < redundantBitsNumber; j++) {
                errPos += (s[j] << j);
            }

            if (errPos != 0 && errPos <= totalNumberOfBits) {
                block[errPos - 1] ^= 1;
                correctedBits++;
//                System.out.println("Corrected: " + Arrays.toString(block));
//                System.out.println(" ".repeat((errPos - 1) * 3 + 12) + "^");
                //System.err.println("Błąd: Block: " + i + " Bit: " + errPos + " (" + (i * totalNumberOfBits + errPos - 1) + ")");
            }

            int[] decoded = new int[dataBitsNumber];

            int dataId = 0;
            for (int j = 0; j < totalNumberOfBits; j++) {
                if (!isRedundant(j)) {
                    decoded[dataId] = block[j];
                    dataId++;
                }
            }

//            System.out.println("Decoded:   " + Arrays.toString(decoded));
//            System.out.println();

            for (int j = 0; j < dataBitsNumber; j++) {
                result[i * dataBitsNumber + j] = decoded[j];
            }
        }
        //System.out.println("Poprawiono: " + correctedBits);
        return result;
    }

    public String getName() {
        return "Hamming(" + totalNumberOfBits + ", " + dataBitsNumber + ")";
    }
}