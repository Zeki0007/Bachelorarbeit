# Bachelor Thesis: Algebraic Data Types in Object-Oriented Software Development

## Abstract
This repository contains the prototype implementations and benchmarking code for my Bachelor's thesis: *"Algebraic Data Types in Object-Oriented Software Development: A Case Study on Sealed Classes and Pattern Matching in Java Compared to Haskell"*.

The core objective of this project is to investigate to what extent modern Java features (Sealed Classes, Records, and Pattern Matching) are suitable for representing Haskell's algebraic data types (ADTs), and how they influence the implementation of classic design patterns.

## Research Questions
The theoretical and practical work is guided by the following sub-questions:
* **The Structure:** How can algebraic data types (ADTs) from Haskell be mapped syntactically in Java using Sealed Classes to define type hierarchies and Records as immutable data containers?
* **The Logic:** How does the evaluation of these data structures through Pattern Matching in Java compare to Haskell, especially regarding type safety and the check for completeness (exhaustiveness)?
* **Paradigm Shift in Design Patterns:** How does the switch from dynamic binding (traditional subtyping) to static pattern matching on closed type hierarchies alter the implementation of classic design patterns in Java, and how does this relate to the functional nature of Haskell?
* **The Comparison:** How do the functional implementations in Java and Haskell differ in terms of developer efficiency, evaluated based on code complexity (boilerplate code), readability, and maintainability?

## Evaluated Design Patterns
To evaluate the paradigm shift from dynamic method dispatch to static pattern matching, this comparative case study implements three distinct patterns:
1. **Visitor Pattern:** Analyzes operations on object structures, comparing dynamic double dispatch against pattern matching on Sealed Classes/ADTs.
2. **Strategy Pattern:** Evaluates the exchange of algorithms, comparing traditional OOP interfaces against higher-order functions and algebraic evaluation.
3. **State Pattern:** Analyzes state management, contrasting class-based state mutation with functional state transitions using immutable records.

## Repository Structure
The repository is divided into three main implementation modules and a simulation orchestrator:

* `/java-classic/`: Traditional Java object-oriented implementations of the patterns using standard classes, interfaces, and dynamic binding. Includes JMH benchmarks.
* `/java-modern/`: Implementations utilizing modern Java features (Java 21+) such as Records, Sealed Interfaces, and Switch Pattern Matching. Includes JMH benchmarks.
* `/haskell/`: Purely functional implementations utilizing Haskell's native Algebraic Data Types (ADTs). Includes Criterion benchmarks.
* `run_simulation.py`: A Python script that orchestrates the build process, runs all JMH and Criterion benchmarks, and generates visual graphs comparing the execution times.

## Prerequisites
To run the code and benchmarks locally, you need to have the following tools installed:
* **Java:** JDK 21 or higher
* **Maven:** For building and running the Java projects
* **Haskell:** GHC and Cabal (or Stack)
* **Python:** Python 3.x with `matplotlib` and `numpy` installed (for the simulation and graph generation)

## How to Run the Simulations
The project includes an automated Python script that compiles the code, runs the benchmarks for all three modules (Classic Java, Modern Java, and Haskell), and visualizes the results.

1. Clone this repository.
2. Ensure you are in the root directory of the project.
3. Run the simulation script:
   ```bash
   python run_simulation.py