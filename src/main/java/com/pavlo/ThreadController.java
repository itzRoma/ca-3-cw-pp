package com.pavlo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreadController {

    private final int numberOfThreads;
    private final int numberOfElements;

    public ThreadController(int numberOfThreads, int numberOfElements) {
        this.numberOfThreads = numberOfThreads;
        this.numberOfElements = numberOfElements;
    }

    public long calculate() {
        SharedResources sharedResources = new SharedResources(numberOfElements);
        Synchronizer synchronizer = new Synchronizer(numberOfThreads);
        List<Thread> threads = new ArrayList<>();

        Integer[] elDist = new Integer[numberOfThreads];
        Arrays.fill(elDist, numberOfElements / numberOfThreads);
        for (int i = 0; i < numberOfElements % numberOfThreads; i++) elDist[i]++;

        int nextIdx = 0;
        for (int i = 1; i <= numberOfThreads; i++) {
            threads.add(new MyThread(i, numberOfThreads, sharedResources, synchronizer, nextIdx, nextIdx + elDist[i - 1] - 1));
            nextIdx += elDist[i - 1];
        }

        long startTime = System.currentTimeMillis();

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                thread.interrupt();
                throw new RuntimeException(e);
            }
        }

        return System.currentTimeMillis() - startTime;
    }
}
