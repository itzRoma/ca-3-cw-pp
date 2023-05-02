package com.itzroma.taras;

/**
 * Архітектура комп'ютерів 3: Курсова робота, частина 2
 * <p>
 * Варіант: 26
 * Функція: A = max(D) * D * (MX * MR) - min(C) * Z
 * Кількість потоків (P): 32
 * ПВВ1: C, Z, A
 * ПВВP: MX, MR, D
 * <p>
 * Автор: Тарасюк Тарас Олександрович, група ІО-03
 * Дата: 15/04/2023
 */
public class Main {
    public static void main(String[] args) {
        long timeTaken = new ThreadsRunner(32, 900).run();
        System.out.printf("%nTime taken: %d ms%n", timeTaken);
    }
}