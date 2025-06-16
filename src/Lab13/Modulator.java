package Lab13;

public abstract class Modulator {
    public abstract double[] modulate(int[] B);

    public abstract int[] demodulate(double[] Z);

    public abstract String getName();
}
