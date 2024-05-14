package woodland.entities.creatures;

import woodland.entities.Creature;

/**
 * Represents a deceptive dragon, a specific type of creature in the woodland game.
 */
public class DeceptiveDragon extends Creature {

    private static final int ATTACK = 29;

    /**
     * Creates a deceptive dragon with a predefined name, description and attack value.
     */
    public DeceptiveDragon() {
        super("Deceptive Dragon", ATTACK);
        this.shortName = "DD";
        this.description = "The DD is a dragon that practices social engineering. The "
            + "dragon is very good at sending phishing emails pretending to be a prince.";
    }
}
