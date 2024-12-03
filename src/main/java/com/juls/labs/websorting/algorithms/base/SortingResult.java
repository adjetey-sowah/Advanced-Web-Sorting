package com.juls.labs.websorting.algorithms.base;

import java.util.Arrays;

public class SortingResult {

    private int[] array;
    private long comparisons;
    private long swaps;
    private long executionTime;

    public SortingResult() {
    }

    public SortingResult(int[] array, long comparisons, long swaps, long executionTime) {
        this.array = array;
        this.comparisons = comparisons;
        this.swaps = swaps;
        this.executionTime = executionTime;
    }

    public int[] getArray() {
        return array;
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    public long getComparisons() {
        return comparisons;
    }

    public void setComparisons(long comparisons) {
        this.comparisons = comparisons;
    }

    public long getSwaps() {
        return swaps;
    }

    public void setSwaps(long swaps) {
        this.swaps = swaps;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    @Override
    public String toString() {
        return "SortingResult{" +
                "array=" + Arrays.toString(array) +
                ", comparisons=" + comparisons +
                ", swaps=" + swaps +
                ", executionTime=" + executionTime +
                '}';
    }
}
