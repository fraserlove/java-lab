/**
 *
 * A class describing a finite state machine (FSM). The finite state machines behaviour is created from a transition table that is loaded
 * into the FSM. It can then take specific input and using its state compute results to display to screen, all before it updates
 * its own state. The class is designed to be robust and handle multiple different .fsm files and input from standard input.
 *
 */

import java.util.Scanner;
import java.util.regex.Pattern;

public class FiniteStateMachine {

    // A transition table object to store the current states, inputs, outputs and new states as well as perform
    // comparisons, tests etc. to separate functionality between the FSM and transition table.
    TransitionTable transitions;

    // Stores the current state of the FSM as a string since states are represented by numbers and can have more than one digit
    // I have not stored state as an integer here as for future use I want to easily represent states as other symbols
    String state;

    public FiniteStateMachine(Scanner fsmDescription) {

        load(fsmDescription);
    }

    // Checks if the layout of the transition table is correct. This functionality could have been implemented within the loadFSM()
    // method to check the validity of every line before it is loaded into the transition table object. However
    // the functionality has been separated to reduce complexity.
    private boolean isLayoutValid() {

        //transitions.printTable();
        boolean validLayout = true;
        // Regex used to make sure that states are numeric integers
        Pattern intPattern = Pattern.compile("\\d+");

        if (transitions.length() > 0) {
            for (int i = 0; i < transitions.length(); i++) {
                // If either of the strings representing states are not numeric integers, then the FSM description is invalid
                boolean isCurrentStateNumeric = intPattern.matcher(transitions.getCurrentState(i)).matches();
                boolean isNextStateNumeric = intPattern.matcher(transitions.getNextState(i)).matches();
                if (!isCurrentStateNumeric || !isNextStateNumeric) {
                    validLayout = false;
                }
                // If the input or the output is not a single character then the FSM is invalid.
                if (!transitions.isInputAndOutputSingleCharacters(i)) {
                    validLayout = false;
                }
            }
        } else {
            // If a transition table has not been loaded it is invalid
            validLayout = false;
        }

        return validLayout;
    }

    // Checks to see if the logic of the FSM makes sense and follows basic rules. This method was created to separate the
    // functionality between checking the file structure of the FSM and the actual logic of the FSM. This method does three
    // main tests. It first checks to see if the next state is a state that is in our FSM, it checks to see if there are duplicate
    // states in our FSM
    private boolean isLogicValid() {

        boolean validLogic = true;
        // Tracks the number of cases where a row is an exact duplicate of another row
        int noOfExactDuplicates = 0;

        for (int i = 0; i < transitions.length(); i++) {
            // Checking to see if the next state is a valid state or not
            if(!transitions.isValidNextState(i)) {
                validLogic = false;
            }

            // Checking that there are no duplicate state, input pairs with different outputs or next states
            for (int j = i + 1; j < transitions.length() - i; j++) {
                if (transitions.isDuplicatePair(i, j)) {
                    // If a FSM has a state that with the same input gives different outputs or goes into different states it is invalid
                    if (transitions.isInvalidDuplicatePair(i, j)) {
                        validLogic = false;
                    }
                    // If a FSM has all input, output, currentStates and nextStates being the same this is not incorrect as the behaviour
                    // of the FSM is the same, however the number of exact duplicates has to be updated for when we calculate if each state
                    // can handle every input
                    else {
                        noOfExactDuplicates++;
                    }
                }
            }
        }

        // Checking to see if each state has an entry for each input. This is done by first creating a set of unique states and inputs and then multiplying
        // both of their sizes and checking if they are greater than the size of the FSM table, minus any exact duplicates (duplicates where all rows are
        // the same).
        if (transitions.noOfUniqueStateInputPairs() > transitions.length() - noOfExactDuplicates) {
            validLogic = false;
        }
        return validLogic;
    }

    // Loads all the data from the specified .fsm file into the transition table object
    public void load(Scanner fsmDescription) {

        int noLines = 0; // Used to keep track of the number of lines in the description
        transitions = new TransitionTable();

            while (fsmDescription.hasNextLine()) {
                String fsmTuple = fsmDescription.nextLine();
                String[] fsmSymbols = fsmTuple.split(" ");
                // If number of symbols does not equal 4, ie not containing all (currentState, input, output, nextState) then do not enter
                // the symbols into the transition table. If the number of symbols is not equal to 4 this is an invalid table, however by not including
                // it within the transition table, when we later check to see that all of the rows are present in the transition table this will
                // cause a 'Bad description' error, and so the correct output will be created.
                if (fsmSymbols.length == 4) {
                    transitions.addRow(fsmSymbols);
                    noLines++;
                }
            }
            fsmDescription.close();
        // Check to see if the file is empty before trying to access the current states array. As if not checked
        // error could occur trying to access a list of length zero within the transition table class.
        if (noLines > 0) {
            // Once loaded, the current state of the FSM is set to the initial state which is the first state in the transition table -
            // (state that was also first in the .fsm file)
            state = transitions.getInitialState();
        }
    }

    // Runs the FSM, first checking if the logic and layout of the FSM is valid. The FSM is ran using the input provided from standard input and is looped
    // over. The method then loops over all of the rows of the FSM and switches to the next state whilst outputting the result to standard output.
    public void run(String inputString) {

        if(isLogicValid() && isLayoutValid()) {
            for (int j = 0; j < inputString.length(); j++) {
                for (int i = 0; i < transitions.length(); i++) {
                    String inputSymbol = inputString.substring(j,j+1);
                    // Using nested if statements as helps speed up program on larger FSMs with lots of states since less comparisons have to be done
                    if (transitions.isValidInput(inputSymbol)) {
                        // Checking if input is valid and if the current symbol is equal to an inputSymbol in the transitions table
                        if (transitions.getCurrentState(i).equals(state)) {
                            if (transitions.areInputSymbolsEqual(i, inputSymbol)) {
                                // Outputting result immediately to standard output rather than storing in a variable to create correct FSM behaviour.
                                // Otherwise on a large FSM it would take a long time to get any output
                                transitions.showOutput(i);
                                state = transitions.getNextState(i);
                                break;
                            }
                        }
                    }
                    else {
                        System.out.println("Bad input");
                        return;
                    }
                }
            }
        }
        else {
            System.out.println("Bad description");
            return;
        }
    }
}