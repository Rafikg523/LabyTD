package Lab13;

import Chart.Chart;
import Lab13.Modulator;
import org.jfree.data.xy.XYSeries;

public class FSK extends Modulator {
    public double Tb;
    public double W;
    public int fs;

    public FSK(double Tb, double W, int fs) {
        this.Tb = Tb;
        this.W = W;
        this.fs = fs;
    }

    public double[] modulate(int[] B) {
        double T = Tb * B.length;
        double fn1 = (W + 1.0) / Tb;
        double fn2 = (W + 2.0) / Tb;

        int N = (int) Math.ceil(fs * T);
        double Ts = 1.0 / fs;

        double[] Z = new double[N];
        for (int i = 0; i < N; i++) {
            double t = i * Ts;
            int BId = (int) Math.floor(i / (N / (double) B.length));
            double zi ;

            if (B[BId] == 0) {
                zi = Math.sin(2 * Math.PI * fn1 * t);
            } else {
                zi = Math.sin(2 * Math.PI * fn2 * t);
            }

            Z[i] = zi;
        }

        return Z;
    }

    public int[] demodulate(double[] Z) {
        double T = (double) Z.length / fs;
        double fn1 = (W + 1.0) / Tb;
        double fn2 = (W + 2.0) / Tb;
        int Blength = (int) Math.ceil(T / Tb);

        int N = (int) Math.ceil(fs * T);
        double Ts = 1.0 / fs;

        double h = 0.0;

        double p1i = 0.0;
        double p2i = 0.0;
        double pi = 0.0;
        int BIdold = 0;

        double[] P = new double[N];

//        XYSeries Zseries = new XYSeries("z-FSK");
//        XYSeries X1series = new XYSeries("x1-FSK");
//        XYSeries X2series = new XYSeries("x2-FSK");
//        XYSeries P1series = new XYSeries("p1-FSK");
//        XYSeries P2series = new XYSeries("p2-FSK");
//        XYSeries Pseries = new XYSeries("p-FSK");
//        XYSeries Cseries = new XYSeries("c-FSK");

        for (int i = 0; i < N; i++) {
            double t = i * Ts;
            int BId = (int) Math.floor(i / (N / (double) Blength));
            double zi = Z[i];
            if (BId > 0 && BId != BIdold) {
                p1i = 0.0;
                p2i = 0.0;
                pi = 0.0;
            }
            BIdold = BId;

            double x1i = zi * Math.sin(2.0 * Math.PI * fn1 * t);
            double x2i = zi * Math.sin(2.0 * Math.PI * fn2 * t);

            p1i += x1i;
            p2i += x2i;
            pi += x2i - x1i;

            P[i] = pi;

//            Zseries.add(t, zi);
//            X1series.add(t, x1i);
//            X2series.add(t, x2i);
//            P1series.add(t, p1i);
//            P2series.add(t, p2i);
//            Pseries.add(t, pi);
        }

        int[] cs = new int[Blength];
        for (int i = 0; i < Blength; i++) {
            int id = N / Blength * i;
            int id1 = N / Blength * (i + 1) -1;

            double piy1 = P[id1];
            double t = T / N * id;
            double t1 = T / N * id1;

            double signal = piy1 > h ? 1.0 : 0.0;

//            Cseries.add(t, signal);
//            Cseries.add(t1, signal);

            cs[i] = (int) signal;
        }

//        Chart.saveChart(Zseries, "Czas [s]", "Amplituda", "src/Lab12/plots/z-FSK.png");
//        Chart.saveChart(X1series, "Czas [s]", "Amplituda", "src/Lab12/plots/x1-FSK.png");
//        Chart.saveChart(X2series, "Czas [s]", "Amplituda", "src/Lab12/plots/x2-FSK.png");
//        Chart.saveChart(P1series, "Czas [s]", "Amplituda", "src/Lab12/plots/p1-FSK.png");
//        Chart.saveChart(P2series, "Czas [s]", "Amplituda", "src/Lab12/plots/p2-FSK.png");
//        Chart.saveChart(Pseries, "Czas [s]", "Amplituda", "src/Lab12/plots/p-FSK.png");

        return cs;
    }

    public String getName() {
        return "FSK";
    }
}
