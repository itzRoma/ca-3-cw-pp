package com.itzroma.dana;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;

public class MyThread extends Thread {

    private int threadNo;

    private Data data;
    private int start;
    private int end;

    private MultithreadedStarter starter;

    public MyThread(int threadNo, Data data, int start, int end, MultithreadedStarter starter) {
        setName("Thread-" + threadNo);

        this.threadNo = threadNo;
        this.data = data;
        this.start = start;
        this.end = end;
        this.starter = starter;
    }

    @Override
    public void run() {
        System.out.println(getName() + " started");

        try {
            if (threadNo == 1) {
                // 1. Якщо i = 1: Введення: C.
                Arrays.fill(data.getC(), 1);
            } else if (threadNo == starter.getThreadsCount()) {
                // 1. Якщо i = P: Введення: MX, MZ, MR, B.
                for (int i = 0; i < data.getN(); i++) {
                    Arrays.fill(data.getMX()[i], 1);
                    Arrays.fill(data.getMZ()[i], 1);
                    Arrays.fill(data.getMR()[i], 1);
                    data.getB()[i] = 1;
                }
            }

            // 2,3. Очікувати на закінчення введення даних.
            starter.getB1().await();

            // 4. Обчислення 1: mi = min(CH).
            int mi = data.getC()[start];
            for (int i = start + 1; i <= end; i++) {
                mi = Math.min(data.getC()[i], mi);
            }

            // 5. Обчислення 2: m = min(m, mi).
            starter.getCS1().lock();
            data.setM(Math.min(data.getM(), mi));
            starter.getCS1().unlock();

            // 6,7. Очікувати на закінчення обчислення m.
            starter.getB2().await();

            // 8. Обчислення 3: dі = BH * CH.
            int di = 0;
            for (int i = start; i <= end; i++) {
                di += data.getB()[i] * data.getC()[i];
            }

            // 9. Обчислення 4: d = d + di.
            starter.getS1().acquire();
            data.setD(data.getD() + di);
            starter.getS1().release();

            // 10,11. Очікувати на закінчення обчислення d.
            starter.getB3().await();

            // 12. Копіювання: mi = m.
            starter.getCS1().lock();
            mi = data.getM();
            starter.getCS1().unlock();

            // 13. Копіювання: di = d.
            starter.getS1().acquire();
            di = data.getD();
            starter.getS1().release();

            // 14. Обчислення 5: MAH = MX * MRH * mi + di * MZH.
            for (int i = 0; i < data.getN(); i++) {
                for (int j = start; j <= end; j++) {
                    int product = 0;
                    for (int k = 0; k < data.getMR().length; k++) {
                        product += data.getMX()[i][k] * data.getMR()[k][j];
                    }
                    data.getMA()[i][j] = product * mi + di * data.getMZ()[i][j];
                }
            }

            if (threadNo == 1) {
                // 15. Якщо i = 1: Чекати сигнал від T2…TP про закінчення обчислення MAH.
                starter.getS2().acquire(starter.getThreadsCount() - 1);
            } else {
                // 15. Якщо i ≠ 1: Сигнал Т1 про закінчення обчислення MAH.
                starter.getS2().release();
            }

            if (threadNo == 1) {
                // 16. Якщо i = 1: Виведення: MA.
                StringBuilder MA = new StringBuilder();
                for (int i = 0; i < data.getMA().length; i++) {
                    MA.append(Arrays.toString(data.getMA()[i]));
                    MA.append(System.lineSeparator());
                }
                System.out.printf("%n%s: MA%n%s%n", getName(), MA);
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        System.out.println(getName() + " finished");
    }
}
