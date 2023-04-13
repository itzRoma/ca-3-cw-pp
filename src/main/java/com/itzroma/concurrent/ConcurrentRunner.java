package com.itzroma.concurrent;

import com.itzroma.Runner;
import com.itzroma.helper.IndicesPair;
import com.itzroma.helper.ThreadHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentRunner implements Runner {

    protected final int threadsNumber;
    private final int elementsNumber;

    protected final CyclicBarrier B1;
    protected final CyclicBarrier B2;
    protected final CyclicBarrier B3;

    protected final Semaphore S1;
    protected final Semaphore S2;

    protected final Lock CS1;

    public ConcurrentRunner(int threadsNumber, int elementsNumber) {
        this.threadsNumber = threadsNumber;
        this.elementsNumber = elementsNumber;

        B1 = new CyclicBarrier(this.threadsNumber, () -> System.out.println("All data was provided, starting calculation..."));
        B2 = new CyclicBarrier(this.threadsNumber, () -> System.out.println("Calculation of 'a' is completed, continuing..."));
        B3 = new CyclicBarrier(this.threadsNumber, () -> System.out.println("Calculation of 'm' is completed, continuing..."));

        S1 = new Semaphore(1);
        S2 = new Semaphore(0);

        CS1 = new ReentrantLock();
    }

    @Override
    public Long call() {
        List<Thread> threads = new ArrayList<>();
        Resources resources = new Resources(elementsNumber);
        Map<Integer, IndicesPair> distribution = ThreadHelper.getIndicesDistributionForNThreads(threadsNumber, elementsNumber);
        for (int i = 1; i <= threadsNumber; i++) {
            threads.add(new GenericThread(i, resources, distribution.get(i).start(), distribution.get(i).end(), this));
        }

        long start = System.currentTimeMillis();

        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.printf("Exception in %s: %s%n", thread.getName(), e.getMessage());
                thread.interrupt();
            }
        });

        return System.currentTimeMillis() - start;
    }
}
