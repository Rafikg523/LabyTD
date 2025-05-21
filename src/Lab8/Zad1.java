//Rafał Grzelak
package Lab8    ;

import Chart.Chart;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.jfree.data.xy.XYSeries;

public class Zad1 {
    public static void main(String[] args) {
        int[] B = {0,1,0,0,1,1,0,1,0,1};
        double Tb = 2.0;
        double T = Tb * B.length;

        String[] Z = {"ASK", "PSK"};

        double W = 10.0;

        double A1 = 1.0;
        double A2 = 2.0;

        double fn = W / Tb;
        System.out.println(fn);

        int fs = 500;

        int N = (int) Math.ceil(fs * T);
        int N2 = Integer.highestOneBit(N - 1) << 1;
        double Ts = 1.0 / fs;

        double[] fi = {0.0, Math.PI};
        //double[] h = {(A1 + A2)/2 * N / B.length / Tb, 0.0};
        double[] h = {500.0, -100.0};
        double A = 1.0;

        for (int o = 0; o < 2; o++) {
            String z = Z[o];
            XYSeries Zseries = new XYSeries("z-" + z);
            XYSeries Xseries = new XYSeries("x-" + z);
            XYSeries Pseries = new XYSeries("p-" + z);
            XYSeries Cseries = new XYSeries("c-" + z);

            double[] s = new double[N2];
            double pi = 0.0;
            int BIdold = 0;

            for (int i = 0; i < N; i++) {
                double t = i * Ts;
                int BId = (int) Math.floor(i / (N / (double) B.length));
                double zi = 0;
                if (BId > 0 && BId != BIdold) pi = 0.0;
                BIdold = BId;
                switch (o) {
                    case 0:
                        if (B[BId] == 0) {
                            zi = A1 * Math.sin(2 * Math.PI * fn * t);
                        } else {
                            zi = A2 * Math.sin(2 * Math.PI * fn * t);
                        }
                        break;
                    case 1:
                        if (B[BId] == 0) {
                            zi = Math.sin(2 * Math.PI * fn * t);
                        } else {
                            zi = Math.sin(2 * Math.PI * fn * t + Math.PI);
                        }

                }
                Zseries.add(t, zi);

                double xi = zi * A * Math.sin(2.0 * Math.PI * fn * t + fi[o]);
                Xseries.add(t, xi);

                pi += xi;
                Pseries.add(t, pi);
            }

            Chart.saveChart(Xseries, "Czas [s]", "Amplituda", "src/Lab8/plots/x-" + z + ".png");
            Chart.saveChart(Zseries, "Czas [s]", "Amplituda", "src/Lab8/plots/z-" + z + ".png");
            Chart.saveChart(Pseries, "Czas [s]", "Amplituda", "src/Lab8/plots/p-" + z + ".png");

            int[] cs = new int[B.length];

            for (int i = 0; i < B.length; i++) {
                int id = N / B.length * i;
                int id1 = N / B.length * (i + 1) -1;

                double piy1 = Pseries.getY(id1).doubleValue();
                double t = T / N * id;
                double t1 = T / N * id1;

                double signal = piy1 > h[o] ? 1.0 : 0.0;

                Cseries.add(t, signal);
                Cseries.add(t1, signal);

                cs[i] = (int) signal;
            }
            //Chart.saveChart3(Cseries, "Czas [s]", "Amplituda", "src/Lab8/plots/c-" + z + ".png");

            System.out.print(z + " signal: ");
            for (int i = 0; i < cs.length; i++) {
                System.out.print(cs[i] + " ");
            }
            System.out.println("\n");
            
            XYSeries Bseries = new XYSeries("B-" + z);
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

            String dane ="h: " + h[o] + "  BER: " + BER + "%";
            Chart.saveChart4(Cseries, Bseries, "Czas [s]", "Amplituda", "src/Lab8/plots/c1-" + z + ".png", dane);


        }
    }
}
