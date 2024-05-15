/**
 * Backtracking (BT) solver.
 */
public class BT extends Solver {

	/**
	 * Creates a new backtracking solver for a given CSP.
	 *
	 * @param csp The constraint satisfaction problem to be solved.
	 * @param ordering The variable ordering heuristic to be used.
	 */
	public BT(BinaryCSP csp, VariableOrdering ordering) {
		super(csp, ordering);
	}

	public void solve() {
		startTime = System.nanoTime();
		backtrack(0);
	}

	/**
	 * Solves the CSP using backtracking.
	 */
	private void backtrack(int depth) {
        BinaryVariable var = csp.getVar(depth);
        for (int val : var.domain()) {
            searchNodes++;
            assign(var, val);
            if (csp.isConsistent(var)) {
                if(csp.completeAssignment()) {
                    endTime = System.nanoTime();
                    showSolution();
                    System.exit(0);
                }
                backtrack(depth + 1);
            }
            unassign(var);
        }
	}
}
