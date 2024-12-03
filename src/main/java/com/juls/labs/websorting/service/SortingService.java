package com.juls.labs.websorting.service;

import com.juls.labs.websorting.algorithms.base.SortingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.juls.labs.websorting.algorithms.base.SortingAlgorithm;


@Service
public class SortingService {
    private final Map<String, SortingAlgorithm> algorithmMap;
    private final Random random;

    @Autowired
    public SortingService(List<SortingAlgorithm> algorithms) {
        this.algorithmMap = algorithms.stream()
                .collect(Collectors.toMap(
                        algorithm -> algorithm.getName().toLowerCase(),
                        Function.identity()
                ));
        this.random = new Random();
    }


    public int[] generateArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(100) + 1;
        }
        return array;
    }

    public SortingResult sort(int[] array, String algorithmName) {
        SortingAlgorithm algorithm = algorithmMap.get(algorithmName.toLowerCase());
        if (algorithm == null) {
            throw new IllegalArgumentException("Unknown sorting algorithm: " + algorithmName);
        }

        return algorithm.sort(array.clone());
    }

    public List<String> getAvailableAlgorithms() {
        return new ArrayList<>(algorithmMap.keySet());
    }

    public Map<String, SortingAlgorithm> getAlgorithmMap() {
        return algorithmMap;
    }
}
