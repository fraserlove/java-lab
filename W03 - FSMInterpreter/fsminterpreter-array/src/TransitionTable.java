/**
 *
 * A class describing a transition table whereby current states, inputs, outputs and next states are stored in parallel arrays. This
 * class also provides functionality to check the validity of states, find duplicate states, etc. This class was created to remove
 * functionality from the FSM table to make it more abstract and maintainable.
 *
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransitionTable {

    // Parallel lists to store data from the FSM description
    // Strings are used instead of characters for future use where words instead of single characters want to be used
    List<String> currentStates;
    List<String> inputs;
    List<String> outputs;
    List<String> nextStates;

    public TransitionTable() {

        currentStates = new ArrayList();
        inputs = new ArrayList();
        outputs = new ArrayList();
        nextStates = new ArrayList();
    }

    public String getCurrentState(int index) {

        return currentStates.get(index);
    }

    public String getNextState(int index) {

        return nextStates.get(index);
    }

    public String getInitialState() {

        return getCurrentState(0);
    }

    public int length() {

        return currentStates.size();
    }

    public boolean isInputAndOutputSingleCharacters(int index) {

        return inputs.get(index).length() == 1 && outputs.get(index).length() == 1;
    }

    public boolean areInputSymbolsEqual(int index, String inputSymbol) {

        return inputs.get(index).equals(inputSymbol);
    }

    public boolean isValidInput(String inputSymbol) {

        return inputs.contains(inputSymbol);
    }

    public boolean isValidNextState(int index) {

        return currentStates.contains(nextStates.get(index));
    }

    public boolean isDuplicatePair(int index1, int index2) {

        return currentStates.get(index1).equals(currentStates.get(index2)) && inputs.get(index1).equals(inputs.get(index2));
    }

    // Duplicate pairs are invalid if the same currentState and input do not have the same output and/or next state
    public boolean isInvalidDuplicatePair(int index1, int index2) {

        return !outputs.get(index1).equals(outputs.get(index2)) || !nextStates.get(index1).equals(nextStates.get(index2));
    }

    // Returns the number of states that there should be in a complete transition table by taking the product of the
    // sets of the current states and input symbols
    public int noOfUniqueStateInputPairs() {

        Set<String> uniqueStates = new HashSet(currentStates);
        Set<String> uniqueInputs = new HashSet(inputs);

        return uniqueStates.size() * uniqueInputs.size();
    }

    // Adds a row to the transition table where the states are specified in an input string formatted as "1 a b 2"
    public void addRow(String[] tuple) {

        currentStates.add(tuple[0]);
        inputs.add(tuple[1]);
        outputs.add(tuple[2]);
        nextStates.add(tuple[3]);
    }

    public void showOutput(int index) {

        System.out.print(outputs.get(index));
    }

    // Displays the loaded fsm transition table. This is primarily for debugging purposes
    public void printTable() {

        for (int i = 0; i < length(); i++) {
            System.out.println(currentStates.get(i) + " " + inputs.get(i) + " " + outputs.get(i) + " " + nextStates.get(i));
        }
    }

}
