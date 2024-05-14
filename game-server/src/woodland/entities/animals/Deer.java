package woodland.entities.animals;

import woodland.Square;
import woodland.entities.Animal;
import woodland.entities.animals.attributes.LongJumpable;

/**
 * Represents a deer, a specific type of animal in the woodland game.
 * Deer have the unique ability to jump.
 */
public class Deer extends Animal implements LongJumpable {

    /**
     * Creates a deer with a predefined name and description.
     */
    public Deer() {
        super("Deer");
        this.description = "The deer has antlers. The deer is recently divorced "
            + "and is looking for a new partner.";
    }

    /**
     * Attempts to move the deer from its current position to a new position.
     * The deer can move one square in any direction or jump three squares in any direction.
     * 
     * @param oldRow The current row position of the deer.
     * @param oldCol The current column position of the deer.
     * @param newRow The row position to move the deer to.
     * @param newCol The column position to move the deer to.
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
     * Allows the deer to jump to a new position that is three squares away.
     * 
     * @param oldRow The current row position of the deer.
     * @param oldCol The current column position of the deer.
     * @param newRow The row position to jump the deer to.
     * @param newCol The column position to jump the deer to.
     * @return The success status of the jump operation.
     */
    public boolean jump(int oldRow, int oldCol, int newRow, int newCol) {
        int rowDiff = Math.abs(newRow - oldRow);
        int colDiff = Math.abs(newCol - oldCol);

        boolean moveThreeSquaresAny = ((rowDiff <= JUMP_SIZE && colDiff == 0) || (rowDiff == 0 && colDiff <= JUMP_SIZE) || (rowDiff == colDiff && rowDiff <= JUMP_SIZE));
        return moveThreeSquaresAny;
    }

    /**
     * Deer can jump over creatures and so cannot be interrupted by a creature, therefore by default
     * we return null. Deer can see what they jump over, so instead reveal any creatures they jump over.
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
        int rowDiff = Integer.compare(newRow, oldRow);
        int colDiff = Integer.compare(newCol, oldCol);
        // Calculate the total difference to move.
        int diff = Math.max(Math.abs(newRow - oldRow), Math.abs(newCol - oldCol));

        // Look for creatures along the whole line.
        Square interSquare;
        int interRow, interCol;
        for (int i = 1; i < diff; i++) {
            interRow = oldRow + i * rowDiff;
            interCol = oldCol + i * colDiff;
            interSquare = board[interRow][interCol];
            if (interSquare.hasCreature()) {
                interSquare.reveal();
            }
        }
        return null;
    }

    /**
     * Deer can jump over other animals and so cannot be interrupted by another animal, therefore, by default
     * we return false.
     * 
     * @param board The game board.
     * @param oldRow The current row position of the animal.
     * @param oldCol The current column position of the animal.
     * @param newRow The row position to move the animal to.
     * @param newCol The column position to move the animal to.
     * @return A boolean describing if the move is interrupted by another animal.
     */
    @Override
    public boolean animalInterruption(Square[][] board, int oldRow, int oldCol, int newRow, int newCol) {
        return false;
    }
}
