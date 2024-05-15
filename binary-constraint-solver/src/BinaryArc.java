import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a binary arc. It stores the current and future variables and
 * the set of allowed tuples.
 */
public final class BinaryArc {

    // The current and future variables.
    private final BinaryVariable current, future;

    // The set of allowed tuples.
    private final Set<BinaryTuple> tuples;

    /**
     * Creates a new binary arc.
     * 
     * @param current Current variable.
     * @param future Future variable.
     * @param tuples Set of allowed tuples.
     */
    public BinaryArc(BinaryVariable current, BinaryVariable future, Set<BinaryTuple> tuples) {
        this.tuples = tuples;
        this.current = current;
        this.future = future;
    }

    /**
     * Gets the current variable.
     *
     * @return the current variable
     */
    public BinaryVariable current() {
        return current;
    }

    /**
     * Gets the future variable.
     *
     * @return the future variable
     */
    public BinaryVariable future() {
        return future;
    }

    /**
     * Reverses the arc so (x, y) becomes (y, x).
     *
     * @return The reversed arc.
     */
    public BinaryArc reverse() {
        Set<BinaryTuple> reversed = new HashSet<BinaryTuple>();
        for (BinaryTuple tuple : tuples) {
            reversed.add(tuple.reverse());
        }
        return new BinaryArc(future, current, reversed);
    }

    /**
     * Checks if the value is supported in the future domain of this arc. The value is
     * considered supported if it forms a valid tuple with the current variable's value,
     * if it is assigned, or with any value in the current variable's domain.
     *
     * @param value Value to be tested.
     * @return True if the value is supported, false otherwise.
     */
    public boolean isSupported(int value) {
        for (int currentVal : current.assigned() ? Collections.singleton(current.value()) : current.domain()) {
            if(tuples.contains(new BinaryTuple(currentVal, value))) {
				return true;
			}
        }
        return false; // No supported tuple found.
    }

    /**
     * Checks if this arc is consistent. An arc is consistent if at least one tuple
     * in the set of allowed tuples is consistent with the current assignment of
     * the current and future variables.
     *
     * @return True if the arc is consistent, false otherwise.
     */
    public boolean isConsistent() {
        // Iterate through each tuple to check for consistency.
        for (BinaryTuple tuple : tuples) {
            if (tuple.isConsistent(current, future)) {
                return true;
            }
        }
        return false; // No consistent tuple found.
    }
    
    /**
     * Returns a string representation of the arc.
     * 
     * @return String representation of the arc.
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(current + "->" + future + "\n");
        for (BinaryTuple tuple : tuples) {
            result.append(tuple + "\n");
        }
        return result.toString();
    }

    /**
     * Computes the hash code of the arc.
     * 
     * @return Hash code of the arc.
     */
    @Override
    public int hashCode() {
        return Objects.hash(current, future, tuples);
    }

    /**
     * Checks if two arcs are equal. They are equal if they have the same
     * current and future variables and the same set of allowed tuples.
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
        BinaryArc other = (BinaryArc) obj;
        return Objects.equals(current, other.current)
            && Objects.equals(future, other.future)
            && Objects.equals(tuples, other.tuples);
    }
}

