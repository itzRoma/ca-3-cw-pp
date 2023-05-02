package com.itzroma.dana;

/**
 * Архітектура комп'ютерів 3
 * Курсова робота, частина 2
 * Варіант: 19
 * Завдання: MA = (MX * MR) * min(C) + (B * C) * MZ
 * Кількість потоків: 32
 * ПВВ1 - C, MA; ПВВP - MX, MZ, MR, B
 * Опалько Богдана ІО-03
 * Дата виконання: 14/04/2023
 */
public class Main {
    public static void main(String[] args) {
        long time = new MultithreadedStarter(32, 320).start();
        System.out.printf("%nCalculation time: %d ms%n", time);
    }
}