<%--
  Created by IntelliJ IDEA.
  User: JuliusAdjeteySowah
  Date: 01/12/2024
  Time: 4:54 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sorting Algorithm Visualizer</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }

        body {
            background-color: #f0f2f5;
            padding: 20px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
        }

        .controls {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 20px;
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
            align-items: center;
        }

        select, button {
            padding: 8px 15px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }

        button {
            background-color: #007bff;
            color: white;
            border: none;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        button:hover {
            background-color: #0056b3;
        }

        button:disabled {
            background-color: #cccccc;
            cursor: not-allowed;
        }

        .visualization {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            min-height: 400px;
            position: relative;
        }

        .array-container {
            height: 300px;
            display: flex;
            align-items: flex-end;
            padding: 20px;
            gap: 2px;
        }

        .array-bar {
            flex: 1;
            background-color: #007bff;
            transition: height 0.3s ease;
        }

        .array-bar.comparing {
            background-color: #ffc107;
        }

        .array-bar.swapping {
            background-color: #dc3545;
        }

        .array-bar.sorted {
            background-color: #28a745;
        }

        .metrics {
            margin-top: 20px;
            display: flex;
            gap: 20px;
            flex-wrap: wrap;
        }

        .metric-box {
            background-color: #f8f9fa;
            padding: 10px 15px;
            border-radius: 4px;
            font-size: 14px;
        }

        .speed-control {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .speed-control input {
            width: 150px;
        }

        @media (max-width: 768px) {
            .controls {
                flex-direction: column;
                align-items: stretch;
            }

            .speed-control {
                flex-direction: column;
                align-items: stretch;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="controls">
        <select id="algorithm">
            <option value="heap sort">Heap Sort</option>
            <option value="quick sort">Quick Sort</option>
            <option value="merge sort">Merge Sort</option>
            <option value="radix sort">Radix Sort</option>
            <option value="bucket sort">Bucket Sort</option>
        </select>

        <div class="speed-control">
            <label for="speed">Speed:</label>
            <input type="range" id="speed" min="1" max="100" value="50">
        </div>

        <select id="size">
            <option value="50">50 elements</option>
            <option value="100">100 elements</option>
            <option value="200">200 elements</option>
        </select>

        <button onclick="generateNewArray()">Generate New Array</button>
        <button onclick="startSort()" id="sortButton">Sort</button>
    </div>

    <div class="visualization">
        <div class="array-container" id="arrayContainer"></div>
        <div class="metrics">
            <div class="metric-box">
                Comparisons: <span id="comparisons">0</span>
            </div>
            <div class="metric-box">
                Swaps: <span id="swaps">0</span>
            </div>
            <div class="metric-box">
                Time: <span id="timeElapsed">0s</span>
            </div>
        </div>
    </div>
</div>

<script>
    let array = [];
    let arrayBars = [];
    let sorting = false;

    // Initialize the visualization
    window.onload = function() {
        generateNewArray();
    };

    async function generateNewArray() {
        const size = parseInt(document.getElementById('size').value);
        try {
            const response = await fetch('/WebSortingAlgorithm_war/api/v1/sorting/generate?size=' + size);
            array = await response.json();
            displayArray();
            resetMetrics();
        } catch (error) {
            console.error('Error generating array:', error);
        }
    }

    function displayArray() {
        const container = document.getElementById('arrayContainer');
        container.innerHTML = '';
        arrayBars = [];

        array.forEach((value, index) => {
            const bar = document.createElement('div');
            bar.className = 'array-bar';
            bar.style.height = `${value * 2}px`;
            container.appendChild(bar);
            arrayBars.push(bar);
        });
    }

    function resetMetrics() {
        document.getElementById('comparisons').textContent = '0';
        document.getElementById('swaps').textContent = '0';
        document.getElementById('timeElapsed').textContent = '0s';
    }

    async function startSort() {
        if(sorting) return;

        const sortButton = document.getElementById('sortButton');
        sortButton.disabled = true;
        sorting = true;

        const algorithm = document.getElementById('algorithm').value;

        try {
            const startTime = performance.now();

            const response = await fetch('/WebSortingAlgorithm_war/api/v1/sorting/sort', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    array: array,
                    algorithm: algorithm
                })
            });

            const result = await response.json();

            // Update metrics
            document.getElementById('comparisons').textContent = result.comparisons;
            document.getElementById('swaps').textContent = result.swaps;
            document.getElementById('timeElapsed').textContent =
                (result.executionTime / 1000).toFixed(2) + 's';

            // Animate the sorting
            await animateSorting(result.array);

        } catch (error) {
            console.error('Error sorting array:', error);
        } finally {
            sorting = false;
            sortButton.disabled = false;
        }
    }


    async function animateSorting(sortedArray) {
        const speed = document.getElementById('speed').value;
        const delay = 101 - speed; // Invert speed so higher value = faster

        for(let i = 0; i < sortedArray.length; i++) {
            array[i] = sortedArray[i];
            arrayBars[i].style.height = `${array[i] * 2}px`;
            arrayBars[i].classList.add('comparing');
            await sleep(delay);
            arrayBars[i].classList.remove('comparing');
            arrayBars[i].classList.add('sorted');
        }
    }

    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    // Update array when size changes
    document.getElementById('size').addEventListener('change', generateNewArray);
</script>
</body>
</html>
