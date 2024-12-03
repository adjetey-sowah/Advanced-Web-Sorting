package com.juls.labs.websorting.algorithms.sortingAlgorithms;


import com.juls.labs.websorting.algorithms.base.AbstractSortingAlgorithm;
import com.juls.labs.websorting.algorithms.base.SortingResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("mergeSort")
public class MergeSort extends AbstractSortingAlgorithm {
    @Override
    public SortingResult sort(int[] array) {
        resetMetrics();
        long startTime = System.currentTimeMillis();

        mergeSort(array, 0, array.length - 1);

        return createResult(array, startTime);
    }

    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    private void merge(int[] array, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            if (!compare(array[i], array[j])) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }

        while (i <= mid) temp[k++] = array[i++];
        while (j <= right) temp[k++] = array[j++];

        for (i = 0; i < k; i++) {
            array[left + i] = temp[i];
            swaps++;
        }
    }

    @Override
    public String getName() {
        return "Merge Sort";
    }

    public static void main(String[] args) {
        MergeSort mergeSort = new MergeSort();
        SortingResult result = new SortingResult();
        int[] array = {14,54,12,2,67,11,10};
        result = mergeSort.sort(array);
        System.out.println(result);;

    }
    }

