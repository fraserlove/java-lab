import java.util.HashSet;
import java.util.Set;

/**
 * Represents a variable in a binary CSP.
 */
public final class BinaryVariable {
    
    // Order of the variable (for static heuristics).
    private final int order;

    // The domain of the variable.
    private Set<Integer> domain;

    // The value of the variable.
    private Integer value;

    /**
     * Creates a new variable.
     * 
     * @param order Order of the variable.
     * @param lowerBound Lower bound of the domain.
     * @param upperBound Upper bound of the domain.
     */
    public BinaryVariable(int order, int lowerBound, int upperBound) {
        this.order = order;
        this.value = null;

        // Initialize the domain.
        domain = new HashSet<Integer>();
        for (int i = lowerBound; i <= upperBound; i++) {
			domain.add(i);
		}
    }

    /**
     * Gets the order of the variable.
     * 
     * @return Order of the variable.
     */
    public int order() {
		return order;
	}

    /**
     * Gets the domain of the variable.
     * 
     * @return Domain of the variable.
     */
    public Set<Integer> domain() {
        return domain;
    }

    /**
     * Gets the value of the variable.
     * 
     * @return Value of the variable.
     */
    public Integer value() {
        return value;
    }

    /**
     * Assigns a value to the variable.
     * 
     * @param value Value to be assigned.
     */
    public void assign(int value) {
        this.value = value;
    }

    /**
     * Unassigns the variable.
     */
    public void unassign() {
        this.value = null;
	}

    /**
     * Adds a set of values to the domain of the variable.
     * 
     * @param value Set of values to be added.
     */
    public void add(int value) {
		domain.add(value);
	}

    /**
     * Removes a set of values from the domain of the variable.
     * 
     * @param values Set of values to be removed.
     */
    public void remove(Set<Integer> values) {
		domain.removeAll(values);
	}

    /**
     * Removes a value from the domain of the variable.
     * 
     * @param val Value to be removed.
     */
    public void remove(int val) {
		domain.remove(val);
	}

    /**
     * Checks if the variable is assigned.
     * 
     * @return True if the variable is assigned, false otherwise.
     */
    public boolean assigned() {
        return value != null;
    }
    
    /**
     * Returns a string representation of the variable. If the variable is assigned,
     * the value is printed. Otherwise, the domain is printed.
     * 
     * @return String representation of the variable.
     */
    public String toString() {
        return "Var " + order + ": " + (value == null ? domain.toString() : value.toString());
    }
}
