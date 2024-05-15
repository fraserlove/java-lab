import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * Solves a binary CSP.
 */
public abstract class Solver {

    // The CSP to be solved.
	BinaryCSP csp;

	// Statistics.
	int searchNodes;
	int arcRevisions;
	long startTime;
	long endTime; 

	// The variable ordering heuristic to be used.
	VariableOrdering ordering;

	// Stores the variables to be assigned.
	Queue<BinaryVariable> variables;

	// Stores the history of future variable domains, used to undo pruning.
	Stack<Map<BinaryVariable, Set<Integer>>> domainHistory;

    /**
	 * Creates a new solver for a given CSP.
	 *
	 * @param csp The constraint satisfaction problem to be solved.
	 * @param ordering The variable ordering heuristic to be used.
	 */
	public Solver(BinaryCSP csp, VariableOrdering ordering) {
		this.csp = csp;
		this.ordering = ordering;

		// Initialise statistics.
		searchNodes = 0;
		arcRevisions = 0;

		// Initialise the queue of variables to be assigned using the given ordering.
		Comparator<BinaryVariable> comparator = (ordering == VariableOrdering.ASCENDING)
				? VariableOrdering.StaticComparator
				: VariableOrdering.SmallestDomainComparator;
		variables = new PriorityQueue<BinaryVariable>(comparator);
		variables.addAll(csp.getVars());

		// Initialise the stack of domain histories.
		domainHistory = new Stack<Map<BinaryVariable, Set<Integer>>>();
	}

	/**
	 * Solves the CSP.
	 */
    public abstract void solve();

    /**
	 * Prints the solution.
	 */
	protected void showSolution() {
		System.out.println("======= Statistics =======");
        System.out.println("Solver: " + this.getClass().getSimpleName());
		System.out.println("Variable Ordering: " + VariableOrdering.toString(ordering));
		System.out.println("Search Nodes: " + searchNodes);
		System.out.println("Arc Revisions: " + arcRevisions);
		System.out.println("Solve Time: " + (endTime - startTime) / 1_000_000.0 + " ms");
		System.out.println("Variables: " + csp.noVariables());
		System.out.println("Constraints: " + csp.noConstraints());
		System.out.println("======== Solution ========");
		for (int i = 0; i < csp.noVariables(); i++) {
			System.out.println(csp.getVars().get(i).toString());
		}
	}

    /**
	 * Assigns a value to a variable.
	 * 
	 * @param var The variable to be assigned.
	 * @param value The value to be assigned.
	 */
	protected void assign(BinaryVariable var, int value) {
		var.assign(value);
	}

	/**
	 * Unassigns a variable.
	 * 
	 * @param var The variable to be unassigned.
	 */
	protected void unassign(BinaryVariable var) {
		var.unassign();
	}

	/**
	 * Selects a value from the domain of a variable.
	 * 
	 * @param var The variable to select a value from.
	 * @return A value from the domain of the variable.
	 */
	protected int selectVal(BinaryVariable var) {
		return var.domain().iterator().next();
	}

	/**
	 * Selects a variable.
	 * 
	 * @return A variable.
	 */
	protected BinaryVariable selectVar() {
		return variables.peek();
	}

	/**
	 * Restore value to a variable.
	 *
	 * @param var The variable to restore the value to.
	 * @param val The value to restore.
	 */
	protected void restoreValue(BinaryVariable var, int val) {
		var.add(val);
	}

	/**
	 * Delete value from a variable.
	 * 
	 * @param var The variable to delete the value from.
	 * @param val The value to delete.
	 */
	protected void deleteValue(BinaryVariable var, int val) {
		var.remove(val);
	}

    /**
	 * Revise an arc, pruning the domain of the future variable based on the assignment
	 * of the current variable.
	 *
	 * @param arc The arc to be revised.
	 * @return The set of values that were removed from the domain of the future variable.
	 */
	protected Set<Integer> revise(BinaryArc arc) {
		Set<Integer> notSupported = new HashSet<Integer>();
		// If the future variable is assgined, don't revise.
		if (arc.future().assigned()) {
			return notSupported;
		}

		// Find and remove all values in the future domain that are not supported.
		for (int futureVal : arc.future().domain()) {
			if (!arc.isSupported(futureVal)) {
				notSupported.add(futureVal);
			}
		}
		arc.future().remove(notSupported);

		// Increment the arc revision counter if the domain was pruned.
		if (!notSupported.isEmpty()) {
			arcRevisions++;
		}
		return notSupported;
	}

    /**
	 * Undo the pruning of the domains of future variables.
	 */
	protected void undoPruning() {
		Map<BinaryVariable, Set<Integer>> pruned = domainHistory.pop();
		for (BinaryVariable future : pruned.keySet()) {
			for (int val : pruned.get(future)) {
				future.add(val);
			}
		}
	}
}
