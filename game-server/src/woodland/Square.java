package woodland;

import woodland.entities.Animal;
import woodland.entities.Creature;
import woodland.entities.Spell;

/**
 * Represents a square on the woodland game board.
 * Each square can contain an animal, a creature, and a spell.
 */
public class Square {

    // Position of this square on the board.
    protected final int row;
    protected final int col;

    // Visibility status of this square.
    protected boolean visible;

    // The animal, creature and spell occupying this square, null if none.
    private Animal animal;
    private Creature creature;
    private Spell spell;

    /**
     * Creates a new square at the specified coordinates.
     *
     * @param row The row number of this square.
     * @param col The column number of this square.
     */
    public Square(int row, int col) {
        this.row = row;
        this.col = col;
        this.visible = false;
    }

    /**
     * Gets the row number of this square.
     * 
     * @return The row number of this square.
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Gets the column number of this square.
     * 
     * @return The column number of this square.
     */
    public int getCol() {
        return this.col;
    }

    /**
     * Checks if the square is visible.
     * 
     * @return A boolean describing if the square is visible.
     */
    public boolean isVisible() {
        return this.visible;
    }

    /**
     * Sets this square as visible.
     */
    public void reveal() {
        this.visible = true;
    }

    /**
     * Check if an animal occupies this square.
     * 
     * @return A boolean describing if the an animal occupies this square.
     */
    public boolean hasAnimal() {
        return this.animal != null;
    }

    /**
     * Check if a creature occupies this square.
     * 
     * @return A boolean describing if a creature occupies this square.
     */
    public boolean hasCreature() {
        return this.creature != null;
    }

    /**
     * Check if a spell occupies this square.
     * 
     * @return A boolean describing if a spell occupies this square.
     */
    public boolean hasSpell() {
        return this.spell != null;
    }

    /**
     * Check if any animal, creature or spell occupies this square.
     * 
     * @return A boolean describing if this square is occupied by an animal, creature of spell.
     */
    public boolean isOccupied() {
        return this.hasAnimal() || this.hasCreature() || this.hasSpell();
    }

    /**
     * Sets the animal occupying this square and reveals the square to the animal.
     * If any creatures then exist on this square, the animal will see them.
     * If the animal is not null, its square is also updated.
     *
     * @param animal The animal to occupy this square.
     */
    public void setAnimal(Animal animal) {
        this.animal = animal;
        if (this.animal != null) {
            this.animal.setSquare(this);
            this.reveal();
        }
    }

    /**
     * Sets the creature occupying this square.
     *
     * @param creature The creature to occupy this square.
     */
    public void setCreature(Creature creature) {
        this.creature = creature;
    }

    /**
     * Sets the spell occupying this square.
     *
     * @param spell The spell to occupy this square.
     */
    public void setSpell(Spell spell) {
        this.spell = spell;
    }

    /**
     * Gets the animal occupying this square.
     * 
     * @return The animal occupying this square, or null if it's empty.
     */
    public Animal getAnimal() {
        return this.animal;
    }

    /**
     * Gets the creature occupying this square.
     * 
     * @return The creature occupying this square, or null if it's empty.
     */
    public Creature getCreature() {
        return this.creature;
    }

    /**
     * Gets the spell occupying this square.
     * 
     * @return The spell occupying this square, or null if it's empty.
     */
    public Spell getSpell() {
        return this.spell;
    }
}
