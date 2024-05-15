# Binary Constraint Solver
A binary constraint solver that uses 2-way branching with backtracking (BT), forward checking (FC) and maintain arc-consistency (MAC) algorithms to solve CSPs. Two variable ordering strategies are also implemented: ascending order (static) and smallest domain first (dynamic). The solver can be run with either BT, FC or MAC and either variable ordering strategy. The solver uses the ascending value ordering by default.
## Usage
A makefile is provided to compile and run the source code. From the root directory, run the following command to compile and run the source code:
```
make compile
```
To run the solver, run the following command:
```
make run args="<BT/FC/MAC> <file.csp> <variable_ordering>"
```
where `<BT/FC/MAC>` is the type of algorithm to use, `<file.csp>` is the path to the file containing the CSP, and `<variable_ordering>` is an integer describing the variable ordering to use, where 0 is the ascending order and 1 is the smallest domain first.

Alternatively, the source code can be compiled and run manually. From the root directory, run the following command to compile and run the source code:
```
javac src/*.java
java -cp src/ SolveCSP <BT/FC/MAC> <file.csp> <variable_ordering>
```
## Input Format
The input file should be a text file with the following format:
```
// Number of variables.
<variable count>

// Domains of variables. (Inclusive, One line per variable.)
<lower_bound>, <upper_bound>
...
<lower_bound>, <upper_bound>

// Constraints. (Variables are 0-indexed, Allowed tuples.)
c(<variable_1>, <variable_2>):
<value_a>, <value_b>
...
<value_i>, <value_j>

c(<variable_2>, <variable_3>):
<value_q>, <value_r>
...
<value_x>, <value_y>

...
```

## Output Format
The output of the solver is the solution to the CSP, if one exists, alongside the number of search nodes, number of arc revisions, solve time, number of variables, and the number of constraints. The output format is as follows:
```
======= Statistics =======
Solver: BT/FC/MAC
Variable Ordering: Ascending/Smallest Domain First
Search Nodes: <number>
Arc Revisions: <number>
Solve Time: <number> ms
Variables: <number>
Constraints: <number>
======== Solution ========
Var 0: <value>
...
Var n: <value>
```