package woodland.entities.creatures;

import woodland.entities.Creature;

/**
 * Represents a complicated centaur, a specific type of creature in the woodland game.
 */
public class ComplicatedCentaur extends Creature {

    private static final int ATTACK = 36;

    /**
     * Creates a complicated centaur with a predefined name, description and attack value.
     */
    public ComplicatedCentaur() {
        super("Complicated Centaur", ATTACK);
        this.shortName = "CC";
        this.description = "The CC is a centaur that has mixed feeling about its love interest, "
            + "a horse. The centaur is unsure whether they can love them fully";
    }
}
