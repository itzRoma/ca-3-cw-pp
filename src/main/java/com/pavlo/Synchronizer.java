package com.pavlo;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Synchronizer {
    public CyclicBarrier B1;
    public CyclicBarrier B2;
    public Semaphore S1;
    public Lock CS1;
    public Semaphore S2;

    public Synchronizer(int threadsAmount) {
        B1 = new CyclicBarrier(threadsAmount);
        B2 = new CyclicBarrier(threadsAmount);
        S1 = new Semaphore(1);
        CS1 = new ReentrantLock();
        S2 = new Semaphore(0);
    }
}
