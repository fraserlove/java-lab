package woodland.entities.creatures;

import woodland.entities.Creature;

/**
 * Represents a sassy sphinx, a specific type of creature in the woodland game.
 */
public class SassySphinx extends Creature {

    private static final int ATTACK = 21;

    /**
     * Creates a sassy sphinx with a predefined name, description and attack value.
     */
    public SassySphinx() {
        super("Sassy Sphinx", ATTACK);
        this.shortName = "SS";
        this.description = "The SS is a sphinx that is very sassy. The sphinx is "
            + "very good at giving sarcastic answers to questions.";
    }
}
