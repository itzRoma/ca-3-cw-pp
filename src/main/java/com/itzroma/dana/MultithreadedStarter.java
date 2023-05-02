package com.itzroma.dana;

import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultithreadedStarter {

    private int threadsCount;
    private int elementsCount;

    private CyclicBarrier B1;
    private CyclicBarrier B2;
    private CyclicBarrier B3;

    private Lock CS1;

    private Semaphore S1;
    private Semaphore S2;

    public MultithreadedStarter(int threadsCount, int elementsCount) {
        this.threadsCount = threadsCount;
        this.elementsCount = elementsCount;

        B1 = new CyclicBarrier(threadsCount);
        B2 = new CyclicBarrier(threadsCount);
        B3 = new CyclicBarrier(threadsCount);
        CS1 = new ReentrantLock();
        S1 = new Semaphore(1);
        S2 = new Semaphore(0);
    }

    public Long start() {
        Data data = new Data(elementsCount);
        List<Thread> threadList = new ArrayList<>();
        Map<Integer, List<Integer>> indices = calculateIndices(threadsCount, elementsCount);
        for (int i = 1; i <= threadsCount; i++) {
            threadList.add(new MyThread(i, data, indices.get(i).get(0), indices.get(i).get(1), this));
        }

        long timeStart = System.currentTimeMillis();

        for (Thread thread : threadList) {
            thread.start();
        }
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long timeEnd = System.currentTimeMillis();

        return timeEnd - timeStart;
    }

    private static Map<Integer, List<Integer>> calculateIndices(int threadsCount, int elementsCount) {
        Integer[] elements = new Integer[threadsCount];
        Arrays.fill(elements, elementsCount / threadsCount);
        for (int i = 0; i < elementsCount % threadsCount; i++) elements[i]++;

        Map<Integer, List<Integer>> indices = new LinkedHashMap<>();
        int currentIndex = 0;
        int currentThread = 1;
        for (Integer element : elements) {
            indices.put(currentThread, List.of(currentIndex, currentIndex + element - 1));
            currentThread += 1;
            currentIndex += element;
        }

        return indices;
    }

    public int getThreadsCount() {
        return threadsCount;
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

    public Semaphore getS1() {
        return S1;
    }

    public Semaphore getS2() {
        return S2;
    }

    public Lock getCS1() {
        return CS1;
    }
}
