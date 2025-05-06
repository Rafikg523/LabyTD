//Rafał Grzelak
package Lab6;

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
        int[] B = {0,1,0,0,1,1,1,0,1,1};
        double Tb = 1.0;
        double T = Tb * B.length;

        char[] Z = {'A', 'F', 'P'};

        double W = 2.0;

        double A1 = 1.0;
        double A2 = 2.0;

        double fn = W / Tb;
        System.out.println(fn);
        double fn1 = (W + 1.0) / Tb;
        double fn2 = (W + 2.0) / Tb;

        int fs = 1000;

        int N = (int) Math.ceil(fs * T);
        int N2 = Integer.highestOneBit(N - 1) << 1;
        double Ts = 1.0 / fs;

        for (int o = 0; o < 3; o++) {
            char z = Z[o];
            XYSeries series = new XYSeries("z" + z);
            double[] s = new double[N2];

            for (int i = 0; i < N; i++) {
                double t = i * Ts;
                int BId = (int) Math.floor(i / (N / (double) B.length));
                double si = 0;
                switch (o) {
                    case 0:
                        if (B[BId] == 0) {
                            si = A1 * Math.sin(2 * Math.PI * fn * t);
                        } else {
                            si = A2 * Math.sin(2 * Math.PI * fn * t);
                        }
                        break;
                    case 1:
                        if (B[BId] == 0) {
                            si = Math.sin(2 * Math.PI * fn1 * t);
                        } else {
                            si = Math.sin(2 * Math.PI * fn2 * t);
                        }
                        break;
                    case 2:
                        if (B[BId] == 0) {
                            si = Math.sin(2 * Math.PI * fn * t);
                        } else {
                            si = Math.sin(2 * Math.PI * fn * t + Math.PI);
                        }

                }
                s[i] = si;
                series.add(t, si);
            }
            Chart.saveChart(series, "Czas [s]", "Amplituda", "src/Lab6/plots/z" + z + ".png");

            XYSeries MSeries = new XYSeries(z + "M");
            FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);

            Complex[] complex = fft.transform(s, TransformType.FORWARD);

            double fri = (double) fs / (N);
            double last = 0;
            for (int i = 0; i <= fs; i++) {
                double fr = i * fri;
                double frLog = fr == 0 ? 0 : Math.log(fr);

                double Re = complex[i].getReal();
                double Im = complex[i].getImaginary();

                double am = Math.sqrt(Math.pow(Re, 2) + Math.pow(Im, 2));
                double dB = 10.0 * Math.log10(am);

                double prog = 0;

                MSeries.add(frLog, Math.max(am, prog));
            }

            Chart.saveChart(MSeries, "Częstotliwość [Hz]", "Amplituda", "src/Lab6/plots/M" + z + ".png", true);
        }
    }
}
