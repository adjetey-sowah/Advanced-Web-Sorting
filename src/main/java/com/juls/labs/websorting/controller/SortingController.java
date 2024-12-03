package com.juls.labs.websorting.controller;

import com.juls.labs.websorting.algorithms.base.SortingResult;
import com.juls.labs.websorting.dto.SortingRequest;
import com.juls.labs.websorting.service.SortingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sorting")
public class SortingController {
    private final SortingService sortingService;

    @Autowired
    public SortingController(SortingService sortingService) {
        this.sortingService = sortingService;
    }

    @GetMapping("/generate")
    public ResponseEntity<int[]> generateArray(@RequestParam(defaultValue = "50") int size) {
        if (size <= 0 || size > 1000) {
            throw new IllegalArgumentException("Size must be between 1 and 1000");
        }
        return ResponseEntity.ok(sortingService.generateArray(size));
    }

    @PostMapping("/sort")
    public ResponseEntity<SortingResult> sort(@RequestBody SortingRequest request) {
        return ResponseEntity.ok(sortingService.sort(request.getArray(), request.getAlgorithm()));
    }

    @GetMapping("/algorithms")
    public ResponseEntity<List<String>> getAlgorithms() {
        return ResponseEntity.ok(sortingService.getAvailableAlgorithms());
    }
}
