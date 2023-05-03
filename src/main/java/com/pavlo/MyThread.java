package com.pavlo;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;

public class MyThread extends Thread {

    private int currThreadNum;
    private int maxThreadNum;
    private SharedResources sharedResources;
    private Synchronizer synchronizer;
    private int beginWith;
    private int endWith;

    public MyThread(
            int currThreadNum,
            int maxThreadNum,
            SharedResources sharedResources,
            Synchronizer synchronizer,
            int beginWith,
            int endWith
    ) {
        setName("T" + currThreadNum);

        this.currThreadNum = currThreadNum;
        this.maxThreadNum = maxThreadNum;
        this.sharedResources = sharedResources;
        this.synchronizer = synchronizer;
        this.beginWith = beginWith;
        this.endWith = endWith;
    }

    @Override
    public void run() {
        System.out.println(getName() + " started");

        try {
            if (currThreadNum == 1) {
                // 1. Якщо i = 1: Введення: B, MX.
                Arrays.fill(sharedResources.B, 1);
                for (int i = 0; i < sharedResources.N; i++) Arrays.fill(sharedResources.MX[i], 1);
            } else if (currThreadNum == maxThreadNum) {
                // 1. Якщо i = P: Введення: Z, C, MR.
                Arrays.fill(sharedResources.Z, 1);
                Arrays.fill(sharedResources.C, 1);
                for (int i = 0; i < sharedResources.N; i++) Arrays.fill(sharedResources.MR[i], 1);
            }

            // 2-3. Очікувати на завершення введення даних.
            synchronizer.B1.await();

            // 4. Обчислення 1: MAH = MX * MRH.
            for (int i = 0; i < sharedResources.N; i++) {
                for (int j = beginWith; j <= endWith; j++) {
                    int cell = 0;
                    for (int k = 0; k < sharedResources.MR.length; k++) {
                        cell += sharedResources.MX[i][k] * sharedResources.MR[k][j];
                    }
                    sharedResources.MA[i][j] = cell;
                }
            }

            // 5. Обчислення 2: ai = BH * CH.
            int ai = 0;
            for (int i = beginWith; i <= endWith; i++) {
                ai += sharedResources.B[i] * sharedResources.C[i];
            }

            // 6. Обчислення 3: a = a + ai.
            synchronizer.S1.acquire();
            sharedResources.a += ai;
            synchronizer.S1.release();

            // 7. Обчислення 4: mi = min(BH).
            int mi = sharedResources.B[beginWith];
            for (int i = beginWith + 1; i <= endWith; i++) {
                mi = Math.min(sharedResources.m, mi);
            }

            // 8. Обчислення 5: m = min(m, mi).
            synchronizer.CS1.lock();
            sharedResources.m = Math.min(sharedResources.m, mi);
            synchronizer.CS1.unlock();

            // 9-10. Очікувати на завершення обчислення a та m.
            synchronizer.B2.await();

            // 11. Копіювання: ai = a.
            synchronizer.S1.acquire();
            ai = sharedResources.a;
            synchronizer.S1.release();

            // 12. Копіювання: mi = m.
            synchronizer.CS1.lock();
            mi = sharedResources.m;
            synchronizer.CS1.unlock();

            // 13. Обчислення 6: AH = ai * ZH - B * MAH * mi.
            for (int i = beginWith; i <= endWith; i++) {
                sharedResources.A[i] += ai * sharedResources.Z[i];
                for (int j = 0; j < sharedResources.N; j++) {
                    sharedResources.A[i] -= sharedResources.B[j] * sharedResources.MA[j][i] * mi;
                }
            }

            if (currThreadNum == 1) {
                // 14. Якщо i = 1: Чекати сигнал про закінчення обчислення AH в T2…TP.
                synchronizer.S2.acquire(maxThreadNum - 1);
            } else if (currThreadNum == maxThreadNum) {
                // 14. Якщо i = P: Сигнал Т1 про закінчення обчислення AH.
                synchronizer.S2.release();
            } else {
                // 14. Інакше: Сигнал Т1 про закінчення обчислення AH.
                synchronizer.S2.release();
            }

            if (currThreadNum == 1) {
                // 15. Якщо i = 1: Виведення: A.
                System.out.printf("%n%s: A%n%s%n%n", getName(), Arrays.toString(sharedResources.A));
            }
        } catch (InterruptedException e) {
            interrupt();
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        System.out.println(getName() + " finished");
    }
}
