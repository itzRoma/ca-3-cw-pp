package com.itzroma.concurrent;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;

public class GenericThread extends Thread {

    private final int threadNumber;

    private final Resources resources;
    private final int startIndex;
    private final int endIndex;

    private final ConcurrentRunner runner;

    public GenericThread(int threadNumber, Resources resources, int startIndex, int endIndex, ConcurrentRunner runner) {
        this.threadNumber = threadNumber;
        this.resources = resources;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.runner = runner;

        setName("Thread " + this.threadNumber);
    }

    @Override
    public void run() {
        System.out.println(getName() + " started");

        try {
            if (threadNumber == 1) {
                // 1. Введення: B, MX.
                Arrays.fill(resources.B, 1);
                for (int i = 0; i < resources.MX.length; i++) {
                    Arrays.fill(resources.MX[i], 1);
                }
            } else if (threadNumber == runner.threadsNumber) {
                // 1. Введення: Z, D, C, MR.
                Arrays.fill(resources.Z, 1);
                Arrays.fill(resources.D, 1);
                Arrays.fill(resources.C, 1);
                for (int i = 0; i < resources.MR.length; i++) {
                    Arrays.fill(resources.MR[i], 1);
                }
            }

            // 2-3. Очікувати на завершення введення даних.
            runner.B1.await();

            // 4. Обчислення 1: aі = BH * CH.
            int ai = 0;
            for (int i = startIndex; i <= endIndex; i++) {
                ai += resources.B[i] * resources.C[i];
            }

            // 5. Обчислення 2: a = a + ai.
            runner.S1.acquire();
            resources.a += ai;
            runner.S1.release();

            // 6-7. Очікувати на завершення обчислення a.
            runner.B2.await();

            // 8. Обчислення 3: MAH = MX * MRH.
            for (int i = 0; i < resources.n; i++) {
                for (int j = startIndex; j <= endIndex; j++) {
                    int cell = 0;
                    for (int k = 0; k < resources.MR.length; k++) {
                        cell += resources.MX[i][k] * resources.MR[k][j];
                    }
                    resources.MA[i][j] = cell;
                }
            }

            // 9. Обчислення 4: RH = D * MAH.
            for (int i = startIndex; i <= endIndex; i++) {
                for (int j = 0; j < resources.n; j++) {
                    resources.R[i] += resources.D[j] * resources.MA[j][i];
                }
            }

            // 10. Обчислення 5: mi = min(CH).
            int mi = Arrays.stream(resources.C, startIndex, endIndex + 1)
                    .min()
                    .getAsInt();

            // 11. Обчислення 6: m = min(m, mi).
            runner.CS1.lock();
            resources.m = Math.min(resources.m, mi);
            runner.CS1.unlock();

            // 12-13. Очікувати на завершення обчислення m.
            runner.B3.await();

            // 14. Копіювання: ai = a.
            runner.S1.acquire();
            ai = resources.a;
            runner.S1.release();

            // 15. Копіювання: mi = m.
            runner.CS1.lock();
            mi = resources.m;
            runner.CS1.unlock();

            // 16. Обчислення 7: AH = ai * ZH + RH * mi.
            for (int i = startIndex; i <= endIndex; i++) {
                resources.A[i] = ai * resources.Z[i] + resources.R[i] * mi;
            }

            if (threadNumber == 1) {
                // 17. Чекати сигнал про від T2-TP завершення обчислення AH.
                runner.S2.acquire(runner.threadsNumber - 1);
            } else {
                // 17. Сигнал Т1 про завершення обчислення AH.
                runner.S2.release();
            }

            if (threadNumber == 1) {
                // 18. Виведення: A.
                System.out.printf("%n%s - Answer A%n%s%n%n", getName(), Arrays.toString(resources.A));
            }

            System.out.println(getName() + " finished");
        } catch (InterruptedException e) {
            System.out.printf("Exception in %s: %s%n", getName(), e.getMessage());
            interrupt();
        } catch (BrokenBarrierException e) {
            System.out.printf("Barrier exception: %s%n", e.getMessage());
        }
    }
}
