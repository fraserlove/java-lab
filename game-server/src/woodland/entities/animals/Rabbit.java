package woodland.entities.animals;

import woodland.entities.Animal;
import woodland.entities.animals.attributes.ShortJumpable;

/**
 * Represents a rabbit, a specific type of animal in the woodland game.
 * Rabbits have the unique ability to jump.
 */
public class Rabbit extends Animal implements ShortJumpable {

    /**
     * Creates a rabbit with a predefined name and description.
     */
    public Rabbit() {
        super("Rabbit");
        this.description = "The rabbit has fluffy ears and tail. The rabbit really "
            + "likes to eat grass.";
    }

    /**
     * Attempts to move the rabbit from its current position to a new position.
     * The rabbit can move one square in any direction or jump two squares in any direction.
     * 
     * @param oldRow The current row position of the rabbit.
     * @param oldCol The current column position of the rabbit.
     * @param newRow The row position to move the rabbit to.
     * @param newCol The column position to move the rabbit to.
     * @return The success status of the move operation.
     */
    @Override
    public boolean move(int oldRow, int oldCol, int newRow, int newCol) {
        int rowDiff = Math.abs(newRow - oldRow);
        int colDiff = Math.abs(newCol - oldCol);

        boolean moveOneSquareAny = (rowDiff <= 1 && colDiff <= 1);
        return moveOneSquareAny || jump(oldRow, oldCol, newRow, newCol);
    }

    /**
     * Allows the rabbit to jump to a new position that is two squares away.
     * 
     * @param oldRow The current row position of the rabbit.
     * @param oldCol The current column position of the rabbit.
     * @param newRow The row position to jump the rabbit to.
     * @param newCol The column position to jump the rabbit to.
     * @return The success status of the jump operation.
     */
    public boolean jump(int oldRow, int oldCol, int newRow, int newCol) {
        int rowDiff = Math.abs(newRow - oldRow);
        int colDiff = Math.abs(newCol - oldCol);

        boolean moveTwoSquaresAny = ((rowDiff <= JUMP_SIZE && colDiff == 0) || (rowDiff == 0 && colDiff <= JUMP_SIZE) || (rowDiff == colDiff && rowDiff <= JUMP_SIZE));
        return moveTwoSquaresAny;
    }
}
