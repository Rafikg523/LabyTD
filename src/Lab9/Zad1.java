//Rafał Grzelak
package Lab9    ;

import Chart.Chart;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.jfree.data.xy.XYSeries;

public class Zad1 {
    public static void main(String[] args) {
        int[] B = {0,1,0,0,1};
        double Tb = 2.0;
        double T = Tb * B.length;

        String Z = "FSK";

        double W = 20.0;

        double fn1 = (W + 1.0) / Tb;
        double fn2 = (W + 2.0) / Tb;

        double fn = W / Tb;
        System.out.println(fn);

        int fs = 2000;

        int N = (int) Math.ceil(fs * T);
        int N2 = Integer.highestOneBit(N - 1) << 1;
        double Ts = 1.0 / fs;

        double h = 0.0;

        XYSeries Zseries = new XYSeries("z-" + Z);
        XYSeries X1series = new XYSeries("x1-" + Z);
        XYSeries X2series = new XYSeries("x2-" + Z);
        XYSeries P1series = new XYSeries("p1-" + Z);
        XYSeries P2series = new XYSeries("p2-" + Z);
        XYSeries Pseries = new XYSeries("p-" + Z);
        XYSeries Cseries = new XYSeries("c-" + Z);

        double[] s = new double[N2];
        double p1i = 0.0;
        double p2i = 0.0;
        double pi = 0.0;
        int BIdold = 0;

        for (int i = 0; i < N; i++) {
            double t = i * Ts;
            int BId = (int) Math.floor(i / (N / (double) B.length));
            double zi = 0;
            if (BId > 0 && BId != BIdold) {
                p1i = 0.0;
                p2i = 0.0;
                pi = 0.0;
            };
            BIdold = BId;

            if (B[BId] == 0) {
                zi = Math.sin(2 * Math.PI * fn1 * t);
            } else {
                zi = Math.sin(2 * Math.PI * fn2 * t);
            }

            Zseries.add(t, zi);

            double x1i = zi * Math.sin(2.0 * Math.PI * fn1 * t);
            double x2i = zi * Math.sin(2.0 * Math.PI * fn2 * t);

            X1series.add(t, x1i);
            X2series.add(t, x2i);

            p1i += x1i;
            p2i += x2i;
            pi += x2i - x1i;

            P1series.add(t, p1i);
            P2series.add(t, p2i);
            Pseries.add(t, pi);
        }

        Chart.saveChart(Zseries, "Czas [s]", "Amplituda", "src/Lab9/plots/z-" + Z + ".png");
        Chart.saveChart(X1series, "Czas [s]", "Amplituda", "src/Lab9/plots/x1-" + Z + ".png");
        Chart.saveChart(X2series, "Czas [s]", "Amplituda", "src/Lab9/plots/x2-" + Z + ".png");
        Chart.saveChart(P1series, "Czas [s]", "Amplituda", "src/Lab9/plots/p1-" + Z + ".png");
        Chart.saveChart(P2series, "Czas [s]", "Amplituda", "src/Lab9/plots/p2-" + Z + ".png");
        Chart.saveChart(Pseries, "Czas [s]", "Amplituda", "src/Lab9/plots/p-" + Z + ".png");

        int[] cs = new int[B.length];
        for (int i = 0; i < B.length; i++) {
            int id = N / B.length * i;
            int id1 = N / B.length * (i + 1) -1;

            double piy1 = Pseries.getY(id1).doubleValue();
            double t = T / N * id;
            double t1 = T / N * id1;

            double signal = piy1 > h ? 1.0 : 0.0;

            Cseries.add(t, signal);
            Cseries.add(t1, signal);

            cs[i] = (int) signal;
        }
        //Chart.saveChart3(Cseries, "Czas [s]", "Amplituda", "src/Lab8/plots/c-" + z + ".png");

        System.out.print(Z + " signal: ");
        for (int i = 0; i < cs.length; i++) {
            System.out.print(cs[i] + " ");
        }
        System.out.println("\n");

        XYSeries Bseries = new XYSeries("B-" + Z);
        for (int i = 0; i < B.length; i++) {
            int id = N / B.length * i;
            double t = id * T / N;
            double t1 = (N / B.length * (i + 1) - 1) * T / N;

            double Bi = B[i];

            Bseries.add(t, Bi);
            Bseries.add(t1, Bi);
        }

        int Ne = 0;
        for (int i = 0; i < Bseries.getItems().size(); i++) {
            if (Bseries.getY(i).doubleValue() != Cseries.getY(i).doubleValue()) {
                Ne++;
            }
        }

        double BER = (double) Ne / Bseries.getItems().size() * 100;

        String dane = "h: " + h + " BER: " + BER + "%";
        Chart.saveChart4(Cseries, Bseries, "Czas [s]", "Amplituda", "src/Lab9/plots/c-" + Z + ".png", dane);
    }
}
