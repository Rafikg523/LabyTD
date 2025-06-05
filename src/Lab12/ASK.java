package Lab12;

import Chart.Chart;
import org.jfree.data.xy.XYSeries;

public class ASK extends Modulator {
    public double Tb;
    public double W;
    public double A1;
    public double A2;
    public int fs;

    public ASK(double Tb, double W, double A1, double A2, int fs){
        this.Tb = Tb;
        this.W = W;
        this.A1 = A1;
        this.A2 = A2;
        this.fs = fs;
    }

    public double[] modulate(int[] B) {
        double T = Tb * B.length;
        double fn = W / Tb;
        int N = (int) Math.ceil(fs * T);
        double Ts = 1.0 / fs;

        double[] Z = new double[N];

        for (int i = 0; i < N; i++) {
            double t = i * Ts;
            int BId = (int) Math.floor(i / (N / (double) B.length));
            double zi;

            if (B[BId] == 0) {
                zi = A1 * Math.sin(2 * Math.PI * fn * t);
            } else {
                zi = A2 * Math.sin(2 * Math.PI * fn * t);
            }

            Z[i] = zi;
        }

        return Z;
    }

    public int[] demodulate(double[] Z) {
        double T = (double) Z.length / fs;
        double fn = W / Tb;
        int Blength = (int) Math.ceil(T / Tb);

        int N = (int) Math.ceil(fs * T);
        double Ts = 1.0 / fs;

        double fi = 0.0;
        double h = (A1 + A2)/2 * N / T;
        double A = 1.0;

        double pi = 0.0;
        int BIdold = 0;
        double[] P = new double[N];

        for (int i = 0; i < N; i++) {
            double t = i * Ts;
            int BId = (int) Math.floor(i / (N / (double) Blength));
            double zi = Z[i];
            if (BId > 0 && BId != BIdold) pi = 0.0;
            BIdold = BId;


            double xi = zi * A * Math.sin(2.0 * Math.PI * fn * t + fi);

            pi += xi;
            P[i] = pi;
        }


        int[] cs = new int[Blength];

        for (int i = 0; i < Blength; i++) {
            int id = N / Blength * i;
            int id1 = N / Blength * (i + 1) -1;

            double piy1 = P[id1];
            double t = T / N * id;
            double t1 = T / N * id1;

            double signal = piy1 > h ? 1.0 : 0.0;

            cs[i] = (int) signal;
        }

        return cs;
    }

    public String getName() {
        return "ASK";
    }
}