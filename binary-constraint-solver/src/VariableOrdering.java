import java.util.Comparator;

public enum VariableOrdering {
    ASCENDING,
    SMALLEST_DOMAIN_FIRST;

	/**
	 * Returns the variable ordering corresponding to the given type.
	 * 
	 * @param type The type of ordering.
	 * @return The variable ordering.
	 */
	public static VariableOrdering getOrdering(int type) {
        switch (type) {
            case 0:
                return VariableOrdering.ASCENDING;
            case 1:
                return VariableOrdering.SMALLEST_DOMAIN_FIRST;
            default:
                throw new IllegalArgumentException("Invalid variable ordering type.");
        }
    }

    /**
     * Returns a string describing the name of the given ordering.
     * 
     * @param ordering The ordering.
     * @return The name of the ordering.
     */
    public static String toString(VariableOrdering ordering) {
        switch (ordering) {
            case ASCENDING:
                return "Ascending";
            case SMALLEST_DOMAIN_FIRST:
                return "Smallest Domain First";
            default:
                throw new IllegalArgumentException("Invalid variable ordering type.");
        }
    }

    /**
     * Comparator for ordering variables by their order. Used for static ordering (ascending order).
     */
    public static final Comparator<BinaryVariable> StaticComparator = new Comparator<BinaryVariable>() {
		@Override
		public int compare(BinaryVariable var1, BinaryVariable var2) {
			return var1.order() - var2.order();
		}
	};

    /** 
     * Comparator for ordering variables by their domain size. Used for smallest domain first.
     */
	public static final Comparator<BinaryVariable> SmallestDomainComparator = new Comparator<BinaryVariable>() {
		@Override
		public int compare(BinaryVariable var1, BinaryVariable var2) {
            if (var1.domain().size() == var2.domain().size()) {
                return var1.order() - var2.order(); // Break ties by order, smallest first.
            }
			return var1.domain().size() - var2.domain().size();
		}
	};
}
