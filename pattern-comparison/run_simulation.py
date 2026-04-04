import subprocess
import json
import os
import sys
import time
import threading
import itertools
import matplotlib.pyplot as plt
import numpy as np

# --- Setup Output Directories ---
OUTPUT_DIR = "results_output"
GRAPHS_DIR = os.path.join(OUTPUT_DIR, "graphs")
os.makedirs(GRAPHS_DIR, exist_ok=True)

# Configuration
simulations = [
    # --- STATE PATTERN ---
    {
        "name": "State Pattern (Classic Java)",
        "dir": "java-classic",
        "cmd": "mvn clean package && java -jar target/benchmarks.jar StateBenchmarkClassic -rf json -rff results-StateBenchmarkClassic.json",
        "result_file": "java-classic/results-StateBenchmarkClassic.json",
        "tool": "jmh"
    },
    {
        "name": "State Pattern (Modern Java)",
        "dir": "java-modern",
        "cmd": "mvn clean package && java -jar target/benchmarks.jar StateBenchmarkModern -rf json -rff results-StateBenchmarkModern.json",
        "result_file": "java-modern/results-StateBenchmarkModern.json",
        "tool": "jmh"
    },
    {
        "name": "State Pattern (Haskell)",
        "dir": "haskell",
        "cmd": "cabal bench state-bench --benchmark-options=\"--json=results-haskell-state.json\"",
        "result_file": "haskell/results-haskell-state.json",
        "tool": "criterion"
    },

    # --- STRATEGY PATTERN ---
    {
        "name": "Strategy Pattern (Classic Java)",
        "dir": "java-classic",
        "cmd": "mvn clean package && java -jar target/benchmarks.jar StrategyBenchmarkClassic -rf json -rff results-StrategyBenchmarkClassic.json",
        "result_file": "java-classic/results-StrategyBenchmarkClassic.json",
        "tool": "jmh"
    },
    {
        "name": "Strategy Pattern (Modern Java)",
        "dir": "java-modern",
        "cmd": "mvn clean package && java -jar target/benchmarks.jar StrategyBenchmarkModern -rf json -rff results-StrategyBenchmarkModern.json",
        "result_file": "java-modern/results-StrategyBenchmarkModern.json",
        "tool": "jmh"
    },
    {
        "name": "Strategy Pattern (Haskell)",
        "dir": "haskell",
        "cmd": "cabal bench strategy-bench --benchmark-options=\"--json=results-haskell-strategy.json\"",
        "result_file": "haskell/results-haskell-strategy.json",
        "tool": "criterion"
    },

    # --- AST / VISITOR PATTERN ---
    {
        "name": "AST Pattern (Classic Java)",
        "dir": "java-classic",
        "cmd": "mvn clean package && java -jar target/benchmarks.jar AstBenchmarkClassic -rf json -rff results-AstBenchmarkClassic.json",
        "result_file": "java-classic/results-AstBenchmarkClassic.json",
        "tool": "jmh"
    },
    {
        "name": "AST Pattern (Modern Java)",
        "dir": "java-modern",
        "cmd": "mvn clean package && java -jar target/benchmarks.jar AstBenchmarkModern -rf json -rff results-AstBenchmarkModern.json",
        "result_file": "java-modern/results-AstBenchmarkModern.json",
        "tool": "jmh"
    },
    {
        "name": "AST Pattern (Haskell)",
        "dir": "haskell",
        "cmd": "cabal bench ast-bench --benchmark-options=\"--json=results-haskell-ast.json\"",
        "result_file": "haskell/results-haskell-ast.json",
        "tool": "criterion"
    }
]

def animated_spinner(stop_event, sim_name):
    spinner = itertools.cycle([
        '[>         ]', '[=>        ]', '[==>       ]', '[===>      ]',
        '[====>     ]', '[=====>    ]', '[======>   ]', '[=======>  ]',
        '[========> ]', '[=========>]', '[==========]'
    ])
    while not stop_event.is_set():
        sys.stdout.write(f"\rStart simulation '{sim_name}' ... {next(spinner)} ")
        sys.stdout.flush()
        time.sleep(0.2)
    sys.stdout.write(f"\rStart simulation '{sim_name}' ... [==========] Done!   \n")
    sys.stdout.flush()

def run_command(cmd, work_dir, sim_name):
    stop_event = threading.Event()
    spinner_thread = threading.Thread(target=animated_spinner, args=(stop_event, sim_name))
    spinner_thread.start()

    result = subprocess.run(cmd, cwd=work_dir, shell=True, capture_output=True, text=True)

    stop_event.set()
    spinner_thread.join()

    if result.returncode != 0:
        print("-> ERROR OCCURRED!\n")
        print("=== Error message ===")
        if result.stderr.strip():
            print(result.stderr)
        else:
            print(result.stdout)
        print("=================================\n")

