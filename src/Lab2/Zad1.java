package Lab2;

import Chart.Chart;
import org.apache.commons.math3.complex.Complex;
import org.jfree.data.xy.XYSeries;

import java.util.ArrayList;

public class Zad1 {
    public static void main(String[] args) {
        int fs = 16000;
        int f = 2000;
        double Tc = 0.012;
        int N = (int) (fs * Tc);
        double Ts = 1.0 / fs;
        double A = 1.0;
        ArrayList<Double> xt = new ArrayList<>();
        ArrayList<Complex> X = new ArrayList<>();
        XYSeries seriesXT = new XYSeries("x(t)");

        for (int n = 0; n < N; n++) {
            double t = n * Ts;
            //double x = Math.sin(2 * Math.PI * 1000 * t) + 0.5 * Math.sin(2 * Math.PI * 500 * t);
            double x = A * Math.sin(2 * Math.PI * f * t);
            xt.add(x);
            seriesXT.add(t, x);
        }

        for (int k = 0; k < N; k++) {
            Complex sum = Complex.ZERO;
            for (int n = 0; n < N; n++) {
                double p = -2.0 * Math.PI * k * n / N;
                Complex w = new Complex(Math.cos(p), Math.sin(p));
                sum = sum.add(new Complex(xt.get(n), 0).multiply(w));
            }
            X.add(sum);
        }

        XYSeries seriesM = new XYSeries("M(k)");
        XYSeries seriesMlog = new XYSeries("Mlog(k)");
        for (int k = 0; k < N / 2; k++) {
            double fr = (double) k * fs / N;
            double m = Math.sqrt(Math.pow(X.get(k).getReal(), 2) + Math.pow(X.get(k).getImaginary(), 2));
            double mdb = 10.0 * Math.log10(m);
            double frlog = Math.log10(fr);
            seriesM.add(fr, m);
            seriesMlog.add(fr, mdb);
        }

        Chart.saveChart(seriesXT, "Czas [s]", "Amplituda", "src/Lab2/plots/xt.png");
        Chart.saveChart(seriesM,  "Częstotliwość [Hz]", "Amplituda", "src/Lab2/plots/Mk.png");
        Chart.saveChart(seriesMlog,  "Częstotliwość dB [Hz]", "Amplituda", "src/Lab2/plots/Mkdb.png");

    }
}
