package de.uros.citlab.confmat;

public class ConfMat {
    private CharMap charMap;
    private double[][] matrix;

    public ConfMat(CharMap charMap) {
        this.charMap = charMap;
    }

    public ConfMat(CharMap charMap, double[][] matrix) {
        this(charMap);
        this.matrix = matrix;
    }

    public CharMap getCharMap() {
        return charMap;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public double[] getVector(int pos) {
        return matrix[pos];
    }
}
