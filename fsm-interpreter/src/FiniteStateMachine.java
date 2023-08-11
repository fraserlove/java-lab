/**
 *
 * A class describing a finite state machine (FSM). The finite state machines behaviour is created from a transition table that is loaded
 * into the FSM. It can then take specific input and using its state compute results to display to screen, all before it updates
 * its own state. The class is designed to be robust and handle multiple different .fsm files and input from standard input.
 *
 */

import java.util.*;
import java.util.regex.*;

public class FiniteStateMachine {

    // The contents of the provided FSM description are stored within a map for constant time - O(1) lookups, this makes the FSM extremely
    // efficient when compared to array or list based counterparts. The FSM uses a custom StateInputKey object to reference each value
    // in the map. The current state and input of the FSM are used as the keys and the output and next state of the FSM are the
    // corresponding values.
    Map<StateInputKey, String[]> transitions;

    String state;

    // Used when checking if a next state is actually a current state in the hashmap and if there is a state-input pair
    // missing from the hash-table.
    Set<String> uniqueStates = new HashSet<>();
    Set<String> uniqueInputs = new HashSet<>();

    public FiniteStateMachine(Scanner fsmDescription) {

        load(fsmDescription);
    }

    // Checks to see if the logic of the FSM makes sense and follows basic rules. This method was created to separate the
    // functionality between checking the structure of the FSM and the actual logic of the FSM. This method does two tests.
    // It checks to see if the next state is in our FSM and if there are any missing rows in our FSM description.
    private boolean isLogicValid() {

        // Iterating through the set of entries present in the transitions HashMap.
        for (String[] outputNextStatePair : transitions.values()) {
            // Checking if the next state is present as a key in the transitions HashMap because if it is not then the
            // FSM description is invalid as a next state is pointing to a state that doesnt exist. The maps keySet()
            // method cannot be used here instead because the keys are comprised of both the current state and input
            // from the FSM description.
            if (!uniqueStates.contains(outputNextStatePair[1])) {
                return false;
            }
            // Using a HashMap, duplicate keys override the others values and so there will be less unique state-input
            // pairs and so will automatically generate a 'Bad description' error.
        }

        // Checking if the number of transitions is less than product of the number of unique states and inputs. If this
        // is true then there is a state-input pair missing from the FSM description and so the description is invalid.
        if (uniqueStates.size() * uniqueInputs.size() > transitions.size()) {
            return false;
        }
        return true;
    }

    // Checks if the layout of the transition table is correct. It does this by checking if the states are numeric
    // and secondly if the input and output symbols are strings made up of a single character.
    private boolean isLayoutValid() {

        // Regex used to make sure that states are integers
        Pattern intPattern = Pattern.compile("\\d+");

        // If the FSM description is empty it is an invalid description
        if (transitions.size() > 0) {
            for (Map.Entry<StateInputKey, String[]> transitionMap : transitions.entrySet()) {
                // Creating temporary variables used to store the boolean describing if the states are numeric helps
                // to increase readability, which is more important than "optimal" programming whereby these are all
                // on one line.
                boolean isCurrentStateNumeric = intPattern.matcher(transitionMap.getKey().getState()).matches();
                boolean isNextStateNumeric = intPattern.matcher(transitionMap.getValue()[1]).matches();
                // Checking for numeric states
                if (!isCurrentStateNumeric || !isNextStateNumeric) {
                    return false;
                }

                // If either of the inputs or outputs are not strings made up of a single character they are invalid.
                // Strings were used to represent symbols because it made the class easier to read rather by removing
                // conversion between characters and strings. Also if characters were used then the program would not return
                // 'Bad description' and instead return an IllegalArgumentException when a input or output longer than one
                // character was present in the FSM description.
                if (transitionMap.getKey().getInput().length() != 1 || transitionMap.getValue()[0].length() != 1) {
                    return false;
                }
            }
        }
        else {
            return false;
        }
        return true;
    }

    // Loads all the data from the specified .fsm file into the transitions HashMap
    private void load(Scanner fsmDescription) {

        int noLines = 0;
        transitions = new HashMap<>();

        while (fsmDescription.hasNextLine()) {

            String fsmTuple = fsmDescription.nextLine();
            String[] fsmSymbols = fsmTuple.split(" ");

            // If number of symbols does not equal 4, ie not containing all (currentState, input, output, nextState) then do not enter
            // the symbols into the transitions map. If the number of symbols is not equal to 4 this is an invalid table.
            if (fsmSymbols.length == 4) {

                String currentState = fsmSymbols[0];
                String input = fsmSymbols[1];

                uniqueStates.add(currentState);
                uniqueInputs.add(input);

                String[] outputNextStatePair = {fsmSymbols[2], fsmSymbols[3]};
                // Adding a new mapping to the transitions in our FSM, where the key is a combination of the current state and the input
                // and this maps to an array containing the output and next state of the FSM.
                transitions.put(new StateInputKey(currentState, input), outputNextStatePair);

                // If this is the first row of symbols read in from the FSM description then the state of the FSM should
                // be set to the current state in this first row.
                if (noLines == 0) {
                    state = currentState;
                }
                noLines++;
            }
        }
        fsmDescription.close();
    }

    // Runs the FSM, first checking if the logic and layout of the FSM is valid. The FSM is ran using the input provided
    // from standard input and is looped over.
    public void run(String inputString) {

        if (isLogicValid() && isLayoutValid()) {
            for (int j = 0; j < inputString.length(); j++) {

                StateInputKey stateInputPair = new StateInputKey(state, "" + inputString.charAt(j));

                // Checking if the current input symbol / current state pair is equal to an input symbol / current state
                // pair in the transitions map using O(1) lookup. If this is not the case, the input provided was incorrect.
                if (transitions.containsKey(stateInputPair)) {
                    String output = transitions.get(stateInputPair)[0];
                    String nextState = transitions.get(stateInputPair)[1];

                    // Outputting result immediately to standard output and changing the state of the FSM
                    System.out.print(output);
                    state = nextState;
                }
                else {
                    System.out.println("Bad input");
                    return;
                }
            }
        }
        else {
            // Either the logic or the layout of the FSM description provided is invalid an error is shown to the user.
            System.out.println("Bad description");
            return;
        }
    }
}