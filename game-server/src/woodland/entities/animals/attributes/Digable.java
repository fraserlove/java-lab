package woodland.entities.animals.attributes;

/**
 * Interface representing the ability for animals to dig from one square to another.
 */
public interface Digable {

    /**
     * Attempts to dig to a new location on the game board.
     * 
     * @param oldRow The current row position of the animal.
     * @param oldCol The current column position of the animal.
     * @param newRow The row position to dig the animal to.
     * @param newCol The column position to dig the animal to.
     * @return The success status of the dig operation.
     */
    boolean dig(int oldRow, int oldCol, int newRow, int newCol);
}
