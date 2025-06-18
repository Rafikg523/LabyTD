package Lab13;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Range;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.util.Random;

public class Zad1  {

    public static void main(String[] args) {
        int N  = 44;
        Random random = new Random();
        int[] rng = new int[N];
        for (int i = 0; i < N; i++) {
            rng[i] = random.nextInt(2);
        }


        int[] data = new int[]{1,1,0,1,1,0,0,1,1,0,0};

        int[] raw = rng;
        int fs = 500;
        double Tb = 1.0;
        double W = 10.0;
        Modulator[] modulators = new Modulator[3];
        modulators[0] = new ASK(Tb, W, 1.0, 0.5, fs);
        modulators[1] = new PSK(Tb, W, fs);

        modulators[2] = new FSK(Tb, W, fs);
        Coder[] coders = new Coder[2];
        coders[0] = new Hamming1(7, 4);

        coders[1] = new Hamming2(15, 11);

        Noise noise = new Noise();
        String[] noiseTypes = {"I+II", "II+I"};
        double[] alpha = new double[10];
        int alphaSize = 10;
        for (int i = 0; i < alphaSize; i++) {
            alpha[i] = i * 3;

        }
        double[] betha = new double[10];
        int bethaSize = 10;
        for (int i = 0; i < bethaSize; i++) {
            betha[i] = i * 3;

        }
        for (Modulator modulator : modulators) {
            for (Coder coder : coders) {
                for (int n = 0; n < noiseTypes.length; n++) {
                    String noiseType = noiseTypes[n];
                    String title = "Modulator: " + modulator.getName() + ", Coder: " + coder.getName() + ", Noise: " + noiseType;
                    String plotTitle = modulator.getName() + "_" + coder.getName() + "_" + noiseType;

                    System.out.println(title);
                    double[][] berMatrix = new double[10][10];
                    for (int a = 0; a < 10; a++) {
                        for (int b = 0; b < 10; b++) {
                            int[] encodedSignal = coder.encode(raw);
                            double[] modulatedSignal = modulator.modulate(encodedSignal);

                            double[] noisySignal;
                            if (n == 0) {
                                noisySignal = noise.addWhiteNoise(modulatedSignal, alpha[a]);
                                noisySignal = noise.addWeirdNoise(noisySignal, alpha[b], fs, plotTitle);
                            } else {
                                noisySignal = noise.addWeirdNoise(modulatedSignal, alpha[b], fs, plotTitle);
                                noisySignal = noise.addWhiteNoise(noisySignal, alpha[a]);
                            }

                            int[] demodulatedSignal = modulator.demodulate(noisySignal);
                            int[] decodedSignal = coder.decode(demodulatedSignal);
                            int bitErrors = 0;
                            for (int i = 0; i < raw.length; i++) {
                                if (raw[i] != decodedSignal[i]) {
                                    bitErrors++;
                                }
                            }
                            double BER = (double) bitErrors / raw.length * 100.0;
                            berMatrix[a][b] = BER;
                        }
                    }
                    create3DPlot(berMatrix, plotTitle, alpha, betha);
                }
            }
        }
    }

    public static void create3DPlot(double[][] berMatrix, String plotTitle, double[] alpha, double[] betha) {
        int sizeA = berMatrix.length;
        int sizeB = berMatrix[0].length;
        // Zakładamy, że alpha i betha są posortowane rosnąco i równomiernie
        double alphaStep = (alpha[sizeA-1] - alpha[0]) / (sizeA - 1);
        double bethaStep = (betha[sizeB-1] - betha[0]) / (sizeB - 1);
        Mapper mapper = new Mapper() {
            @Override
            public double f(double a, double b) {
                int ia = (int) Math.round((a - alpha[0]) / alphaStep);
                int ib = (int) Math.round((b - betha[0]) / bethaStep);
                if (ia >= 0 && ia < sizeA && ib >= 0 && ib < sizeB) {
                    return berMatrix[ia][ib];
                } else {
                    return 0;
                }
            }
        };
        Range rangeA = new Range((float)alpha[0], (float)alpha[sizeA-1]);
        Range rangeB = new Range((float)betha[0], (float)betha[sizeB-1]);
        int steps = sizeA;
        Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(rangeA, steps, rangeB, steps), mapper);
        surface.setColorMapper(new org.jzy3d.colors.ColorMapper(
            new org.jzy3d.colors.colormaps.ColorMapRainbow(),
            surface.getBounds().getZmin(),
            surface.getBounds().getZmax(),
            new Color(1, 1, 1, 1f)
        ));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(true);
        surface.setWireframeColor(Color.BLACK);
        Chart chart = AWTChartComponentFactory.chart(Quality.Advanced, "offscreen");
        chart.getScene().getGraph().add(surface);
        chart.getAxeLayout().setXAxeLabel("alpha");
        chart.getAxeLayout().setYAxeLabel("betha");
        chart.getAxeLayout().setZAxeLabel("BER (%)");
        chart.getView().setViewPoint(new Coord3d(Math.PI / 4 * -3, Math.PI/4, 0));
        try {
            String filePath = "src/Lab13/plots/" + plotTitle + ".png";
            chart.screenshot(new java.io.File(filePath));
            System.out.println("Wykres zapisany: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}