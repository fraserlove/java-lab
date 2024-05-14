package woodland.entities;

import woodland.Square;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 * Abstract base class representing an animal in the woodland game.
 * Animals have the ability to move and cast spells.
 */
public abstract class Animal {

    // Maximum health of each animal.
    private static final int MAX_LIFE_POINTS = 100;

    // Amount of health to regenerate when healing.
    private static final int HEAL_AMOUNT = 10;

    // Information about the animal.
    protected String name;
    protected String description;
    protected int lifePoints;

    // A map storing the type and number of spells this animal has picked up.
    protected Map<Spell, Integer> spells;

    // Current square the animal is occupying.
    private Square square;

    /**
     * Creates an animal with the specified name.
     * 
     * @param name The name of the creature.
     */
    public Animal(String name) {
        this.name = name;
        this.lifePoints = MAX_LIFE_POINTS;
        this.spells = new HashMap<>();
    }

    /**
     * Abstract method to move the animal on the game board. Each animal overrides
     * this method and has their own implementation of movement.
     *
     * @param oldRow The current row position of the animal.
     * @param oldCol The current column position of the animal.
     * @param newRow The row position to move the animal to.
     * @param newCol The column position to move the animal to.
     * @return The success status of the move operation.
     */
    public abstract boolean move(int oldRow, int oldCol, int newRow, int newCol);

    /**
     * Checks if the animals move will be interrupted by a creature and returns the square
     * of the creature if true, otherwise returns null.
     * 
     * @param board The game board.
     * @param oldRow The current row position of the animal.
     * @param oldCol The current column position of the animal.
     * @param newRow The row position to move the animal to.
     * @param newCol The column position to move the animal to.
     * @return The square of the creature interrupting the move.
     */
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
                return interSquare;
            }
        }
        return null;
    }

    /**
     * Checks if the animals move will be interrupted by another animal.
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

        // Look for animals along the whole line.
        Square interSquare;
        int interRow, interCol;
        for (int i = 1; i < diff; i++) {
            interRow = oldRow + i * rowDiff;
            interCol = oldCol + i * colDiff;
            interSquare = board[interRow][interCol];
            if (interSquare.hasAnimal()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets this animal's name.
     * 
     * @return The name of this animal.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the current square this animal is occupying.
     * 
     * @return The current square on which the animal is occupying.
     */
    public Square getSquare() {
        return this.square;
    }

    /**
     * Sets the animal's current square.
     *
     * @param square The square to set as the animal's current location.
     */
    public void setSquare(Square square) {
        this.square = square;
    }

    /**
     * Heals the animal, increasing its life points by 10, up to its maximum health.
     */
    public void heal() {
        this.lifePoints = Math.min(MAX_LIFE_POINTS, this.lifePoints + HEAL_AMOUNT);
    }

    /**
     * Shield this animal from a creature located on the same square as this animal, if one exists.
     */
    public void shield() {
        if (this.getSquare().hasCreature()) {
            this.getSquare().getCreature().addShieldAnimal(this);
        }
    }

    /**
     * Reveals any hidden creatures in adjacent squares around this animal.
     *
     * @param board The game board.
     */
    public void detect(Square[][] board) {
        int row = this.getSquare().getRow();
        int col = this.getSquare().getCol();
        for (int i = Math.max(0, row - 1); i <= Math.min(board.length - 1, row + 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(board[0].length - 1, col + 1); j++) {
                board[i][j].reveal();
            }
        }
    }

    /**
     * Confuses creatures in adjacent squares.
     *
     * @param board The game board.
     */
    public void confuse(Square[][] board) {
        int row = this.getSquare().getRow();
        int col = this.getSquare().getCol();
        for (int i = Math.max(0, row - 1); i <= Math.min(board.length - 1, row + 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(board[0].length - 1, col + 1); j++) {
                // Cannot confuse a creature on the square the animal is occupying.
                if (i != row || j != col) {
                    if (board[i][j].hasCreature()) {
                        board[i][j].getCreature().setConfused(true);
                    }
                }
            }
        }
    }

    /**
     * Charms creatures in adjacent squares.
     *
     * @param board The game board.
     */
    public void charm(Square[][] board) {
        int row = this.getSquare().getRow();
        int col = this.getSquare().getCol();
        for (int i = Math.max(0, row - 1); i <= Math.min(board.length - 1, row + 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(board[0].length - 1, col + 1); j++) {
                // Cannot charm a creature on the square the animal is occupying.
                if (i != row || j != col) {
                    if (board[i][j].hasCreature()) {
                        board[i][j].getCreature().addCharmAnimal(this);
                    }
                }
            }
        }
    }

    /**
     * Applies an attack to this animal, reducing its life points.
     *
     * @param attackValue The value by which to reduce the life points.
     */
    public void attacked(int attackValue) {
        this.lifePoints = Math.max(0, this.lifePoints - attackValue);
    }

    /**
     * Checks if this animal is still alive.
     * 
     * @return Current living status of this animal.
     */
    public boolean isAlive() {
        return this.lifePoints > 0;
    }

    /**
     * Checks if this animal has reached the end of the board.
     * 
     * @return Current finishing status of this animal.
     */
    public boolean hasFinished() {
        return this.getSquare().getRow() == 0;
    }

    /**
     * Adds a spell to this animal's collection and removes the spell from the square.
     *
     * @param spell The spell to add.
     */
    public void addSpell(Spell spell) {
        this.spells.put(spell, this.spells.getOrDefault(spell, 0) + 1);
        this.square.setSpell(null);
    }

    /**
     * Updates the quantity of a spell in the animal's collection, decrementing the count.
     *
     * @param spell The spell to update.
     */
    public void updateSpell(Spell spell) {
        this.spells.put(spell, this.spells.getOrDefault(spell, 1) - 1);
    }

    /**
     * Converts this state of this animal to a JSON object.
     *
     * @return A JsonObject representing the current state of this animal.
     */
    public JsonObject toJson() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        // Building JsonArray of spells this animal carries.
        for (Map.Entry<Spell, Integer> entry : spells.entrySet()) {
            if (entry.getValue() > 0) {
                arrayBuilder.add(Json.createObjectBuilder()
                    .add("name", entry.getKey().getName())
                    .add("description", entry.getKey().getDescription())
                    .add("amount", entry.getValue())
                    .build());
            }
        }
        return Json.createObjectBuilder()
                .add("name", name)
                .add("type", "Animal")
                .add("description", description)
                .add("life", lifePoints)
                .add("spells", arrayBuilder.build())
                .build();
    }
}
