//Table 1 - 1
//Table 2 - 1

package Lab1;

import Chart.Chart;
import org.jfree.data.xy.XYSeries;

public class Zad1 {
    public static void main(String[] args) {
        double f = 2;
        double phi = Math.PI / 2;
        double fs = 8000;
        double Tc = 2;
        int N = (int) Math.round(Tc * fs);
        double Ts = 1.0 / fs;

        XYSeries seriesX = new XYSeries("x(t)");
        XYSeries seriesY = new XYSeries("y(t)");
        XYSeries seriesZ = new XYSeries("z(t)");
        XYSeries seriesV = new XYSeries("v(t)");

        for (int i = 0; i < N; i++) {
            double t = i * Ts;
            double xt = Math.cos(2 * Math.PI * f * t + phi) * Math.cos(2.5 * Math.pow(t, 0.2) * Math.PI);
            double yt = (xt * t) / (3 + Math.cos(20 * Math.PI * t));
            double zt = Math.pow(t, 2) * Math.abs(xt * yt - 2 / (10 + yt));
            double vt = Math.pow(zt, 3) + 3 * Math.sin(zt * yt * Math.abs(yt - xt));

            seriesX.add(t, xt);
            seriesY.add(t, yt);
            seriesZ.add(t, zt);
            seriesV.add(t, vt);
        }

        Chart.saveChart(seriesY, "Czas [s]", "Amplituda",  "yt.png");
        Chart.saveChart(seriesX, "Czas [s]", "Amplituda",  "xt.png");
        Chart.saveChart(seriesZ,  "Czas [s]", "Amplituda",  "zt.png");
        Chart.saveChart(seriesV, "Czas [s]", "Amplituda",  "vt.png");
    }


}
