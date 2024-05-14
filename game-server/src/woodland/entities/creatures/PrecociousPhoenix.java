package woodland.entities.creatures;

import woodland.entities.Creature;

/**
 * Represents a precocious phoenix, a specific type of creature in the woodland game.
 */
public class PrecociousPhoenix extends Creature {

    private static final int ATTACK = 42;

    /**
     * Creates a precocious phoenix with a predefined name, description and attack value.
     */
    public PrecociousPhoenix() {
        super("Precocious Phoenix", ATTACK);
        this.shortName = "PP";
        this.description = "The PP is a phoenix that is very precocious. The phoenix "
            + "understands the meaning of life and the universe.";
    }
}
