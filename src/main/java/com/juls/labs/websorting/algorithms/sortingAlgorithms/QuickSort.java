package com.juls.labs.websorting.algorithms.sortingAlgorithms;

import com.juls.labs.websorting.algorithms.base.AbstractSortingAlgorithm;
import com.juls.labs.websorting.algorithms.base.SortingResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("quickSort")
public class QuickSort extends AbstractSortingAlgorithm {
    @Override
    public SortingResult sort(int[] array) {
        resetMetrics();
        long startTime = System.currentTimeMillis();

        quickSort(array, 0, array.length - 1);

        return createResult(array, startTime);
    }

    private void quickSort(int[] array, int low, int high) {
        if (low < high) {
            int pi = partition(array, low, high);
            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        }
    }

    private int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (!compare(array[j], pivot)) {
                i++;
                swap(array, i, j);
            }
        }

        swap(array, i + 1, high);
        return i + 1;
    }

    @Override
    public String getName() {
        return "Quick Sort";
    }

    public static void main(String[] args) {

        QuickSort quickSort = new QuickSort();
        SortingResult result = new SortingResult();
        int[] array = {14,54,12,2,67,11,10};
        result = quickSort.sort(array);
        System.out.println(result);;


    }
}