# Genetic Algorithm Path Finder

This project aims to develop a console-based Genetic Algorithm for solving optimization problems using a crossover method known as Partially Matched Crossover (PMX). The main objective is to find the shortest path in a given matrix.

## Main Features of the Project

- **Genetic Algorithm**: Simulates natural selection to evolve a solution over successive generations.
- **Crossover Method**: PMX is used to combine two parent solutions (paths) to produce an offspring that inherits characteristics from both parents.
- **Population Management**: Threads are used to manage and evolve a population of solutions concurrently, enhancing performance through parallel processing.
- **File Reading**: The matrix that represents distances or costs is read from a file.

## Programming Techniques Used

- Multithreading environment and synchronized resource sharing.
- Application of biological genetic algorithms for the best offspring determination.
