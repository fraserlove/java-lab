/**
 *
 * @author 200002548
 * Created 26/09/20
 *
 * A class used to create and control a finite state machine (FSM). The class reads in a .fsm file consisting of a
 * transition table outlineing the current states, inputs, outputs and next states of the machine. Standard input is
 * also used to read in the input to be processed by the fsm.
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class fsminterpreter {

    static FiniteStateMachine fsm;
    static String fsmPath;
    static String fsmInput;

    // Returns a Scanner object to read the specified file.
    private static Scanner createReader(String fsmPath) {

        try {
            File fsmFile = new File(fsmPath);
            return new Scanner(fsmFile);
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred while trying to access the specified file.");
            e.printStackTrace();
            return null; // Returns a null reference instead of a reference to a Scanner object if one cannot be created.
        }
    }

    public static void main(String[] args) {

        fsmPath = args[0];
        Scanner fsmDescription = createReader(fsmPath);

        // Reading in standard input
        Scanner standardInput = new Scanner(System.in);
        fsmInput = standardInput.nextLine();

        fsm = new FiniteStateMachine(fsmDescription);
        fsm.run(fsmInput);
    }
}