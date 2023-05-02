package com.itzroma.taras;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;

public class WorkerThread extends Thread {

    private int threadNumber;

    private Data data;
    private int startAt;
    private int endAt;

    private ThreadsRunner runner;

    public WorkerThread(int threadNumber, Data data, int startAt, int endAt, ThreadsRunner runner) {
        setName("T" + threadNumber);

        this.threadNumber = threadNumber;
        this.data = data;
        this.startAt = startAt;
        this.endAt = endAt;
        this.runner = runner;
    }

    @Override
    public void run() {
        System.out.println(getName() + " started");

        try {
            if (threadNumber == 1) {
                // 1. Введення: C, Z.
                Arrays.fill(data.getC(), 1);
                Arrays.fill(data.getZ(), 1);
            } else if (threadNumber == runner.getThreadsCount()) {
                // 1. Введення: MX, MR, D.
                for (int i = 0; i < data.getSize(); i++) {
                    Arrays.fill(data.getMX()[i], 1);
                    Arrays.fill(data.getMR()[i], 1);
                    data.getD()[i] = 1;
                }
            }

            // 2,3. Очікувати на завершення введення даних.
            runner.getB1().await();

            // 4. Обчислення 1: MAH = MX * MRH.
            for (int i = 0; i < data.getSize(); i++) {
                for (int j = startAt; j <= endAt; j++) {
                    int cell = 0;
                    for (int k = 0; k < data.getMR().length; k++) {
                        cell += data.getMX()[i][k] * data.getMR()[k][j];
                    }
                    data.getMA()[i][j] = cell;
                }
            }

            // 5. Обчислення 2: mi = max(DH).
            int mi = Arrays.stream(data.getD(), startAt, endAt + 1).max().getAsInt();

            // 6. Обчислення 3: m = max(m, mi).
            runner.getCS1().lock();
            data.setM(Math.max(data.getM(), mi));
            runner.getCS1().unlock();

            // 7,8. Очікувати на закінчення обчислення m.
            runner.getB2().await();

            // 9. Обчислення 4: ni = min(CH).
            int ni = Arrays.stream(data.getC(), startAt, endAt + 1).min().getAsInt();

            // 10. Обчислення 5: n = min(n, ni).
            runner.getS1().acquire();
            data.setN(Math.min(data.getN(), ni));
            runner.getS1().release();

            // 11,12. Очікувати на закінчення обчислення n.
            runner.getB3().await();

            // 13. Копіювання: mi = m.
            runner.getCS1().lock();
            mi = data.getM();
            runner.getCS1().unlock();

            // 14. Копіювання: ni = n.
            runner.getS1().acquire();
            ni = data.getN();
            runner.getS1().release();

            // 15. Обчислення 6: AH = mi * D * MAH - ni * ZH.
            for (int i = startAt; i <= endAt; i++) {
                for (int j = 0; j < data.getSize(); j++) {
                    data.getA()[i] += mi * data.getD()[j] * data.getMA()[j][i];
                }
                data.getA()[i] -= ni * data.getZ()[i];
            }

            if (threadNumber == 1) {
                // 16. Чекати сигнал про закінчення обчислення AH в T2…TP.
                runner.getS2().acquire(runner.getThreadsCount() - 1);
            } else {
                // 16. Сигнал Т1 про закінчення обчислення AH.
                runner.getS2().release();
            }

            if (threadNumber == 1) {
                // 17. Виведення: A.
                System.out.printf("%n%s: A%n%s%n%n", getName(), Arrays.toString(data.getA()));
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