def parse_jmh(file_path):
    parsed_results = []
    with open(file_path, 'r') as f:
        data = json.load(f)

        if not data:
            return [("All", "ERROR: 0 benchmarks executed (JSON empty)")]

        for entry in data:
            full_name = entry.get('benchmark', 'Unknown')
            method_name = full_name.split('.')[-1]

            params = entry.get('params', {})
            if params:
                param_str = ", ".join(f"{k}={v}" for k, v in params.items())
                method_name = f"{method_name} ({param_str})"

            score = entry['primaryMetric']['score']
            unit = entry['primaryMetric']['scoreUnit']

            # Automatic conversion: ms to ns if runtime < 1 ms
            if unit == "ms/op" and score < 1.0:
                score = score * 1_000_000
                unit = "ns/op"

            parsed_results.append((method_name, f"{score:.4f} {unit}"))

    return parsed_results

def parse_criterion(file_path):
    parsed_results = []
    with open(file_path, 'r') as f:
        data = json.load(f)

        if len(data) < 3 or not data[2]:
            return [("All", "ERROR: Criterion JSON invalid or empty")]

        reports = data[2]
        for report in reports:
            test_name = report.get('reportName', 'Unknown')
            mean_seconds = report['reportAnalysis']['anMean']['estPoint']

            ms = mean_seconds * 1000

            # Automatic conversion: ms to ns if runtime < 1 ms
            if ms < 1.0:
                ns = mean_seconds * 1_000_000_000
                parsed_results.append((test_name, f"{ns:.4f} ns/op"))
            else:
                parsed_results.append((test_name, f"{ms:.4f} ms/op"))

    return parsed_results

