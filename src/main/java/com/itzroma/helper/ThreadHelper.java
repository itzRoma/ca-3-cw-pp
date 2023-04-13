package com.itzroma.helper;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ThreadHelper {
    private ThreadHelper() {
    }

    public static void showIndicesDistribution(int maxThreadsNumber, int elementsNumber) {
        validateInput(maxThreadsNumber, elementsNumber);

        Map<Integer, Map<Integer, IndicesPair>> elementsDistribution = new LinkedHashMap<>();
        for (int i = 1; i <= maxThreadsNumber; i++) {
            elementsDistribution.put(i, distribute(i, elementsNumber));
        }

        String elementFormat = " %d-%d ";
        String threadFormat = "%d thread%s: %s%n";
        elementsDistribution.forEach((key, value) -> {
            String els = value.values().stream()
                    .map(integers -> elementFormat.formatted(integers.start(), integers.end()))
                    .collect(Collectors.joining());
            System.out.printf(threadFormat, key, key == 1 ? "" : "s", els.stripTrailing());
        });
    }

    private static void validateInput(int threadsNumber, int elementsNumber) {
        if (threadsNumber < 1) {
            throw new IllegalArgumentException("Should be at least one thread");
        }
        if (elementsNumber < threadsNumber) {
            throw new IllegalArgumentException("elementsNumber should be equal or more than maxThreadsNumber");
        }
    }

    private static Map<Integer, IndicesPair> distribute(int threadsNumber, int elementsNumber) {
        Integer[] els = new Integer[threadsNumber];
        Arrays.fill(els, elementsNumber / threadsNumber);
        for (int j = 0; j < elementsNumber % threadsNumber; j++) {
            els[j]++;
        }

        Map<Integer, IndicesPair> distribution = new LinkedHashMap<>();
        int nextIndex = 0;
        int currentThread = 1;
        for (Integer el : els) {
            distribution.put(currentThread++, new IndicesPair(nextIndex, nextIndex + el - 1));
            nextIndex += el;
        }

        return distribution;
    }

    public static Map<Integer, IndicesPair> getIndicesDistributionForNThreads(int threadsNumber, int elementsNumber) {
        validateInput(threadsNumber, elementsNumber);
        return distribute(threadsNumber, elementsNumber);
    }
}
