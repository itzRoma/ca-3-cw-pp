package com.itzroma.concurrent;

public class Resources {

    public final int n; // size of vectors and matrices

    public int a; // shared
    public int m; // shared

    public final int[] A;
    public final int[] B;
    public final int[] C;
    public final int[] Z;
    public final int[] D; // shared
    public final int[] R;

    public final int[][] MX; // shared
    public final int[][] MR;
    public final int[][] MA;

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
