package woodland.entities.animals.attributes;

/**
 * Interface representing the ability for animals to jump from one square to another.
 */
public interface Jumpable {

    /**
     * Attempts to jump to a new location on the game board.
     * 
     * @param oldRow The current row position of the animal.
     * @param oldCol The current column position of the animal.
     * @param newRow The row position to jump the animal to.
     * @param newCol The column position to jump the animal to.
     * @return The success status of the jump operation.
     */
    boolean jump(int oldRow, int oldCol, int newRow, int newCol);
}
