package com.itzroma;

import com.itzroma.concurrent.Resources;

import java.util.Arrays;

public class SingleThreadMain {
    public static void main(String[] args) {
        Thread thread = new SingleThread(new Resources(2700));

        long start = System.currentTimeMillis();

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            System.out.println("Exception: " + e.getMessage());
            thread.interrupt();
        }

        System.out.println("Time taken: " + (System.currentTimeMillis() - start) + " ms");
    }

    private static class SingleThread extends Thread {

        private Resources resources;

        public SingleThread(Resources resources) {
            this.resources = resources;
            setName("T1");
        }

        @Override
        public void run() {
            System.out.println(getName() + " started");

            // 1. Введення даних
            Arrays.fill(resources.B, 1);
            Arrays.fill(resources.Z, 1);
            Arrays.fill(resources.D, 1);
            Arrays.fill(resources.C, 1);
            for (int i = 0; i < resources.MX.length; i++) {
                Arrays.fill(resources.MX[i], 1);
                Arrays.fill(resources.MR[i], 1);
            }

            // 4. Обчислення 1: aі = BH * CH.
            int ai = 0;
            for (int i = 0; i < resources.n; i++) {
                ai += resources.B[i] * resources.C[i];
            }

            // 5. Обчислення 2: a = a + ai.
            resources.a += ai;

            // 8. Обчислення 3: MAH = MX * MRH.
            for (int i = 0; i < resources.n; i++) {
                for (int j = 0; j < resources.n; j++) {
                    int cell = 0;
                    for (int k = 0; k < resources.MR.length; k++) {
                        cell += resources.MX[i][k] * resources.MR[k][j];
                    }
                    resources.MA[i][j] = cell;
                }
            }

            // 9. Обчислення 4: RH = D * MAH.
            for (int i = 0; i < resources.n; i++) {
                for (int j = 0; j < resources.n; j++) {
                    resources.R[i] += resources.D[j] * resources.MA[j][i];
                }
            }

            // 10. Обчислення 5: mi = min(CH).
            int mi = Arrays.stream(resources.C, 0, resources.n)
                    .min()
                    .getAsInt();

            // 11. Обчислення 6: m = min(m, mi).
            resources.m = Math.min(resources.m, mi);

            // 14. Копіювання: ai = a.
            ai = resources.a;

            // 15. Копіювання: mi = m.
            mi = resources.m;

            // 16. Обчислення 7: AH = ai * ZH + RH * mi.
            for (int i = 0; i < resources.n; i++) {
                resources.A[i] = ai * resources.Z[i] + resources.R[i] * mi;
            }

            // 18. Виведення: A.
            System.out.printf("%n%s - Answer A%n%s%n%n", getName(), Arrays.toString(resources.A));

            System.out.println(getName() + " finished");
        }
    }
}
