package woodland.entities;

/**
 * Enumerates the spells available to animals within the woodland game.
 * Each spell has a specific effect such as detecting creatures, healing,
 * providing protection, or affecting creature behavior.
 */
public enum Spell {
    /** Detect spell. */
    DETECT("Detect", "The detect spell allows the animal to detect the mythical "
        + "creatures on the adjacent squares."),
    /** Heal spell. */
    HEAL("Heal", "The heal spell allows the animal to heal 10 life points."),
    /** Shield spell. */
    SHIELD("Shield", "The shield spell allows the animal to block a mythical creature "
        + "attack for that turn."),
    /** Confuse spell. */
    CONFUSE("Confuse", "The confuse spell allows the animal to confuse a mythical "
        + "creature on a square adjacent to the animal but not the square the animal is occupying. "
        + "The mythical creature will not attack any animal for the next turn."),
    /** Charm spell. */
    CHARM("Charm", "The charm spell allows the animal to charm a mythical creature on "
        + "a square adjacent to the animal but not the square the animal is occupying. The "
        + "mythical creature will not attack the charming animal for the next three turns.");

    // Information about the spell.
    private String name;
    private String description;

    /**
     * Creates a spell with the specified name and description.
     * 
     * @param name The name of the spell.
     * @param description A description of the spells effects.
     */
    Spell(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Gets this spell's name.
     * 
     * @return The name of this spell.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets this spell's description.
     * 
     * @return The name of this description.
     */
    public String getDescription() {
        return description;
    }
}
