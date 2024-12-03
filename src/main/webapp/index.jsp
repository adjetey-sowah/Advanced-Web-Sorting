<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sorting Visualizer</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background: #1a1a2e;
            color: #fff;
            min-height: 100vh;
            padding: 2rem;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
        }

        .header {
            text-align: center;
            margin-bottom: 2rem;
        }

        .header h1 {
            font-size: 2.5rem;
            margin-bottom: 1rem;
            color: #4ecca3;
        }

        .controls {
            display: flex;
            gap: 1rem;
            flex-wrap: wrap;
            justify-content: center;
            margin-bottom: 2rem;
        }

        .control-group {
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        button, select, input {
            padding: 0.5rem 1rem;
            border: none;
            border-radius: 4px;
            background: #4ecca3;
            color: #1a1a2e;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        button:hover {
            background: #45b393;
            transform: scale(1.05);
        }

        .visualization {
            display: flex;
            align-items: flex-end;
            justify-content: center;
            height: 400px;
            gap: 2px;
            padding: 1rem;
            background: #232741;
            border-radius: 8px;
            overflow: hidden;
        }

        .bar {
            background: #4ecca3;
            transition: height 0.2s ease;
            width: 100%;
        }

        .stats {
            margin-top: 1rem;
            text-align: center;
            padding: 1rem;
            background: #232741;
            border-radius: 8px;
        }

        @media (max-width: 768px) {
            .controls {
                flex-direction: column;
            }

            .visualization {
                height: 300px;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>Sorting Visualizer</h1>
        <p>Watch sorting algorithms in action!</p>
    </div>

    <div class="controls">
        <div class="control-group">
            <label for="arraySize">Array Size:</label>
            <input type="number" id="arraySize" min="1" max="1000" value="50">
        </div>

        <div class="control-group">
            <label for="algorithm">Algorithm:</label>
            <select id="algorithm"></select>
        </div>

        <button onclick="generateNewArray()">Generate New Array</button>
        <button onclick="startSort()">Sort!</button>
    </div>

    <div class="visualization" id="arrayContainer"></div>

    <div class="stats">
        <p>Time taken: <span id="timeTaken">0</span> s</p>
        <p>Comparisons: <span id="comparisons">0</span></p>
        <p>Swaps: <span id="swaps">0</span></p>
    </div>
</div>

<script>
    let currentArray = [];
    let algorithms = [];

    async function fetchAlgorithms() {
        try {
            const response = await fetch('/WebSortingAlgorithm_war/api/v1/sorting/algorithms');
            algorithms = await response.json();
            const algorithmSelect = document.getElementById('algorithm');
            algorithms.forEach(algo => {
                const option = document.createElement('option');
                option.value = algo;
                option.textContent = algo;
                algorithmSelect.appendChild(option);
            });
        } catch (error) {
            console.error('Error fetching algorithms:', error);
        }
    }

    async function generateNewArray() {
        const size = document.getElementById('arraySize').value;
        try {
            const response = await fetch("/WebSortingAlgorithm_war/api/v1/sorting/generate?size="+size);
            currentArray = await response.json();
            visualizeArray(currentArray);
            resetStats();
        } catch (error) {
            console.error('Error generating array:', error);
        }
    }

    async function startSort() {
        const algorithm = document.getElementById('algorithm').value;
        if (!currentArray.length) {
            alert('Please generate an array first!');
            return;
        }

        try {
            const startTime = performance.now();
            const response = await fetch('/WebSortingAlgorithm_war/api/v1/sorting/sort', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    array: currentArray,
                    algorithm: algorithm
                })
            });

            // Parse the JSON response
            const sortingResult = await response.json();

            // Now you can access all fields from SortingResult
            const {
                array: sortedArray,
                comparisons,
                swaps,
                executionTime
            } = sortingResult;


            console.log(sortedArray);
            console.log(swaps);
            console.log(comparisons);

            const endTime = performance.now();

            visualizeArray(sortedArray);
            console.log("Array" +sortedArray);
            updateStats(endTime - startTime, sortingResult.comparisons, sortingResult.swaps);
        } catch (error) {
            console.error('Error sorting array:', error);
        }
    }

    function visualizeArray(array) {
        const container = document.getElementById('arrayContainer');
        container.innerHTML = '';

        const maxVal = Math.max.apply(null, array);

        array.forEach(value => {
            const bar = document.createElement('div');
            bar.className = 'bar';
            bar.style ="height : "+(value / maxVal) * 100 +"%;";
            bar.textContent = value;
            container.appendChild(bar);

        });
    }

    function resetStats() {
        document.getElementById('timeTaken').textContent = '0';
        document.getElementById('swaps').textContent = '0';
        document.getElementById('comparisons').textContent = '0';
    }

    function updateStats(time, comparisons,swaps) {
        document.getElementById('timeTaken').textContent = time.toFixed(2);
        document.getElementById('comparisons').textContent = comparisons;
        document.getElementById('swaps').textContent = swaps;
    }

    // Initialize the page
    fetchAlgorithms();
    generateNewArray();
</script>
</body>
</html>