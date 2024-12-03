<%--
  Created by IntelliJ IDEA.
  User: JuliusAdjeteySowah
  Date: 02/12/2024
  Time: 10:14 am
  To change this template use File | Settings | File Templates.
--%>


<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Algorithm Visualizer</title>
  <style>
    :root {
      --primary: #2c3e50;
      --secondary: #3498db;
      --accent: #e74c3c;
      --background: #ecf0f1;
    }

    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background: var(--background);
      color: var(--primary);
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
      padding: 1rem;
      background: var(--primary);
      color: white;
      border-radius: 10px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }

    .controls {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 1rem;
      margin-bottom: 2rem;
    }

    .control-group {
      background: white;
      padding: 1rem;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .btn {
      background: var(--secondary);
      color: white;
      border: none;
      padding: 0.8rem 1.5rem;
      border-radius: 5px;
      cursor: pointer;
      transition: transform 0.2s, background 0.2s;
      font-size: 1rem;
      width: 100%;
    }

    .btn:hover {
      background: #2980b9;
      transform: translateY(-2px);
    }

    .visualization-container {
      height: 400px;
      background: white;
      border-radius: 8px;
      padding: 1rem;
      position: relative;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      margin-bottom: 2rem;
    }

    .array-bar {
      position: absolute;
      bottom: 0;
      background: var(--secondary);
      transition: height 0.3s, background 0.3s;
    }

    .array-bar.comparing {
      background: var(--accent);
    }

    .array-bar.sorted {
      background: #2ecc71;
    }

    .metrics {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
      gap: 1rem;
      margin-top: 1rem;
    }

    .metric-card {
      background: white;
      padding: 1rem;
      border-radius: 8px;
      text-align: center;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .metric-value {
      font-size: 1.5rem;
      font-weight: bold;
      color: var(--secondary);
    }

    select, input {
      width: 100%;
      padding: 0.5rem;
      margin: 0.5rem 0;
      border: 1px solid #ddd;
      border-radius: 4px;
      font-size: 1rem;
    }

    .legend {
      display: flex;
      justify-content: center;
      gap: 1rem;
      margin-top: 1rem;
    }

    .legend-item {
      display: flex;
      align-items: center;
      gap: 0.5rem;
    }

    .legend-color {
      width: 20px;
      height: 20px;
      border-radius: 4px;
    }
  </style>
</head>
<body>
<div class="container">
  <div class="header">
    <h1>Algorithm Visualizer</h1>
    <p>Watch sorting algorithms in action!</p>
  </div>

  <div class="controls">
    <div class="control-group">
      <label for="arraySize">Array Size:</label>
      <input type="range" id="arraySize" min="10" max="100" value="50">
      <span id="arraySizeValue">50</span>
    </div>

    <div class="control-group">
      <label for="algorithm">Algorithm:</label>
      <select id="algorithm"></select>
    </div>

    <div class="control-group">
      <label for="speed">Animation Speed:</label>
      <input type="range" id="speed" min="1" max="100" value="50">
      <span id="speedValue">50</span>
    </div>

    <div class="control-group">
      <button class="btn" id="generateBtn">Generate New Array</button>
      <button class="btn" id="sortBtn" style="margin-top: 0.5rem;">Start Sorting</button>
    </div>
  </div>

  <div class="visualization-container" id="visualizer"></div>

  <div class="metrics">
    <div class="metric-card">
      <h3>Comparisons</h3>
      <div class="metric-value" id="comparisons">0</div>
    </div>
    <div class="metric-card">
      <h3>Swaps</h3>
      <div class="metric-value" id="swaps">0</div>
    </div>
    <div class="metric-card">
      <h3>Time Elapsed</h3>
      <div class="metric-value" id="timeElapsed">0.00s</div>
    </div>
  </div>

  <div class="legend">
    <div class="legend-item">
      <div class="legend-color" style="background: var(--secondary);"></div>
      <span>Unsorted</span>
    </div>
    <div class="legend-item">
      <div class="legend-color" style="background: var(--accent);"></div>
      <span>Comparing</span>
    </div>
    <div class="legend-item">
      <div class="legend-color" style="background: #2ecc71;"></div>
      <span>Sorted</span>
    </div>
  </div>
</div>

<script>
  class SortingVisualizer {
    constructor() {
      this.array = [];
      this.visualization = document.getElementById('visualizer');
      this.comparisons = 0;
      this.swaps = 0;
      this.startTime = 0;
      this.timeInterval = null;
      this.isSorting = false;

      this.initializeControls();
      this.loadAlgorithms();
      this.generateNewArray();
    }

    initializeControls() {
      document.getElementById('generateBtn').addEventListener('click', () => this.generateNewArray());
      document.getElementById('sortBtn').addEventListener('click', () => this.startSorting());

      const arraySizeInput = document.getElementById('arraySize');
      arraySizeInput.addEventListener('input', (e) => {
        document.getElementById('arraySizeValue').textContent = e.target.value;
        this.generateNewArray();
      });

      const speedInput = document.getElementById('speed');
      speedInput.addEventListener('input', (e) => {
        document.getElementById('speedValue').textContent = e.target.value;
      });
    }

    async loadAlgorithms() {
      try {
        const response = await fetch('/WebSortingAlgorithm_war/api/v1/sorting/algorithms');
        const algorithms = await response.json();
        const select = document.getElementById('algorithm');
        algorithms.forEach(algo => {
          const option = document.createElement('option');
          option.value = algo;
          option.textContent = algo;
          select.appendChild(option);
        });
      } catch (error) {
        console.error('Failed to load algorithms:', error);
      }
    }

    async generateNewArray() {
      if (this.isSorting) return;

      const size = document.getElementById('arraySize').value;
      try {
        const response = await fetch("/WebSortingAlgorithm_war/api/v1/sorting/generate?size=" + size + "");
        this.array = await response.json();
        this.resetMetrics();
        this.renderArray();
      } catch (error) {
        console.error('Failed to generate array:', error);
      }
    }

    resetMetrics() {
      this.comparisons = 0;
      this.swaps = 0;
      document.getElementById('comparisons').textContent = '0';
      document.getElementById('swaps').textContent = '0';
      document.getElementById('timeElapsed').textContent = '0.00s';
      if (this.timeInterval) clearInterval(this.timeInterval);
    }

    renderArray() {
      this.visualization.innerHTML = '';
      const width = this.visualization.clientWidth;
      const height = this.visualization.clientHeight;
      const barWidth = (width / this.array.length) - 1;

      this.array.forEach((value, index) => {
        const bar = document.createElement('div');
        bar.className = 'array-bar';
        bar.style = "width: " + barWidth + "px; left: " + (index * (barWidth + 1)) + "px;";
        bar.style.height = (value / Math.max.apply(null, this.array)) * (height - 20) + "px";
        bar.textContent = value.toString();
        this.visualization.appendChild(bar);
      });
    }


    async startSorting() {
      document.getElementById('sortBtn').addEventListener('click', () => this.startSorting());
      if (this.isSorting) return;
      this.isSorting = true;
      this.resetMetrics();
      this.startTime = Date.now();

      this.timeInterval = setInterval(() => {
        const elapsed = (Date.now() - this.startTime) / 1000;
        document.getElementById('timeElapsed').textContent = elapsed.toFixed(2) + "s";
      }, 10);

      const algorithm = document.getElementById('algorithm').value;
      console.log("This is the array\n", this.array, " \nAnd this is the algorithm \n", algorithm);
      try {
        const response = await fetch('/WebSortingAlgorithm_war/api/v1/sorting/sort', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            array: this.array,
            algorithm: algorithm
          })
        });

        const result = await response.json();
        console.log("Result ", result);
        await this.animateSorting(result);
        this.visualization.firstChild.remove();
      } catch (error) {
        console.error('Sorting failed:', error);
      }

      this.isSorting = false;
      clearInterval(this.timeInterval);
    }

