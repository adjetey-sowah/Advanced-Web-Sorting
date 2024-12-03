package com.juls.labs.websorting.algorithms.sortingAlgorithms;

import com.juls.labs.websorting.algorithms.base.AbstractSortingAlgorithm;
import com.juls.labs.websorting.algorithms.base.SortingResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("radixSort")
public class RadixSort extends AbstractSortingAlgorithm {
    @Override
    public SortingResult sort(int[] array) {
        resetMetrics();
        long startTime = System.currentTimeMillis();

        int max = getMax(array);

        for (int exp = 1; max / exp > 0; exp *= 10) {
            countSort(array, exp);
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

    private void countSort(int[] array, int exp) {
        int n = array.length;
        int[] output = new int[n];
        int[] count = new int[10];

        for (int i = 0; i < n; i++)
            count[(array[i] / exp) % 10]++;

        for (int i = 1; i < 10; i++)
            count[i] += count[i - 1];

        for (int i = n - 1; i >= 0; i--) {
            output[count[(array[i] / exp) % 10] - 1] = array[i];
            count[(array[i] / exp) % 10]--;
            swaps++;
        }

        System.arraycopy(output, 0, array, 0, n);
    }

    @Override
    public String getName() {
        return "Radix Sort";
    }

    public static void main(String[] args) {
        RadixSort radixSort = new RadixSort();
        SortingResult result = new SortingResult();
        int[] array = {14,54,12,2,67,11,10};
        result = radixSort.sort(array);
        System.out.println(result);;

    }
    }

