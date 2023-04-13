package com.itzroma.concurrent;

public class Resources {

    protected final int n; // size of vectors and matrices

    protected int a; // shared
    protected int m; // shared

    protected final int[] A;
    protected final int[] B;
    protected final int[] C;
    protected final int[] Z;
    protected final int[] D; // shared
    protected final int[] R;

    protected final int[][] MX; // shared
    protected final int[][] MR;
    protected final int[][] MA;

    public Resources(int n) {
        this.n = n;

        a = 0;
        m = Integer.MAX_VALUE;

        A = new int[this.n];
        B = new int[this.n];
        C = new int[this.n];
        Z = new int[this.n];
        D = new int[this.n];
        R = new int[this.n];

        MX = new int[this.n][this.n];
        MR = new int[this.n][this.n];
        MA = new int[this.n][this.n];
    }
}
