import java.util.Objects;

/**
 * Represents an allowed tuple of values in a binary constraint.
 */
public final class BinaryTuple {
    
	// The two values in the tuple.
    private final int val1, val2;

	/**
	 * Creates a new tuple of values.
	 * 
	 * @param val1 First value in the tuple.
	 * @param val2 Second value in the tuple.
	 */
    public BinaryTuple(int val1, int val2) {
        this.val1 = val1;
        this.val2 = val2;
    }

	/**
	 * Gets the first value in the tuple.
	 * 
	 * @return First value in the tuple.
	 */
    public int val1() {
        return val1;
    }

	/**
	 * Gets the second value in the tuple.
	 * 
	 * @return Second value in the tuple.
	 */
    public int val2() {
        return val2;
    }

	/**
	 * Reverses the tuple so (x, y) becomes (y, x).
	 * 
	 * @return The reversed tuple.
	 */
	public BinaryTuple reverse() {
		return new BinaryTuple(val2, val1);
	}

    /**
     * Checks if the tuple is consistent with two variables in an arc.
     * 
     * A tuple is considered consistent if both variables are assigned and the tuple's
     * values match their assigned values, or if either or both variables are unassigned
     * and the tuple's corresponding values are within their respective domains.
     * 
     * @param var1 The first variable.
     * @param var2 The second variable.
     * @return True if the tuple is consistent, false otherwise.
     */
    public boolean isConsistent(BinaryVariable var1, BinaryVariable var2) {
        boolean currentConsistent = var1.assigned() ? val1 == var1.value() : var1.domain().contains(val1);
        boolean futureConsistent = var2.assigned() ? val2 == var2.value() : var2.domain().contains(val2);

        if (currentConsistent && futureConsistent) {
            return true;
        }
        return false;
    }

	/**
     * Returns a string representation of the tuple.
     * 
     * @return String representation of the tuple.
     */
    public String toString() {
        return "<" + val1 + ", " + val2 + ">";
    }

    /**
     * Computes the hash code of the tuple.
     * 
     * @return Hash code of the tuple.
     */
    @Override
	public int hashCode() {
		return Objects.hash(val1, val2);
	}

	/**
     * Checks if two tuples are equal. They are equal if they have the same values.
     * 
     * @param obj Object to be compared.
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

		BinaryTuple other = (BinaryTuple) obj;
        return Objects.equals(val1, other.val1)
            && Objects.equals(val2, other.val2);
	}
}
