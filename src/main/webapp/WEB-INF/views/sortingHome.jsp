<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
            margin: 0; padding: 0; box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: var(--background); color: var(--primary);
            min-height: 100vh; padding: 2rem;
        }
        .container { max-width: 1200px; margin: 0 auto; }
        .header { text-align: center; margin-bottom: 2rem; padding: 1rem; background: var(--primary); color: white; border-radius: 10px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); }
        .controls { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1rem; margin-bottom: 2rem; }
        .control-group { background: white; padding: 1rem; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }
        .btn { background: var(--secondary); color: white; border: none; padding: 0.8rem 1.5rem; border-radius: 5px; cursor: pointer; transition: transform 0.2s, background 0.2s; font-size: 1rem; width: 100%; }
        .btn:hover { background: #2980b9; transform: translateY(-2px); }
        .visualization-container { height: 400px; background: white; border-radius: 8px; padding: 1rem; position: relative; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); margin-bottom: 2rem; }
        .array-bar { position: absolute; bottom: 0; background: var(--secondary); transition: height 0.3s, background 0.3s; }
        .array-bar.comparing { background: var(--accent); }
        .array-bar.sorted { background: #2ecc71; }
        .metrics { display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: 1rem; margin-top: 1rem; }
        .metric-card { background: white; padding: 1rem; border-radius: 8px; text-align: center; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }
        .metric-value { font-size: 1.5rem; font-weight: bold; color: var(--secondary); }
        select, input { width: 100%; padding: 0.5rem; margin: 0.5rem 0; border: 1px solid #ddd; border-radius: 4px; font-size: 1rem; }
        .legend { display: flex; justify-content: center; gap: 1rem; margin-top: 1rem; }
        .legend-item { display: flex; align-items: center; gap: 0.5rem; }
        .legend-color { width: 20px; height: 20px; border-radius: 4px; }
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
            <select id="algorithm">
                <c:forEach var="algorithm" items="${algorithms}">
                    <option value="${algorithm}">${algorithm}</option>
                </c:forEach>
            </select>
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

        async generateNewArray() {
            if (this.isSorting) return;

            const size = document.getElementById('arraySize').value;
            try {
                const response = await fetch(`/api/v1/sorting/generate?size=${size}`);
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
            if (this.isSorting) return;
            this.isSorting = true;
            this.resetMetrics();
            this.startTime = Date.now();

            this.timeInterval = setInterval(() => {
                const elapsed = (Date.now() - this.startTime) / 1000;
                document.getElementById('timeElapsed').textContent = `${elapsed.toFixed(2)}s`;
            }, 10);

            const algorithm = document.getElementById('algorithm').value;
            try {
                const response = await fetch('/api/v1/sorting/sort', {
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
                await this.animateSorting(result);
            } catch (error) {
                console.error('Sorting failed:', error);
            }

            this.isSorting = false;
            clearInterval(this.timeInterval);
        }

        async animateSorting(result) {
            const speed = 101 - document.getElementById('speed').value;
            const bars = document.getElementsByClassName('array-bar');

            for (const step of result.steps) {
                if (step.type === 'comparison') {
                    this.comparisons++;
                    document.getElementById('comparisons').textContent = this.comparisons;
                    bars[step.indices[0]].classList.add('comparing');
                    bars[step.indices[1]].classList.add('comparing');
                    await this.sleep(speed);
                    bars[step.indices[0]].classList.remove('comparing');
                    bars[step.indices[1]].classList.remove('comparing');
                } else if (step.type === 'swap') {
                    this.swaps++;
                    document.getElementById('swaps').textContent = this.swaps;
                    const [i, j] = step.indices;
                    const tempHeight = bars[i].style.height;
                    bars[i].style.height = bars[j].style.height;
                    bars[j].style.height = tempHeight;
                    await this.sleep(speed);
                }
            }

            for (let i = 0; i < bars.length; i++) {
                bars[i].classList.add('sorted');
                await this.sleep(speed / 2);
            }
        }

        sleep(ms) {
            return new Promise(resolve => setTimeout(resolve, ms));
        }
    }

    document.addEventListener('DOMContentLoaded', () => {
        new SortingVisualizer();
    });
</script>
</body>
</html>