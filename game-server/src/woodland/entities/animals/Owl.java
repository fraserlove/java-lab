package woodland.entities.animals;

import woodland.Square;
import woodland.entities.Animal;
import woodland.entities.animals.attributes.Flyable;

/**
 * Represents a owl, a specific type of animal in the woodland game.
 * Owls have the unique ability to fly.
 */
public class Owl extends Animal implements Flyable {

    /**
     * Creates a owl with a predefined name and description.
     */
    public Owl() {
        super("Owl");
        this.description = "The owl has wings. The owl has prescription contact lenses "
            + "but cannot put them on.";
    }

    /**
     * Attempts to move the owl from its current position to a new position.
     * The owl can move one square in any direction or fly any number of squares away in any direction.
     * 
     * @param oldRow The current row position of the owl.
     * @param oldCol The current column position of the owl.
     * @param newRow The row position to move the owl to.
     * @param newCol The column position to move the owl to.
     * @return The success status of the move operation.
     */
    @Override
    public boolean move(int oldRow, int oldCol, int newRow, int newCol) {
        int rowDiff = Math.abs(newRow - oldRow);
        int colDiff = Math.abs(newCol - oldCol);

        boolean moveOneSquareAny = (rowDiff <= 1 && colDiff <= 1);
        return moveOneSquareAny || fly(oldRow, oldCol, newRow, newCol);
    }

    /**
     * Allows the owl to fly to a new position that is any number of squares away.
     * 
     * @param oldRow The current row position of the owl.
     * @param oldCol The current column position of the owl.
     * @param newRow The row position to fly the owl to.
     * @param newCol The column position to fly the owl to.
     * @return The success status of the fly operation.
     */
    public boolean fly(int oldRow, int oldCol, int newRow, int newCol) {
        int rowDiff = Math.abs(newRow - oldRow);
        int colDiff = Math.abs(newCol - oldCol);

        boolean moveAnySquaresAny = ((rowDiff >= 0 && colDiff == 0) || (rowDiff == 0 && colDiff >= 0) || (rowDiff == colDiff && rowDiff >= 0));
        return moveAnySquaresAny;
    }

    /**
     * Checks if the owls move will be interrupted by an animal and creature on the same square.
     * 
     * @param board The game board.
     * @param oldRow The current row position of the animal.
     * @param oldCol The current column position of the animal.
     * @param newRow The row position to move the animal to.
     * @param newCol The column position to move the animal to.
     * @return A boolean describing if the move is interrupted by another animal.
     */
    public boolean animalInterruption(Square[][] board, int oldRow, int oldCol, int newRow, int newCol) {
        int rowDiff = Integer.compare(newRow, oldRow);
        int colDiff = Integer.compare(newCol, oldCol);
        // Calculate the total difference to move.
        int diff = Math.max(Math.abs(newRow - oldRow), Math.abs(newCol - oldCol));

        // Look for animal/creature squares along the whole line.
        Square interSquare;
        int interRow, interCol;
        for (int i = 1; i < diff; i++) {
            interRow = oldRow + i * rowDiff;
            interCol = oldCol + i * colDiff;
            interSquare = board[interRow][interCol];
            if (interSquare.hasAnimal() && interSquare.hasCreature()) {
                return true;
            }
        }
        return false;
    }
}
