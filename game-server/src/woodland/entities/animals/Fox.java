package woodland.entities.animals;

import woodland.entities.Animal;
import woodland.entities.animals.attributes.LongJumpable;

/**
 * Represents a fox, a specific type of animal in the woodland game.
 * Foxes have the unique ability to jump.
 */
public class Fox extends Animal implements LongJumpable {

    /**
     * Creates a fox with a predefined name and description.
     */
    public Fox() {
        super("Fox");
        this.description = "The fox has a bushy tail. The fox really enjoys looking "
            + "at butterflies in the sunlight.";
    }

    /**
     * Attempts to move the fox from its current position to a new position.
     * The fox can move one square in any straight direction or jump three squares in any straight direction.
     * 
     * @param oldRow The current row position of the fox.
     * @param oldCol The current column position of the fox.
     * @param newRow The row position to move the fox to.
     * @param newCol The column position to move the fox to.
     * @return The success status of the move operation.
     */
    @Override
    public boolean move(int oldRow, int oldCol, int newRow, int newCol) {
        int rowDiff = Math.abs(newRow - oldRow);
        int colDiff = Math.abs(newCol - oldCol);

        boolean moveOneSquareStraight = ((rowDiff <= 1 && colDiff == 0) || (rowDiff == 0 && colDiff <= 1));
        return moveOneSquareStraight || jump(oldRow, oldCol, newRow, newCol);
    }

    /**
     * Allows the fox to jump to a new position that is three squares away in any straight direction.
     * 
     * @param oldRow The current row position of the fox.
     * @param oldCol The current column position of the fox.
     * @param newRow The row position to jump the fox to.
     * @param newCol The column position to jump the fox to.
     * @return The success status of the jump operation.
     */
    public boolean jump(int oldRow, int oldCol, int newRow, int newCol) {
        int rowDiff = Math.abs(newRow - oldRow);
        int colDiff = Math.abs(newCol - oldCol);

        boolean moveThreeSquaresStraight = ((rowDiff <= JUMP_SIZE && colDiff == 0) || (rowDiff == 0 && colDiff <= JUMP_SIZE));
        return moveThreeSquaresStraight;
    }
}
