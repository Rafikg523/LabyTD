//Tabela 3 - 1
//Tabela 4 - 1
package Lab1;

import Chart.Chart;
import org.jfree.data.xy.XYSeries;


public class Zad2 {
    public static void main(String[] args) {
        double f = 2;
        double phi = Math.PI / 2;
        double fs = 22050;
        double Tc = 1;
        int N = (int) Math.round(Tc * fs);
        double Ts = 1.0 / fs;
        int[] H = {5, 20 , 50};
        double[][] b = new double[3][N];

        XYSeries seriesU = new XYSeries("u(t)");
        XYSeries[] seriesB = {new XYSeries("b(1)"), new XYSeries("b(2)"), new XYSeries("b(3)")};

        for (int i = 0; i < N; i++) {
            double t = i * Ts;
            double ut = 0;

            if ( 0.1 > t && t >= 0){
                ut = Math.sin(6 * Math.PI) * Math.cos(5 * Math.PI * t);
            } else if (0.4 > t && t >= 0.1) {
                ut = -1.1 * t * Math.cos(41 * Math.PI * Math.pow(t, 2));
            } else if (0.72 > t && t >= 0.4) {
                ut = t * Math.sin(20 * Math.pow(t, 4));
            } else  if (1 > t && t >= 0.72) {
                ut = 3.3 * (t - 0.72) * Math.cos(27 * t + 1.3);
            }


            seriesU.add(t, ut);
        }

        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < N; k++) {
                double t = k * Ts;
                for (int h = 1; h < H[i] + 1; h++) {
                    b[i][k] += Math.pow(-1,h) / h * Math.sin(h * Math.PI * 2 * t);
                }
                b[i][k] *= 2/Math.PI;
                seriesB[i].add(t, b[i][k]);
            }
        }



        Chart.saveChart(seriesU, "Czas [s]", "Amplituda", "ut.png");
        for (int i = 0; i < 3; i++) {
            Chart.saveChart(seriesB[i],   "Czas [s]", "Amplituda", "b(" + (i + 1) + ").png");
        }
    }
}
