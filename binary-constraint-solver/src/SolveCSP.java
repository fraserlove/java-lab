public class SolveCSP {

    /**
     * Main method to solve a binary CSP.
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java SolveCSP <BT/FC/MAC> <file.csp> <true/false>");
            return;
        }
        String algorithm = args[0];
        String CSPLocation = args[1];
        VariableOrdering ordering = VariableOrdering.getOrdering(Integer.parseInt(args[2]));

        BinaryCSPReader reader = new BinaryCSPReader();
        BinaryCSP csp = reader.readBinaryCSP(CSPLocation);
        Solver solver;

        switch(algorithm) {
            case "BT":
                solver = new BT(csp, ordering);
                break;
            case "FC":
                solver = new FC(csp, ordering);
                break;
            case "MAC":
                solver = new MAC(csp, ordering);
                break;
            default:
                System.out.println("Usage: java SolveCSP <BT/FC/MAC> <file.csp> <true/false>");
                return;
        }
        solver.solve();
    }
}