// Ensure `renderResult` is part of the class
     visualizeArray(array) {
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



    async animateSorting(result) {
      // Validate the result
      if (!result || !result.array) {
        console.error('No valid sorting result received');
        return;
      }

      const { array, comparisons, swaps, executionTime } = result;

      // Ensure the array is not empty
      if (array.length === 0) {
        console.error('No data to animate');
        return;
      }

      // Display the number of comparisons and swaps
      this.comparisons = comparisons;
      this.swaps = swaps;
      document.getElementById('comparisons').textContent = this.comparisons;
      document.getElementById('swaps').textContent = this.swaps;
      document.getElementById('timeElapsed').textContent = executionTime + "ms";

      const speed = 101 - document.getElementById('speed').value;
      const bars = Array.from(document.getElementsByClassName('array-bar'));

      if (!bars.length) {
        console.error('No bars found for animation');
        return;
      }

      try {
        // Perform Bubble Sort animation
        for (let i = 0; i < array.length - 1; i++) {
          for (let j = 0; j < array.length - i - 1; j++) {
            if (!this.isSorting) break;

            // Highlight bars being compared
            const barJ = bars[j];
            const barJNext = bars[j + 1];
            barJ.classList.add('comparing');
            barJNext.classList.add('comparing');
            await this.sleep(speed);

            // Swap if necessary
            if (array[j] > array[j + 1]) {
              [array[j], array[j + 1]] = [array[j + 1], array[j]];

              // Update the bar heights
              // Update the bar heights
              const tempHeight = barJ.style.height;
              barJ.style = "height :"+ barJNext.style.height+"px;";
              barJNext.style = "height: " + tempHeight+"px;";

              // Update the swaps count
              this.swaps++;
              document.getElementById('swaps').textContent = this.swaps;
            }

            // Remove comparison highlight
            barJ.classList.remove('comparing');
            barJNext.classList.remove('comparing');
          }

          // Mark the last sorted element
          bars[array.length - i - 1].classList.add('sorted');
        }

        // Mark the first element as sorted after the loop
        bars[0].classList.add('sorted');

        // Clear unsorted array bars
        this.visualization.innerHTML = '';

        // Render the sorted array in the correct order
        this.visualizeArray(array);

      } catch (error) {
        console.error('Animation error:', error);
      }
    }



    sleep(ms) {
      return new Promise(resolve => setTimeout(resolve, ms));
    }
  }

  // Initialize the visualizer when the page loads
  document.addEventListener('DOMContentLoaded', () => {
    new SortingVisualizer();
  });
</script>
</body>
</html>