package com.itzroma.helper;

public record IndicesPair(int start, int end) {
    @Override
    public String toString() {
        return "[%d-%d]".formatted(start, end);
    }
}
