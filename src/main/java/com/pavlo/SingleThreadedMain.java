package com.pavlo;

import java.util.Arrays;

public class SingleThreadedMain {
    public static void main(String[] args) {
        Thread thread = new SingleThreadedMain.SingleThread(new SharedResources(8));

        long startTime = System.currentTimeMillis();

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            thread.interrupt();
            throw new RuntimeException(e);
        }

        long endTime = System.currentTimeMillis();

        System.out.printf("%nCalculation completed in %d ms%n", endTime - startTime);
    }

    private static class SingleThread extends Thread {

        private final SharedResources sharedResources;

        public SingleThread(SharedResources sharedResources) {
            this.sharedResources = sharedResources;
            setName("T1");
        }

        @Override
        public void run() {
            System.out.println(getName() + " started");

            // 1. Введення: B, MX, Z, C, MR.
            Arrays.fill(sharedResources.B, 1);
            Arrays.fill(sharedResources.Z, 1);
            Arrays.fill(sharedResources.C, 1);
            for (int i = 0; i < sharedResources.N; i++) {
                Arrays.fill(sharedResources.MX[i], 1);
                Arrays.fill(sharedResources.MR[i], 1);
            }

            // 4. Обчислення 1: MAH = MX * MRH.
            for (int i = 0; i < sharedResources.N; i++) {
                for (int j = 0; j < sharedResources.N; j++) {
                    int cell = 0;
                    for (int k = 0; k < sharedResources.MR.length; k++) {
                        cell += sharedResources.MX[i][k] * sharedResources.MR[k][j];
                    }
                    sharedResources.MA[i][j] = cell;
                }
            }

            // 5. Обчислення 2: ai = BH * CH.
            int ai = 0;
            for (int i = 0; i < sharedResources.N; i++) {
                ai += sharedResources.B[i] * sharedResources.C[i];
            }

            // 6. Обчислення 3: a = a + ai.
            sharedResources.a += ai;

            // 7. Обчислення 4: mi = min(BH).
            int mi = sharedResources.B[0];
            for (int i = 1; i < sharedResources.N; i++) {
                mi = Math.min(sharedResources.m, mi);
            }

            // 8. Обчислення 5: m = min(m, mi).
            sharedResources.m = Math.min(sharedResources.m, mi);

            // 11. Копіювання: ai = a.
            ai = sharedResources.a;

            // 12. Копіювання: mi = m.
            mi = sharedResources.m;

            // 13. Обчислення 6: AH = ai * ZH - B * MAH * mi.
            for (int i = 0; i < sharedResources.N; i++) {
                sharedResources.A[i] += ai * sharedResources.Z[i];
                for (int j = 0; j < sharedResources.N; j++) {
                    sharedResources.A[i] -= sharedResources.B[j] * sharedResources.MA[j][i] * mi;
                }
            }

            // 15. Якщо i = 1: Виведення: A.
            System.out.printf("%n%s: A%n%s%n%n", getName(), Arrays.toString(sharedResources.A));

            System.out.println(getName() + " finished");
        }
    }
}
