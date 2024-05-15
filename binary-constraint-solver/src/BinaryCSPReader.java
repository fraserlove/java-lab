import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Reads a binary CSP from a file.
 */
public final class BinaryCSPReader {

    private StreamTokenizer in;

    /**
     * Main (for testing).
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java BinaryCSPReader <file.csp>");
            return;
        }
        BinaryCSPReader reader = new BinaryCSPReader();
        System.out.println(reader.readBinaryCSP(args[0]));
    }

    /**
     * Configures the tokenizer.
     * 
     * @param fileReader The file reader.
     * @throws IOException If an I/O error occurs.
     */
    private void configureTokenizer(FileReader fileReader) throws IOException {
        in = new StreamTokenizer(fileReader);
        in.ordinaryChar('(');
        in.ordinaryChar(')');
        in.nextToken(); // Advance to first token.
    }

    /**
     * Reads a binary CSP from a file.
     * 
     * @param file File name.
     * @return The binary CSP.
     */
    public BinaryCSP readBinaryCSP(String file) {
        try(FileReader fileReader = new FileReader(file)) {
            configureTokenizer(fileReader);
            BinaryVariable[] variables = readVariables();
            Map<BinaryVariable, Map<BinaryVariable, BinaryArc>> arcs = readBinaryArcs(variables);
            return new BinaryCSP(variables, arcs);
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Reads a list of binary arcs. Each constraint is of the form
     * c(<varno>, <varno>) \n <tuple> <tuple> \n <tuple> <tuple> \n ...
     * 
     * @param variables The variables in the CSP.
     * @return The binary arcs.
     */
    private Map<BinaryVariable, Map<BinaryVariable, BinaryArc>> readBinaryArcs(BinaryVariable[] variables) {
        Map<BinaryVariable, Map<BinaryVariable, BinaryArc>> arcs = new HashMap<>();
        try {
            in.nextToken(); // 'c' or EOF.
            while (in.ttype != StreamTokenizer.TT_EOF) {
                BinaryVariable var1 = readVariable(variables);
                BinaryVariable var2 = readVariable(variables);
                in.nextToken();
                Set<BinaryTuple> tuples = readTuples();
                addArc(arcs, var1, var2, tuples);
            }
            return arcs;
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Reads a list of variables.
     * 
     * @return The variables.
     * @throws IOException If an I/O error occurs.
     */
    private BinaryVariable[] readVariables() throws IOException {
        int n = (int) in.nval; // Number of variables.
        BinaryVariable[] variables = new BinaryVariable[n];
        for (int i = 0; i < n; i++) {
            variables[i] = readVariable(i);
        }
        return variables;
    }

    /**
     * Reads a variable and its domain.
     * 
     * @param index The index of the variable.
     * @return The variable.
     * @throws IOException If an I/O error occurs.
     */
    private BinaryVariable readVariable(int index) throws IOException {
        in.nextToken(); // lower
        int lower = (int) in.nval;
        in.nextToken(); // ','
        in.nextToken(); // upper
        int upper = (int) in.nval;
        return new BinaryVariable(index, lower, upper);
    }

    /**
     * Reads a variable.
     * 
     * @param variables The variables.
     * @return The variable.
     * @throws IOException If an I/O error occurs.
     */
    private BinaryVariable readVariable(BinaryVariable[] variables) throws IOException {
        in.nextToken(); // '('
        in.nextToken(); // var
        BinaryVariable variable = variables[(int) in.nval];
        return variable;
    }

    /**
     * Reads a list of tuples.
     * 
     * @return The tuples.
     * @throws IOException If an I/O error occurs.
     */
    private Set<BinaryTuple> readTuples() throws IOException {
        Set<BinaryTuple> tuples = new HashSet<>();
        in.nextToken(); // val1 of first tuple or 'c' or EOF.
        while (!"c".equals(in.sval) && in.ttype != StreamTokenizer.TT_EOF) {
            int val1 = (int) in.nval;
            in.nextToken(); // ','
            in.nextToken(); // val2
            tuples.add(new BinaryTuple(val1, (int) in.nval));
            in.nextToken(); // Next token.
        }
        return tuples;
    }

    /**
     * Adds an arc to the map of arcs.
     * 
     * @param arcs The map of arcs.
     * @param var1 The first variable.
     * @param var2 The second variable.
     * @param tuples The tuples.
     */
    private void addArc(Map<BinaryVariable, Map<BinaryVariable, BinaryArc>> arcs, BinaryVariable var1, BinaryVariable var2, Set<BinaryTuple> tuples) {
        BinaryArc arc = new BinaryArc(var1, var2, tuples);
        if (!arcs.containsKey(var1)) {
            arcs.put(var1, new HashMap<BinaryVariable, BinaryArc>());
        }
        arcs.get(var1).put(var2, arc);

        arc = arc.reverse();
        if (!arcs.containsKey(var2)) {
            arcs.put(var2, new HashMap<BinaryVariable, BinaryArc>());
        }
        arcs.get(var2).put(var1, arc);
    }
}
