package com.juls.labs.websorting.algorithms.base;

public abstract class AbstractSortingAlgorithm implements SortingAlgorithm {

    protected  long comparisons;
    protected long swaps;

    protected void resetMetrics(){
        long DEFAULT_METRIC_VALUES = 0;
        this.comparisons = DEFAULT_METRIC_VALUES;
        this.swaps = DEFAULT_METRIC_VALUES;
    }

    protected void swap(int[] array, int i, int j){
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    protected boolean compare (int a, int b){
        comparisons++;
        return a > b;
    }

    protected SortingResult createResult(int[] array, long startTime){
        long executionTime = (System.currentTimeMillis()-startTime);
        return new SortingResult(array,comparisons,swaps,executionTime);
    }


}
