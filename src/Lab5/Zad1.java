//Rafał Grzelak
package Lab5;

import Chart.Chart;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.jfree.data.xy.XYSeries;

import java.util.HashMap;
import java.util.Map;

public class Zad1 {
    public static void main(String[] args) {
        int fs = 1024;
        int T = 1;
        int N = fs * T;
        double Ts = 1.0 / fs;

        double fm = 5;
        double fn = 300;

        char[] C = {'a', 'b', 'c'};
        char[] Z = {'A', 'P', 'F'};
        int[] B = {3, 6, 10};
        double[] Bwidth = new double[B.length];

        double[] kA = {0.5, 10, 40};
        double[] kP = {-4.0 * Math.PI, 5.0 * Math.PI, 4.0 * Math.PI};
        double[] kF = {-4.0 * Math.PI, 5.0 * Math.PI, 4.0 * Math.PI};

        Map<Character, double[]> K = new HashMap<>();
        K.put('a', kA);
        K.put('b', kP);
        K.put('c', kF);

        double[] m = new double[N];
        XYSeries mSeries = new XYSeries("m(t)");
        for (int i = 0; i < N; i++) {
            double t = i * Ts;
            m[i] = Math.sin(2 * Math.PI * fm * t);
            mSeries.add(t, m[i]);
        }

        for (int o = 0; o < 3; o++) {
            for (char c : C) {
                double k = K.get(c)[o];
                double[] z = new double[N];
                XYSeries series = new XYSeries("z" + c + "(t)");
                for (int i = 0; i < N; i++) {
                    double t = i * Ts;
                    switch (Z[o]) {
                        case 'A':
                            z[i] = (k * m[i] + 1) * Math.cos(2 * Math.PI * fn * t);
                            break;
                        case 'P':
                            z[i] = Math.cos(2 * Math.PI * fn * t + k * m[i]);
                            break;
                        case 'F':
                            z[i] = Math.cos(2 * Math.PI * fn * t + (k / fm) * m[i]);
                            break;
                    }
                    series.add(t, z[i]);
                }
                //Chart.saveChart(series, mSeries, "Czas [s]", "Amplituda", "src/Lab5/plots/" + c + "-z" + Z[o] + ".png");

                XYSeries MSeries = new XYSeries(c + "M");
                FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);

                Complex[] complex = fft.transform(z, TransformType.FORWARD);

                for (int i = 0; i <= fs / 2.0; i++) {
                    double fr = i * ((double) fs / N);

                    double Re = complex[i].getReal();
                    double Im = complex[i].getImaginary();

                    double complexi = Math.sqrt(Math.pow(Re, 2) + Math.pow(Im, 2));
                    double dB = 10.0 * Math.log10(complexi);

                    double prog = 0;

                    MSeries.add(fr, Math.max(dB, prog));
                }

                double top = MSeries.getMaxY();
                double[] min = new double[B.length];
                double[] max = new double[B.length];
                double[] minV = new double[B.length];
                double[] maxV = new double[B.length];
                for (int i = 0; i < B.length; i++) {
                    double bottom = top - B[i];
                    boolean isMin = false;
                    series:
                    for (int j = 0; j < MSeries.getItemCount(); j++) {
                        double v = MSeries.getY(j).doubleValue();
                        if (!isMin && v >= bottom) {
                            min[i] = MSeries.getX(j).doubleValue();
                            isMin = true;
                        }
                        if (v >= bottom) {
                            max[i] = MSeries.getX(j).doubleValue();
                        }
                    }
                    Bwidth[i] = max[i] - min[i];
                }
                Chart.saveChart(MSeries,c + "-M" + Z[o] , "Częstotliwość [Hz]", "Amplituda [dB]", "src/Lab5/plots/" + c + "-M" + Z[o] + ".png", B, Bwidth, min, max);
            }
        }
    }
}
