//Rafał Grzelak
package Lab4;

import Chart.Chart;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.jfree.data.xy.XYSeries;

public class Zad1 {
    public static void main(String[] args) {
        int fs = 1024;
        int T = 1;
        int N = fs * T;
        double Ts = 1.0 / fs;

        double fm = 5;
        double fn = 80;

        for (int o = 0; o < 3; o++) {
            double[] kA = {0.5, 10, 40};
            double[] kP = {-20, 5.0 * Math.PI, -10.0 * Math.PI};
            double[] kF = {-20, 5.0 * Math.PI, -10.0 * Math.PI};
            char[] c = {'a', 'b', 'c'};

            double[] m = new double[N];
            double[] zA = new double[N];
            double[] zP = new double[N];
            double[] zF = new double[N];

            XYSeries mSeries = new XYSeries("m(t)");
            XYSeries zASeries = new XYSeries("zA(t)");
            XYSeries zPSeries = new XYSeries("zP(t)");
            XYSeries zFSeries = new XYSeries("zF(t)");

            for (int i = 0; i < N; i++) {
                double t = i * Ts;
                m[i] = Math.sin(2 * Math.PI * fm * t);

                zA[i] = (kA[o] * m[i] + 1) * Math.cos(2 * Math.PI * fn * t);
                zP[i] = Math.cos(2 * Math.PI * fn * t + kP[o] * m[i]);
                zF[i] = Math.cos(2 * Math.PI * fn * t + (kF[o] / fm) * m[i]);

                mSeries.add(t, m[i]);
                zASeries.add(t, zA[i]);
                zPSeries.add(t, zP[i]);
                zFSeries.add(t, zF[i]);
            }


            //Chart.saveChart(mSeries, "Czas [s]", "Amplituda", "src/Lab4/plots/mt.png");
            Chart.saveChart(zASeries, mSeries, "Czas [s]", "Amplituda", "src/Lab4/plots/" + c[o] +"-zAt.png");
            Chart.saveChart(zPSeries, mSeries, "Czas [s]", "Amplituda", "src/Lab4/plots/" + c[o] +"-zPt.png");
            Chart.saveChart(zFSeries, mSeries, "Czas [s]", "Amplituda", "src/Lab4/plots/" + c[o] +"-zFt.png");

            XYSeries zAMSeries = new XYSeries("zAM(t)");
            XYSeries zPMSeries = new XYSeries("zPAM(t)");
            XYSeries zFMSeries = new XYSeries("zFAM(t)");

            FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);

            Complex[] zAc = fft.transform(zA, TransformType.FORWARD);
            Complex[] zPc = fft.transform(zP, TransformType.FORWARD);
            Complex[] zFc = fft.transform(zF, TransformType.FORWARD);

            for (int i = 0; i <= fs / 2.0; i++) {
                double fr = i * ((double) fs  / N );
                double frlog = fr == 0 ? 0 : Math.log(fr);
                double zAci = Math.sqrt(Math.pow(zAc[i].getReal(), 2) + Math.pow(zAc[i].getImaginary(), 2));
                double zPci = Math.sqrt(Math.pow(zPc[i].getReal(), 2) + Math.pow(zPc[i].getImaginary(), 2));
                double zFci = Math.sqrt(Math.pow(zFc[i].getReal(), 2) + Math.pow(zFc[i].getImaginary(), 2));

                double zAcidB = 10 * Math.log10(zAci);
                double zPcidB = 10 * Math.log10(zPci);
                double zFcidB = 10 * Math.log10(zFci);

                double prog = -100.0;

                zAMSeries.add(fr, zAcidB > prog ? zAcidB : prog);
                zPMSeries.add(fr, zPcidB > prog ? zPcidB : prog);
                zFMSeries.add(fr, zFcidB > prog ? zFcidB : prog);
            }

            Chart.saveChart(zAMSeries, "Czas [s]", "Amplituda [dB]", "src/Lab4/plots/" + c[o] +"-MzAt.png" ,true);
            Chart.saveChart(zPMSeries, "Czas [s]", "Amplituda [dB]", "src/Lab4/plots/" + c[o] +"-MzPt.png",  true);
            Chart.saveChart(zFMSeries, "Czas [s]", "Amplituda [dB]", "src/Lab4/plots/" + c[o] +"-MzFt.png", true);
        }

    }
}