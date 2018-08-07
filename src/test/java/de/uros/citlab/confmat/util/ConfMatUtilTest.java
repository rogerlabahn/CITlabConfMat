package de.uros.citlab.confmat.util;

import de.uros.citlab.confmat.CharMap;
import de.uros.citlab.confmat.ConfMat;
import de.uros.citlab.confmat.util.ConfMatUtil;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ConfMatUtilTest {

    @Rule
    public TemporaryFolder testfolder = new TemporaryFolder();

    private static double[][] getRndMat(int dim1, int dim2) {
        Random rnd = new Random(1235);
        double[][] res = new double[dim1][dim2];
        for (int i = 0; i < res.length; i++) {
            double[] vec = res[i];
            double sum = 0;
            for (int j = 0; j < vec.length; j++) {
                double v = Math.exp(-Math.pow(rnd.nextDouble(), 0.1) * 50);
                vec[j] = v;
                sum += v;
            }
            for (int j = 0; j < vec.length; j++) {
                vec[j] /= sum;
            }
        }
        return res;
    }

    @Test
    public void testIO() throws IOException {
        System.out.println("testIO");
        CharMap charMap = new CharMap();
        charMap.add("!");
        charMap.add("#");
        charMap.add(";");
        charMap.add(",");
        charMap.add("c\"d");
        charMap.add("\\");
        double[][] matIn = getRndMat(70, 7);

        for (int m = 1; m < 10; m++) {
            System.out.println("test mantisse = " + m);
            ConfMat cm = new ConfMat(charMap);
            cm.setMatrix(getRndMat(70, 7));
            File out = testfolder.newFile("confmat_" + m + ".csv");
            ConfMatUtil.toCSV(cm, out, m);
            ConfMat load = ConfMatUtil.fromCSV(out);
            double[][] matOut = load.getMatrix();
            double sum = 0;
            for (int i = 0; i < matIn.length; i++) {
                double[] vecIn = matIn[i];
                double[] vecOut = matOut[i];
                for (int j = 0; j < vecIn.length; j++) {
                    double e = vecIn[j] - vecOut[j];
                    sum += e * e;
                }
            }
            Assert.assertEquals(0, Math.sqrt(sum / matIn.length / matIn[0].length), Math.pow(0.1, m));
        }
    }

}