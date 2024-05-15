import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Forward checking (FC) solver.
 */
public class FC extends Solver {

	/**
	 * Creates a new forward checking solver for a given CSP.
	 *
	 * @param csp The constraint satisfaction problem to be solved.
	 * @param ordering The variable ordering heuristic to be used.
	 */
	public FC(BinaryCSP csp, VariableOrdering ordering) {
		super(csp, ordering);
	}

	public void solve() {
		startTime = System.nanoTime();
		forwardChecking();
	}

	/**
	 * Solves the CSP using forward checking.
	 */
	private void forwardChecking() {
		if (csp.completeAssignment()) {
			endTime = System.nanoTime();
			showSolution();
			System.exit(0);
		}
		BinaryVariable var = selectVar();
		int val = selectVal(var);
		branchFCLeft(var, val);
		branchFCRight(var, val);
	}

	/**
	 * Branch left, set a variable to be equal to the value.
	 * 
	 * @param var The variable to assign.
	 * @param val The value to assign.
	 */
	public void branchFCLeft(BinaryVariable var, int val) {
		searchNodes++;
		assign(var, val);

		// Revise all future arcs from the variable.
		if (reviseFutureArcs(var)) {
			variables.remove(var);
			forwardChecking();
			variables.add(var);
		}
		// No solution found, so undo.
		undoPruning();
		unassign(var);
	}

	/**
	 * Branch right, set a variable to be not equal to the value.
	 *
	 * @param var The variable to assign.
	 * @param val The value to assign.
	 */
	public void branchFCRight(BinaryVariable var, int val) {
		searchNodes++;
		deleteValue(var, val);

		// Check for a domain whipeout.
		if (!var.domain().isEmpty()) {
			if (reviseFutureArcs(var)) {
				forwardChecking();
			}
			// No solution found, so undo.
			undoPruning();
		}
		restoreValue(var, val);
	}

	/**
	 * Revise all future arcs from a variable based on the current assignment.
	 * Pruning the domains of future variables.
	 * 
	 * @param current The variable to revise arcs from.
	 * @return True, if the problem is arc consistent, false otherwise.
	 */
	private boolean reviseFutureArcs(BinaryVariable current) {
		Map<BinaryVariable, Set<Integer>> pruned = new HashMap<BinaryVariable, Set<Integer>>();

		// For each arc from the current variable, revise the arc.
		for (BinaryArc arc : csp.futureArcs(current)) {
			BinaryVariable futureVar = arc.future();
			Set<Integer> removed = revise(arc);

			// Record the revised arcs, for undo later.
			if (!pruned.containsKey(futureVar)) {
				pruned.put(futureVar, new HashSet<Integer>());
			}
			pruned.get(futureVar).addAll(removed);

			// Check for a domain whipeout.
			if (futureVar.domain().isEmpty()) {
				domainHistory.push(pruned);
				return false;
			}
		}
		domainHistory.push(pruned);
		return true;
	}
}
