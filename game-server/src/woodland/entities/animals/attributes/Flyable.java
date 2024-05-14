package woodland.entities.animals.attributes;

/**
 * Interface representing the ability for animals to fly from one square to another.
 */
public interface Flyable {

    /**
     * Attempts to fly to a new location on the game board.
     * 
     * @param oldRow The current row position of the animal.
     * @param oldCol The current column position of the animal.
     * @param newRow The row position to fly the animal to.
     * @param newCol The column position to fly the animal to.
     * @return The success status of the fly operation.
     */
    boolean fly(int oldRow, int oldCol, int newRow, int newCol);
}
