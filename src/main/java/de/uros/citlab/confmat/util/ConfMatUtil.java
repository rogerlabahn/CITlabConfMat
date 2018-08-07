package de.uros.citlab.confmat.util;

import de.uros.citlab.confmat.CharMap;
import de.uros.citlab.confmat.ConfMat;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ConfMatUtil {

    private static final CSVFormat csvFormat = CSVFormat.DEFAULT;
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final String PATTERN_1 = "0.#E0";
    private static final String PATTERN_2 = "0.##E0";
    private static final String PATTERN_3 = "0.###E0";
    private static final String PATTERN_4 = "0.####E0";
    private static final String PATTERN_5 = "0.#####E0";
    private static final String PATTERN_6 = "0.######E0";
    private static final String PATTERN_7 = "0.#######E0";
    private static final String PATTERN_8 = "0.########E0";
    private static String PATTERN = PATTERN_5;
    private static int len = 5;

    public static void setPrecision(int mantisse) {
        if (len != mantisse) {
            PATTERN = getPrecision(mantisse);
            len = mantisse;
        }
    }

    private static String getPrecision(int mantisse) {
        switch (mantisse) {
            case 1:
                return PATTERN_1;
            case 2:
                return PATTERN_2;
            case 3:
                return PATTERN_3;
            case 4:
                return PATTERN_4;
            case 5:
                return PATTERN_5;
            case 6:
                return PATTERN_6;
            case 7:
                return PATTERN_7;
            case 8:
                return PATTERN_8;
        }
        if (len == mantisse) {
            return PATTERN;
        }
        PATTERN = "0.";
        for (int i = 0; i < mantisse; i++) {
            PATTERN += "#";
        }
        return PATTERN + "E0";

    }

    public static void toCSV(ConfMat cm, File file) throws IOException {
        toCSV(cm, file, len);
    }

    public static void toCSV(ConfMat cm, File file, int mantisse) throws IOException {
        CSVPrinter printer = csvFormat.withCommentMarker('!').print(new OutputStreamWriter(new FileOutputStream(file), CHARSET));
        printer.printRecord(toRecord(cm.getCharMap()));
        for (Object[] vec : toRecord(cm.getMatrix(), mantisse)) {
            printer.printRecord(vec);
        }
        printer.flush();
        printer.close();
    }

    private static Object[] toRecord(CharMap cm) {
        HashMap<Character, Integer> map = cm.getMap();
        LinkedList<String> out = new LinkedList<String>();
        for (int i = 0; i < cm.size(); i++) {
            out.add("");
        }
        for (Character character : map.keySet()) {
            out.set(map.get(character), out.get(map.get(character)) + character);
        }
        out.removeFirst();
        out.sort((o1, o2) -> Integer.compare(map.get(o1.charAt(0)), map.get(o2.charAt(0))));
        out.addFirst("");
        return out.toArray(new Object[0]);
    }

    private static CharMap fromRecord(CSVRecord record) {
        Iterator<String> iterator = record.iterator();
        CharMap cm = new CharMap();
        for (Iterator<String> it = iterator; it.hasNext(); ) {
            String id = it.next();
            if (!id.isEmpty()) {
                cm.add(id);
            }
        }
        return cm;
    }

    private static Object[][] toRecord(double[][] matrix, int mantisse) {
        NumberFormat formatter = new DecimalFormat(getPrecision(mantisse));
        Object[][] out = new Object[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            Object[] outVec = out[i];
            double[] valVec = matrix[i];
            for (int j = 0; j < valVec.length; j++) {
                outVec[j] = formatter.format(valVec[j]);
            }
        }
        return out;
    }

    private static double[][] fromRecord(List<CSVRecord> records) {
        double[][] mat = new double[records.size()][];
        for (int i = 0; i < records.size(); i++) {
            CSVRecord strings = records.get(i);
            double[] vec = new double[strings.size()];
            for (int j = 0; j < strings.size(); j++) {
                vec[j] = Double.parseDouble(strings.get(j));
            }
            mat[i] = vec;
        }
        return mat;
    }

    public static ConfMat fromCSV(File file) throws IOException {
        CSVParser parser = csvFormat.withCommentMarker('!').parse(new InputStreamReader(new FileInputStream(file), CHARSET));
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        CSVRecord charString = records.get(0);
        CharMap cm = fromRecord(charString);
        records.remove(0);
        double[][] mat = fromRecord(records);
        return new ConfMat(cm, mat);
    }

}
