import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Maintaining Arc Consistency (MAC) solver.
 */
public class MAC extends Solver {

	/**
	 * Creates a new MAC solver for a given CSP.
	 *
	 * @param csp The constraint satisfaction problem to be solved.
	 * @param ordering The variable ordering heuristic to be used.
	 */
	public MAC(BinaryCSP csp, VariableOrdering ordering) {
        super(csp, ordering);
	}

	public void solve() {
		startTime = System.nanoTime();
		AC3();
		MAC3();
	}

	/**
	 * Solves the CSP using MAC3.
	 */
	private void MAC3() {
		if (csp.completeAssignment()) {
			endTime = System.nanoTime();
			showSolution();
			System.exit(0);
		}
		BinaryVariable var = selectVar();
		int val = selectVal(var);
		branchMAC3Left(var, val);
		branchMAC3Right(var, val);
	}

	/**
	 * Branch left, set a variable to be equal to the value.
	 * 
	 * @param var The variable to assign.
	 * @param val The value to assign.
	 */
	public void branchMAC3Left(BinaryVariable var, int val) {
		searchNodes++;
		assign(var, val);

		// Revise all future arcs from the variable.
		if (AC3()) {
			variables.remove(var);
			MAC3();
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
	public void branchMAC3Right(BinaryVariable var, int val) {
		searchNodes++;
		deleteValue(var, val);

		// Check for a domain whipeout.
		if (!var.domain().isEmpty()) {
			if (AC3()) {
				MAC3();
			}
			// No solution found, so undo.
			undoPruning();
		}
		restoreValue(var, val);
	}

	/**
	 * AC3 algorithm. Revises all arcs from the current variable.
	 * 
	 * @return True, if the problem is arc consistent, false otherwise.
	 */
	private boolean AC3() {
		Map<BinaryVariable, Set<Integer>> pruned = new HashMap<BinaryVariable, Set<Integer>>();
        Queue<BinaryArc> queue = csp.queueArcs();

        while(!queue.isEmpty()) {
            // Revise the current arc.
            BinaryArc arc = queue.remove();
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

            // Add new arcs to the queue where the future variable has been pruned.
			if (!removed.isEmpty()) {
				for (BinaryArc newArc : csp.futureArcs(futureVar)) {
					if (!newArc.equals(arc.reverse())) {
                        queue.add(newArc);
					}
				}
			}
        }
		domainHistory.push(pruned);
		return true;
	}
}
