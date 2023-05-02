package com.itzroma.taras;

public class Data {

    private int size;

    private int m;
    private int n;

    private int[] A;
    private int[] D;
    private int[] C;
    private int[] Z;

    private int[][] MX;
    private int[][] MR;
    private int[][] MA;

    public Data(int size) {
        this.size = size;

        m = Integer.MIN_VALUE;
        n = Integer.MAX_VALUE;

        A = new int[size];
        D = new int[size];
        C = new int[size];
        Z = new int[size];

        MX = new int[size][size];
        MR = new int[size][size];
        MA = new int[size][size];
    }

    public int getSize() {
        return size;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int[] getA() {
        return A;
    }

    public int[] getD() {
        return D;
    }

    public int[] getC() {
        return C;
    }

    public int[] getZ() {
        return Z;
    }

    public int[][] getMX() {
        return MX;
    }

    public int[][] getMR() {
        return MR;
    }

    public int[][] getMA() {
        return MA;
    }
}
