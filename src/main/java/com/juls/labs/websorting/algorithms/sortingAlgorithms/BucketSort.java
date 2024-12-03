package com.juls.labs.websorting.algorithms.sortingAlgorithms;

import com.juls.labs.websorting.algorithms.base.AbstractSortingAlgorithm;
import com.juls.labs.websorting.algorithms.base.SortingResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Qualifier("bucketSort")
public class BucketSort extends AbstractSortingAlgorithm {
    @Override
    public SortingResult sort(int[] array) {
        resetMetrics();
        long startTime = System.currentTimeMillis();

        int maxVal = getMax(array);
        int minVal = getMin(array);
        int range = maxVal - minVal + 1;

        ArrayList<ArrayList<Integer>> buckets = new ArrayList<>(range);
        for (int i = 0; i < range; i++) {
            buckets.add(new ArrayList<>());
        }

        // Add elements to buckets
        for (int num : array) {
            buckets.get(num - minVal).add(num);
        }

        // Concatenate buckets back into array
        int currentIndex = 0;
        for (ArrayList<Integer> bucket : buckets) {
            for (int num : bucket) {
                array[currentIndex++] = num;
                swaps++;
            }
        }

        return createResult(array, startTime);
    }

    private int getMax(int[] array) {
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (compare(array[i], max)) {
                max = array[i];
            }
        }
        return max;
    }

    private int getMin(int[] array) {
        int min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (compare(min, array[i])) {
                min = array[i];
            }
        }
        return min;
    }

    @Override
    public String getName() {
        return "Bucket Sort";
    }

    public static void main(String[] args) {
        BucketSort bucketSort = new BucketSort();
        SortingResult result = new SortingResult();
        int[] array = {14,54,12,2,67,11,10};
        result = bucketSort.sort(array);
        System.out.println(result);;

    }
    }

