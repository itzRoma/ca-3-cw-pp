package com.itzroma.taras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadsRunner {

    private final int threadsAmount;
    private final int elementsAmount;

    private final CyclicBarrier B1;
    private final CyclicBarrier B2;
    private final CyclicBarrier B3;
    private final Lock CS1;
    private final Semaphore S1;
    private final Semaphore S2;

    public ThreadsRunner(int threadsAmount, int elementsAmount) {
        this.threadsAmount = threadsAmount;
        this.elementsAmount = elementsAmount;

        B1 = new CyclicBarrier(threadsAmount);
        B2 = new CyclicBarrier(threadsAmount);
        B3 = new CyclicBarrier(threadsAmount);
        CS1 = new ReentrantLock();
        S1 = new Semaphore(1);
        S2 = new Semaphore(0);
    }

    public Long run() {
        Data data = new Data(elementsAmount);
        List<Thread> threadList = new ArrayList<>();

        Integer[] elements = new Integer[threadsAmount];
        Arrays.fill(elements, elementsAmount / threadsAmount);
        for (int i = 0; i < elementsAmount % threadsAmount; i++) elements[i]++;

        int nextIndex = 0;
        for (int i = 1; i <= threadsAmount; i++) {
            threadList.add(new WorkerThread(i, data, nextIndex, nextIndex + elements[i - 1] - 1, this));
            nextIndex += elements[i - 1];
        }

        long timeStart = System.currentTimeMillis();

        threadList.forEach(Thread::start);
        threadList.forEach(myThread -> {
            try {
                myThread.join();
            } catch (InterruptedException e) {
                myThread.interrupt();
                throw new RuntimeException(e);
            }
        });

        return System.currentTimeMillis() - timeStart;
    }

    public int getThreadsCount() {
        return threadsAmount;
    }

    public CyclicBarrier getB1() {
        return B1;
    }

    public CyclicBarrier getB2() {
        return B2;
    }

    public CyclicBarrier getB3() {
        return B3;
    }

    public Lock getCS1() {
        return CS1;
    }

    public Semaphore getS1() {
        return S1;
    }

    public Semaphore getS2() {
        return S2;
    }
}
