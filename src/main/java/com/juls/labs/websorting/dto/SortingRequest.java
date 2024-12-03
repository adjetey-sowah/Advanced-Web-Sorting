package com.juls.labs.websorting.dto;


import java.util.Arrays;

public class SortingRequest {

    private int[] array;
    private String algorithm;

    public SortingRequest() {
    }

    public SortingRequest(int[] array, String algorithm) {
        this.array = array;
        this.algorithm = algorithm;
    }

    public int[] getArray() {
        return array;
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String toString() {
        return "SortingRequest{" +
                "array=" + Arrays.toString(array) +
                ", algorithm='" + algorithm + '\'' +
                '}';
    }
}


