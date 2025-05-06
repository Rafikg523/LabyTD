//Rafał Grzelak
package Lab7;

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
        int[] Bin = {0,1,0,0,1,1,1,0,1,1};
        double Tb = 1.0;
        double T = Tb * Bin.length;

        String[] Z = {"ASK", "FSK", "PSK"};

        double W = 30.0;

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

        int[] B = {10, 6, 3};
        double[] Bwidth = new double[B.length];

        for (int o = 0; o < 3; o++) {
            String z = Z[o];
            XYSeries series = new XYSeries("z" + z);
            double[] s = new double[N2];

            for (int i = 0; i < N; i++) {
                double t = i * Ts;
                int BinId = (int) Math.floor(i / (N / (double) Bin.length));
                double si = 0;
                switch (o) {
                    case 0:
                        if (Bin[BinId] == 0) {
                            si = A1 * Math.sin(2 * Math.PI * fn * t);
                        } else {
                            si = A2 * Math.sin(2 * Math.PI * fn * t);
                        }
                        break;
                    case 1:
                        if (Bin[BinId] == 0) {
                            si = Math.sin(2 * Math.PI * fn1 * t);
                        } else {
                            si = Math.sin(2 * Math.PI * fn2 * t);
                        }
                        break;
                    case 2:
                        if (Bin[BinId] == 0) {
                            si = Math.sin(2 * Math.PI * fn * t);
                        } else {
                            si = Math.sin(2 * Math.PI * fn * t + Math.PI);
                        }

                }
                s[i] = si;
                series.add(t, si);
            }
            //Chart.saveChart(series, "Czas [s]", "Amplituda", "src/Lab6/plots/z" + z + ".png");

            XYSeries MSeries = new XYSeries(z + "M");
            FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);

            Complex[] complex = fft.transform(s, TransformType.FORWARD);

            double fri = (double) fs / (N);
            double last = 0;
            for (int i = 0; i <= fs; i++) {
                double fr = i * fri;
                double frLog = fr == 0 ? 0 : Math.log10(fr);

                double Re = complex[i].getReal();
                double Im = complex[i].getImaginary();

                double am = Math.sqrt(Math.pow(Re, 2) + Math.pow(Im, 2));
                double dB = 10.0 * Math.log10(am);

                double prog = 0;

                MSeries.add(fr, Math.max(prog, dB));
            }
            //Chart.saveChart(MSeries, "Częstotliwość [Hz]", "Amplituda", "src/Lab6/plots/M" + z + ".png", true);

            double top = MSeries.getMaxY();
            double[] min = new double[B.length];
            double[] max = new double[B.length];
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
                Bwidth[i] = Math.round((max[i] - min[i]) * 1000.0) / 1000.0;
            }
            Chart.saveChart(MSeries,"M-" + Z[o] , "Częstotliwość [Hz]", "Amplituda [dB]", "src/Lab7/plots/M-" + Z[o] + ".png", B, Bwidth, min, max);

            double E = 0.0;
            double maxY = MSeries.getMaxY();
            int topId = -1;
            for (int i = 0; i < MSeries.getItemCount(); i++) {
                E += Math.pow(MSeries.getY(i).doubleValue(), 2.0);
                if (maxY == MSeries.getY(i).doubleValue()) topId = i;
            }

            double ra = 0;
            int alpha = 0;
            int faId = topId;
            int fbId = topId;
            int p = 1;
            alpha = 1;
            while (ra <= 80) {
                faId = faId > 0 ? faId - alpha : faId;
                fbId = fbId < MSeries.getItemCount() ? fbId + alpha : fbId;
                double Ea = 0;
                for (int i = faId; i <= fbId; i++) {
                    Ea += Math.pow(MSeries.getY(i).doubleValue(), 2.0);
                }

                ra = Ea/E * 100;
                //System.out.println(p++ + ". Fa: " + faId + " Fb: " + fbId + " Ra: " + ra);
            }

            double Ba = MSeries.getX(fbId).doubleValue() - MSeries.getX(faId).doubleValue();

            String[] dane = new String[3];
            dane[0] = "alpha = " + (fbId - faId);
            dane[1] = "r = " + ra;
            dane[2] = "Ba = " + Ba;

            Chart.saveChart2(MSeries, "M2-" + Z[o] , "Częstotliwość [Hz]", "Amplituda [dB]", "src/Lab7/plots/" + "M2-" + Z[o]  + ".png", faId, fbId, dane);

        }
    }
}
