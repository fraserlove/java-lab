# Finite State Machine Interpreter

A finite state machine (FSM) interpreter which constructs a FSM which then performs computation on a given input. The application reads in a `.fsm` file consisting of a transition table outlining the current states, inputs, outputs and next states of the machine. Standard input is also used to read in the input to be processed by the FSM. The interpreter expects the `.fsm` to be in a tabular format, with 4 columns for current states, inputs, outputs and next states, separated by spaces. States have to be numeric with inputs and outputs composed of single characters. The initial state of the FSM is the first rown in the file. The interpreter will output `Bad description` if the machines description (`.fsm` file) is not well formed. An example of a valid transition table, `valid.fsm` is detailed below:
```
1 a z 2
1 b y 1
2 a x 3
2 b w 1
2 a v 2
3 b u 3
 . . .
```
The characters used as input into the FSM must be present in the set of inputs described in the machines description, otherwise the error message `Bad input` is displayed.

## Usage
The interpreter can be compiled and ran using the following commands inside the `/src` directory.
1. **Compile Java from Source:**
```bash
javac FSMInterpreter.java
```
2. **Run the Compiled Program:**

Include the path to a valid `.fsm` and an input `.txt` file as follows:
```bash
java FSMInterpreter path/to/machine.fsm < path/to/input.txt
```

## Testing
Run the following to initiate testing:
```bash
source tests/run_tests.sh
```
