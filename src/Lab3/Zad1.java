//Rafał Grzelak
//Mx liczba prążków: 5
//My liczba prążków: 4
//Mz liczba prążków: 4
package Lab3;

import Chart.Chart;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.jfree.data.xy.XYSeries;


public class Zad1 {
    public static void main(String[] args) {
        int H = 5;
        int f = 2;
        int fs = 64;
        int T = 2;
        double Ts = 1.0 / fs;
        int N = T * fs;

        double[] x = new double[N];
        double[] y = new double[N];
        double[] z = new double[N];

        XYSeries Xseries = new XYSeries("X(t)");
        XYSeries Yseries = new XYSeries("Y(t)");
        XYSeries Zseries = new XYSeries("Z(t)");

        XYSeries MXseries = new XYSeries("Mx(t)");
        XYSeries MYseries = new XYSeries("My(t)");
        XYSeries MZseries = new XYSeries("Mz(t)");

        for (int i = 0; i < N; i++) {
            double t = i * Ts;
            double xt = 0;
            double yt = 0;
            double zt = 0;

            for (int k = 1; k <= H; k++){
                xt += Math.pow(-1, k + 1) * Math.sin(2 * Math.PI * k * f * t) / k;
                yt += Math.pow(-1, k - 1) * Math.sin(2 * Math.PI * (2 * k - 1) * f * t) / Math.pow(2*k - 1, 2);
                zt += Math.sin(2 * Math.PI * (2 * k - 1) * f * t) / (2 * k - 1);
            }

            xt *= 2.0 / Math.PI;
            yt *= 8.0 / Math.pow(Math.PI, 2);
            zt *= 4.0 / Math.PI;

            x[i] = xt;
            y[i] = yt;
            z[i] = zt;

            Xseries.add(t, xt);
            Yseries.add(t, yt);
            Zseries.add(t, zt);
        }

        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);

        Complex[] xc = fft.transform(x, TransformType.FORWARD);
        Complex[] yc = fft.transform(y, TransformType.FORWARD);
        Complex[] zc = fft.transform(z, TransformType.FORWARD);


        for (int i = 0; i <= fs / 2.0; i++) {
            double fr = i * ((double) fs  / N );
            double frlog = Math.log10(fr);
            double xci = Math.sqrt(Math.pow(xc[i].getReal(), 2) + Math.pow(xc[i].getImaginary(), 2)) / N;
            double yci = Math.sqrt(Math.pow(yc[i].getReal(), 2) + Math.pow(yc[i].getImaginary(), 2)) / N;
            double zci = Math.sqrt(Math.pow(zc[i].getReal(), 2) + Math.pow(zc[i].getImaginary(), 2)) / N;
            MXseries.add(fr, 10.0 * Math.log10(xci));
            MYseries.add(fr, 10.0 * Math.log10(yci));
            MZseries.add(fr, 10.0 * Math.log10(zci));
        }

        Chart.saveChart(Xseries,  "Czas [s]", "Amplituda", "src/Lab3/plots/Xt.png");
        Chart.saveChart(Yseries,  "Czas [s]", "Amplituda", "src/Lab3/plots/Yt.png");
        Chart.saveChart(Zseries,  "Czas [s]", "Amplituda", "src/Lab3/plots/Zt.png");

        Chart.saveChart(MXseries,  "Częstotliwość [Hz]", "Amplituda [dB]", "src/Lab3/plots/Mx.png", true);
        Chart.saveChart(MYseries,  "Częstotliwość [Hz]", "Amplituda [dB]", "src/Lab3/plots/My.png", true);
        Chart.saveChart(MZseries,  "Częstotliwość [Hz]", "Amplituda [dB]", "src/Lab3/plots/Mz.png", true);
    }
}
