package com.pavlo;

public class SharedResources {

    public int N;

    public int[] A;
    public int[] B;
    public int[] C;
    public int[] Z;

    public int[][] MX;
    public int[][] MR;

    public int[][] MA;
    public int a;
    public int m;

    public SharedResources(int N) {
        this.N = N;

        A = new int[N];
        B = new int[N];
        C = new int[N];
        Z = new int[N];

        MX = new int[N][N];
        MR = new int[N][N];

        MA = new int[N][N];
        a = 0;
        m = Integer.MAX_VALUE;
    }
}
