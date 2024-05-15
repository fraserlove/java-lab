import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Represents an entire CSP. It stores a list of binary variables and a map of binary arcs.
 */
public final class BinaryCSP {

	// The list of variables.
	private final List<BinaryVariable> variables;

	// The map of binary arcs.
	private final Map<BinaryVariable, Map<BinaryVariable, BinaryArc>> arcs;

	/**
	 * Creates a new binary CSP.
	 *
	 * @param variables The list of variables.
	 * @param arcs The set of binary arcs.
	 */
	public BinaryCSP(BinaryVariable[] variables, Map<BinaryVariable, Map<BinaryVariable, BinaryArc>> arcs) {
		this.variables = new ArrayList<BinaryVariable>(Arrays.asList(variables));
		this.arcs = arcs;
	}

	/**
	 * Gets the number of variables.
	 *
	 * @return the number of variables in the problem
	 */
	public int noVariables() {
		return variables.size();
	}

	/**
	 * Gets the list of variables.
	 *
	 * @return The list of variables.
	 */
	public List<BinaryVariable> getVars() {
		return variables;
	}

	/**
	 * Gets the variable at the specified index.
	 *
	 * @param i The index of the variable.
	 * @return The variable at the specified index.
	 */
	public BinaryVariable getVar(int i) {
		return variables.get(i);
	}

	/**
	 * Gets the number of constraints.
	 *
	 * @return The number of constraints.
	 */
	public int noConstraints() {
		int i = 0;
		for (BinaryVariable var : arcs.keySet()) {
			i += arcs.get(var).values().size();
		}
		return i / 2; // Each constraint is counted twice
	}

	/**
	 * Checks if the problem is consistent with a variable.
	 *
	 * @param var The variable to check consistency for.
	 * @return True, if the problem is consistent with the variable.
	 */
	public boolean isConsistent(BinaryVariable current) {
		for (BinaryVariable future: arcs.get(current).keySet()) {
			BinaryArc arc = arcs.get(current).get(future);
			if (!arc.isConsistent()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the problem is consistent (all arcs are consistent).
	 *
	 * @return True, if the problem is consistent.
	 */
	public boolean isConsistent() {
		for (BinaryVariable current : arcs.keySet()) {
			for (BinaryVariable future : arcs.get(current).keySet()) {
				BinaryArc arc = arcs.get(current).get(future);
				if (!arc.isConsistent()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks whether every variable in the problem is assigned.
	 *
	 * @return True, if every variable is assigned.
	 */
	public boolean completeAssignment() {
		for (BinaryVariable var : variables) {
			if (!var.assigned()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets all the arcs in a queue.
	 *
	 * @return The arcs in a queue.
	 */
	public Queue<BinaryArc> queueArcs() {
		Queue<BinaryArc> queue = new LinkedList<BinaryArc>();
		for (BinaryVariable current : arcs.keySet()) {
			for (BinaryVariable future : arcs.get(current).keySet()) {
				queue.add(arcs.get(current).get(future));
			}
		}
		return queue;
	}

	/**
	 * Gets the future arcs from the provided variable.
	 *
	 * @param var The variable to get all the future arcs from.
	 * @return The outgoing arcs.
	 */
	public Collection<BinaryArc> futureArcs(BinaryVariable var) {
		List<BinaryArc> futureArcs = new ArrayList<BinaryArc>();
		if (arcs.containsKey(var)) {
			futureArcs.addAll(arcs.get(var).values());
		}
		return futureArcs;
	}

	/**
     * Returns a string representation of the CSP.
     * 
     * @return String representation of the CSP.
     */
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("CSP:\n");
		for (int i = 0; i < variables.size(); i++)
			result.append(variables.get(i).toString() + "\n");
		for (Map<BinaryVariable, BinaryArc> map : arcs.values()) {
			for (BinaryArc arc : map.values())
				result.append(arc + "\n");
		}
		return result.toString();
	}

}
