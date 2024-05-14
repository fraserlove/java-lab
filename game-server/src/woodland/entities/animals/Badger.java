package woodland.entities.animals;

import woodland.Square;
import woodland.entities.Animal;
import woodland.entities.animals.attributes.Digable;

/**
 * Represents a badger, a specific type of animal in the woodland game.
 * Badgers have the unique ability to dig.
 */
public class Badger extends Animal implements Digable {

    /**
     * Creates a badger with a predefined name and description.
     */
    public Badger() {
        super("Badger");
        this.description = "The badger has a black and white face. The badger is a often"
            + "mistaken for a very small panda. The badger wears a t-shirt that says “I am not a panda”"
            + "to combat this.";
    }

    /**
     * Attempts to move the Badger from its current position to a new position.
     * The badger can move one square in any direction or dig exactly two squares in any direction.
     * 
     * @param oldRow The current row position of the badger.
     * @param oldCol The current column position of the badger.
     * @param newRow The row position to move the badger to.
     * @param newCol The column position to move the badger to.
     * @return The success status of the move operation.
     */
    @Override
    public boolean move(int oldRow, int oldCol, int newRow, int newCol) {
        int rowDiff = Math.abs(newRow - oldRow);
        int colDiff = Math.abs(newCol - oldCol);

        boolean moveOneSquareAny = (rowDiff <= 1 && colDiff <= 1);
        return moveOneSquareAny || dig(oldRow, oldCol, newRow, newCol);
    }

    /**
     * Allows the badger to dig to a new position that is exactly two squares away.
     * 
     * @param oldRow The current row position of the badger.
     * @param oldCol The current column position of the badger.
     * @param newRow The row position to dig the badger to.
     * @param newCol The column position to dig the badger to.
     * @return The success status of the dig operation.
     */
    public boolean dig(int oldRow, int oldCol, int newRow, int newCol) {
        int rowDiff = Math.abs(newRow - oldRow);
        int colDiff = Math.abs(newCol - oldCol);

        boolean moveExactlyTwoSquaresAny = ((rowDiff == 2 && colDiff == 0) || (rowDiff == 0 && colDiff == 2) || (rowDiff == colDiff && rowDiff == 2));
        return moveExactlyTwoSquaresAny;
    }

    /**
     * Badgers dig under creatures and so cannot be interrupted by a creature, therefore by default
     * we return null.
     * 
     * @param board The game board.
     * @param oldRow The current row position of the animal.
     * @param oldCol The current column position of the animal.
     * @param newRow The row position to move the animal to.
     * @param newCol The column position to move the animal to.
     * @return The square of the creature interrupting the move.
     */
    @Override
    public Square creatureInterruption(Square[][] board, int oldRow, int oldCol, int newRow, int newCol) {
        return null;
    }

    /**
     * Badgers dig under animals and so cannot be interrupted by an animal, therefore by default
     * we return false.
     * 
     * @param board The game board.
     * @param oldRow The current row position of the animal.
     * @param oldCol The current column position of the animal.
     * @param newRow The row position to move the animal to.
     * @param newCol The column position to move the animal to.
     * @return A boolean describing if the move is interrupted by another animal.
     */
    public boolean animalInterruption(Square[][] board, int oldRow, int oldCol, int newRow, int newCol) {
        return false;
    }
}