def generate_graphs(results):
    print("\nGenerating dynamic graphs from live data...")

    state_data = {'Haskell': 0, 'Java Classic': 0, 'Java Modern': 0}
    strat_data = {'Java Classic (Imperative)': 0, 'Haskell (Clean Pipeline)': 0, 'Java Modern (Streams)': 0, 'Java Classic (Pipeline)': 0}
    ast_simple = {'Java Classic': [0]*4, 'Java Modern': [0]*4, 'Haskell': [0]*4}
    ast_complex = {'Java Classic': [0]*4, 'Java Modern': [0]*4, 'Haskell': [0]*4}

    for arch, test, score_str in results:
        if "ERROR" in score_str: continue
        try:
            val = float(score_str.split()[0])
            unit = score_str.split()[1]
        except:
            continue

        val_ms = val if unit == "ms/op" else val / 1_000_000
        val_ns = val if unit == "ns/op" else val * 1_000_000

        if "State Pattern" in arch:
            if "Haskell" in arch: state_data['Haskell'] = val_ms
            elif "Classic Java" in arch: state_data['Java Classic'] = val_ms
            elif "Modern Java" in arch: state_data['Java Modern'] = val_ms

        elif "Strategy Pattern" in arch:
            if "Haskell" in arch: strat_data['Haskell (Clean Pipeline)'] = val_ns
            elif "Modern Java" in arch: strat_data['Java Modern (Streams)'] = val_ns
            elif "Classic Java" in arch:
                if "Imperative" in test or "Loop" in test: strat_data['Java Classic (Imperative)'] = val_ns
                elif "Strict" in test or "Pipeline" in test: strat_data['Java Classic (Pipeline)'] = val_ns

        elif "AST Pattern" in arch:
            idx = -1
            if "13" in test: idx = 0
            elif "16" in test: idx = 1
            elif "19" in test: idx = 2
            elif "23" in test: idx = 3

            if idx != -1:
                cat = 'Haskell' if 'Haskell' in arch else ('Java Classic' if 'Classic Java' in arch else 'Java Modern')
                if "Simple" in test: ast_simple[cat][idx] = val_ms
                elif "Complex" in test and "Lazy Trap" not in test: ast_complex[cat][idx] = val_ms

    # --- Plotting ---
    plt.style.use('ggplot')

    # 1. State Pattern
    labels_state = ['Haskell\n(Purely Functional)', 'Java Classic\n(Mutation)', 'Java Modern\n(Immutability)']
    times_state = [state_data['Haskell'], state_data['Java Classic'], state_data['Java Modern']]
    colors_state = ['#1f77b4', '#ff7f0e', '#2ca02c']
    fig, ax = plt.subplots(figsize=(8, 5))
    bars = ax.bar(labels_state, times_state, color=colors_state)
    ax.set_ylabel('Runtime in ms')
    ax.set_title('State Pattern Performance (1M Workflows)')
    for bar in bars:
        yval = bar.get_height()
        ax.text(bar.get_x() + bar.get_width()/2, yval + (max(times_state)*0.02), f"{yval:.2f} ms", ha='center', va='bottom')
    plt.tight_layout()
    plt.savefig(os.path.join(GRAPHS_DIR, 'state_pattern.png'), dpi=300)
    plt.close()

    # 2. Strategy Pattern (Logarithmic)
    labels_strat = ['Java Classic\n(Imperative)', 'Haskell\n(Clean Pipeline)', 'Java Modern\n(Streams)', 'Java Classic\n(Pipeline)']
    times_strat = [strat_data['Java Classic (Imperative)'], strat_data['Haskell (Clean Pipeline)'], strat_data['Java Modern (Streams)'], strat_data['Java Classic (Pipeline)']]
    colors_strat = ['#ff7f0e', '#1f77b4', '#2ca02c', '#d62728']
    fig, ax = plt.subplots(figsize=(10, 6))
    bars = ax.bar(labels_strat, times_strat, color=colors_strat)
    ax.set_yscale('log')
    ax.set_ylabel('Runtime in ns (logarithmic)')
    ax.set_title('Strategy Pattern Performance (10M Elements)')
    for bar in bars:
        yval = bar.get_height()
        formatted_val = f"{int(yval):,} ns".replace(',', '.')
        ax.text(bar.get_x() + bar.get_width()/2, yval * 1.5, formatted_val, ha='center', va='bottom')
    plt.tight_layout()
    plt.savefig(os.path.join(GRAPHS_DIR, 'strategy_pattern.png'), dpi=300)
    plt.close()

    # 3. & 4. AST Patterns (Line Charts)
    depths = ['D-13\n(~16k)', 'D-16\n(~131k)', 'D-19\n(~1M)', 'D-23\n(~16.7M)']
    x = np.arange(len(depths))

    def plot_ast(data, title, filename):
        fig, ax = plt.subplots(figsize=(9, 5))
        ax.plot(x, data['Java Classic'], marker='o', label='Java Classic', color='#ff7f0e', linewidth=2)
        ax.plot(x, data['Java Modern'], marker='s', label='Java Modern', color='#2ca02c', linewidth=2)
        ax.plot(x, data['Haskell'], marker='^', label='Haskell', color='#1f77b4', linewidth=2)
        ax.set_xticks(x)
        ax.set_xticklabels(depths)
        ax.set_ylabel('Runtime in ms')
        ax.set_title(title)
        ax.legend()
        ax.grid(True, linestyle='--', alpha=0.7)
        plt.tight_layout()
        plt.savefig(os.path.join(GRAPHS_DIR, filename), dpi=300)
        plt.close()

    plot_ast(ast_simple, 'AST Pattern - Simple Tree Scaling', 'ast_simple_tree.png')
    plot_ast(ast_complex, 'AST Pattern - Complex Tree Scaling', 'ast_complex_tree.png')

    print(f"-> 4 graphs successfully saved as PNG in '{GRAPHS_DIR}'!\n")


# --- Main program ---
print("==================================================")
print(" BACHELOR THESIS SIMULATION PIPELINE STARTED")
print("==================================================\n")

results = []

for sim in simulations:
    if os.path.exists(sim["result_file"]):
        os.remove(sim["result_file"])

    run_command(sim["cmd"], sim["dir"], sim["name"])

    if os.path.exists(sim["result_file"]):
        if sim["tool"] == "jmh":
            sub_results = parse_jmh(sim["result_file"])
        else:
            sub_results = parse_criterion(sim["result_file"])

        for test_name, score in sub_results:
            results.append((sim["name"], test_name, score))
    else:
        results.append((sim["name"], "-", "ERROR: No JSON found"))

output_lines = []
output_lines.append("====================================================================================================")
output_lines.append(" FINAL RESULTS")
output_lines.append("====================================================================================================")
output_lines.append(f"{'Architecture':<35} | {'Test Case':<45} | {'Runtime'}")
output_lines.append("-" * 105)

for arch, test, score in results:
    output_lines.append(f"{arch:<35} | {test:<45} | {score}")

output_lines.append("====================================================================================================")

# Output on the console
final_output_string = "\n".join(output_lines)
print(final_output_string)

# Saving the results to a text file
results_file_path = os.path.join(OUTPUT_DIR, "final_results.txt")
with open(results_file_path, "w", encoding="utf-8") as text_file:
    text_file.write(final_output_string)

print(f"\n-> Final results table successfully saved to '{results_file_path}'!")

# Generate the graphs at the end
generate_graphs(results)