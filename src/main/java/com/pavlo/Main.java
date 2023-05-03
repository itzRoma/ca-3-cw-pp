package com.pavlo;

/**
 * Архітектура комп'ютерів 3: Курсова робота, частина 2
 * <p>
 * Варіант: 17
 * Функція: A = (B * C) * Z - B * (MX * MR) * min(B)
 * Кількість потоків (P): 12
 * ПВВ1: B, MX, A
 * ПВВP: Z, D, C, MR
 * <p>
 * Автор: Насонов Павло Олександрович, група ІО-03
 * Дата: 03/05/2023
 */
public class Main {
    public static void main(String[] args) {
        ThreadController threadController = new ThreadController(2, 900);
        System.out.printf("%nCalculation completed in %d ms%n", threadController.calculate());
    }
}