package woodland.entities.creatures;

import woodland.entities.Creature;

/**
 * Represents a under-appreciated unicorn, a specific type of creature in the woodland game.
 */
public class UnderAppreciatedUnicorn extends Creature {

    private static final int ATTACK = 14;

    /**
     * Creates a under-appreciated unicorn with a predefined name, description and attack value.
     */
    public UnderAppreciatedUnicorn() {
        super("Under-appreciated Unicorn", ATTACK);
        this.shortName = "UU";
        this.description = "The UU is a unicorn that is under-appreciated by the "
            + "other mythical creatures because it is often mistaken for a horse with a horn.";
    }
}
