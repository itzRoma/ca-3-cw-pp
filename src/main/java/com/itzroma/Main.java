package com.itzroma;

import com.itzroma.concurrent.ConcurrentRunner;

/**
 * Архітектура комп'ютерів 3: Курсова робота, частина 2
 * <p>
 * Варіант: 1
 * <p>
 * Функція: A = (B * C) * Z + D * (MX * MR) * min(C)
 * <p>
 * Кількість потоків (P): 32
 * <p>
 * Введення - виведення:
 * <p>
 * - Потік 1: A, B, MX
 * <p>
 * - Потік P: Z, D, C, MR
 * <p>
 * Автор: Бондаренко Роман Ігорович, група ІО-03
 * <p>
 * Дата: 10/04/2023 - 14/04/2023
 */
public class Main {

    public static final int NUMBER_OF_THREADS = 32;

    public static void main(String[] args) {
        long result = new ConcurrentRunner(NUMBER_OF_THREADS, 900).call();
        System.out.printf("%nCalculation finished, it took %d ms%n", result);
    }
}