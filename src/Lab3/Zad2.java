//Rafał Grzelak
package Lab3;

import Chart.Chart;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.jfree.data.xy.XYSeries;

public class Zad2 {
    public static void main(String[] args) {
        int fs = 64;
        int T = 2;
        int N = fs * T;
        double Ts = 1.0 / fs;

        double f1 = 4;
        double f2 = 8;
        double a = 3.0;
        double b = 2.0;

        double[] x = new double[N];
        double[] y = new double[N];
        double[] z = new double[N];
        double[] Mzz = new double[N];

        XYSeries Xseries = new XYSeries("X(t)");
        XYSeries Yseries = new XYSeries("Y(t)");
        XYSeries Zseries = new XYSeries("Z(t)");

        XYSeries MXseries = new XYSeries("Mx");
        XYSeries MYseries = new XYSeries("My");
        XYSeries MZseries = new XYSeries("Mz");
        XYSeries MZZseries = new XYSeries("Mz^");

        for (int i = 0; i < N; i++) {
            double t = i * Ts;
            x[i] = 0.5 * Math.sin(2 * Math.PI * f1 * t);
            y[i] = Math.sin(2 * Math.PI * f2 * t) + 0.7 * Math.sin(2 * Math.PI * f1 * t);
            z[i] = a * x[i] + b * y[i];
            Xseries.add(t, x[i]);
            Yseries.add(t, y[i]);
            Zseries.add(t, z[i]);
        }

        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] xc = fft.transform(x, TransformType.FORWARD);
        Complex[] yc = fft.transform(y, TransformType.FORWARD);
        Complex[] zc = fft.transform(z, TransformType.FORWARD);

        for (int i = 0; i <= N / 2; i++) {
            double fr = i * ((double) fs / N);

            double xci = Math.sqrt(Math.pow(xc[i].getReal(), 2) + Math.pow(xc[i].getImaginary(), 2)) / N;
            double yci = Math.sqrt(Math.pow(yc[i].getReal(), 2) + Math.pow(yc[i].getImaginary(), 2)) / N;
            double zci = Math.sqrt(Math.pow(zc[i].getReal(), 2) + Math.pow(zc[i].getImaginary(), 2)) / N;

            double mzz = a * xci + b * yci;

            double xcidB = 10.0 * Math.log10(xci);
            double ycidB = 10.0 * Math.log10(yci);
            double zcidB = 10.0 * Math.log10(zci);
            double mzzdB = 10.0 * Math.log10(mzz);

            double prog = -20.0;

            MXseries.add(fr,xcidB > prog ? xcidB : prog);
            MYseries.add(fr,ycidB > prog ? ycidB : prog);
            MZseries.add(fr,zcidB > prog ? zcidB : prog);
            MZZseries.add(fr,mzzdB > prog ? mzzdB : prog);

        }


        Chart.saveChart(Xseries, "Czas [s]", "Amplituda", "src/Lab3/plots/Xt.png");
        Chart.saveChart(Yseries, "Czas [s]", "Amplituda", "src/Lab3/plots/Yt.png");
        Chart.saveChart(Zseries, "Czas [s]", "Amplituda", "src/Lab3/plots/Zt.png");

        Chart.saveChart(MXseries, "Częstotliwość [Hz]", "Amplituda [dB]", "src/Lab3/plots/Mx.png", true);
        Chart.saveChart(MYseries, "Częstotliwość [Hz]", "Amplituda [dB]", "src/Lab3/plots/My.png", true);
        Chart.saveChart(MZseries, "Częstotliwość [Hz]", "Amplituda [dB]", "src/Lab3/plots/Mz.png", true);
        Chart.saveChart(MZZseries, "Częstotliwość [Hz]", "Amplituda [dB]", "src/Lab3/plots/Mzz.png", true);
    }
}
