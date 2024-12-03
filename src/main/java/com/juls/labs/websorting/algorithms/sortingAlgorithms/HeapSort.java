package com.juls.labs.websorting.algorithms.sortingAlgorithms;


import com.juls.labs.websorting.algorithms.base.AbstractSortingAlgorithm;
import com.juls.labs.websorting.algorithms.base.SortingResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("heapSort")
public class HeapSort extends AbstractSortingAlgorithm {
    @Override
    public SortingResult sort(int[] array) {
        resetMetrics();
        long startTime = System.currentTimeMillis();

        int n = array.length;

        // Build heap
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(array, n, i);

        // Extract elements from heap
        for (int i = n - 1; i > 0; i--) {
            swap(array, 0, i);
            heapify(array, i, 0);
        }

        return createResult(array, startTime);
    }

    private void heapify(int[] array, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && compare(array[left], array[largest]))
            largest = left;

        if (right < n && compare(array[right], array[largest]))
            largest = right;

        if (largest != i) {
            swap(array, i, largest);
            heapify(array, n, largest);
        }
    }

    @Override
    public String getName() {
        return "Heap Sort";
    }




    public static void main(String[] args) {

        HeapSort heapSort = new HeapSort();
        SortingResult result = new SortingResult();
        int[] array = {14,54,12,2,67,11,10};
        result = heapSort.sort(array);
        System.out.println(result);;

    }
}
