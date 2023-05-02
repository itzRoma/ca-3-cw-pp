package com.itzroma.dana;

public class Data {

    private int N;

    private int m;
    private int d;

    private int[] C;
    private int[] B;

    private int[][] MA;
    private int[][] MX;
    private int[][] MR;
    private int[][] MZ;

    public Data(int N) {
        this.N = N;

        m = Integer.MAX_VALUE;
        d = 0;

        C = new int[N];
        B = new int[N];

        MA = new int[N][N];
        MX = new int[N][N];
        MR = new int[N][N];
        MZ = new int[N][N];
    }

    public int getN() {
        return N;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public int[] getC() {
        return C;
    }

    public int[] getB() {
        return B;
    }

    public int[][] getMA() {
        return MA;
    }

    public int[][] getMX() {
        return MX;
    }

    public int[][] getMR() {
        return MR;
    }

    public int[][] getMZ() {
        return MZ;
    }
}
