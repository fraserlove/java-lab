package woodland.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 * Abstract base class representing a creature in the woodland game.
 * Creatures have the ability to attack animals, and can be charmed or confused.
 */
public abstract class Creature {

    // Number of turns the charm spell lasts on a creature.
    private static final int CHARM_LENGTH = 3;

    // Information about the creature.
    protected String name;
    protected String shortName;
    protected String description;
    protected int attackValue;

    // Status indicating whether the creature is currently confused.
    protected boolean confused;

    // A map storing which animals have currently charmed this creature, and for how many turns.
    protected Map<Animal, Integer> charmAnimal;

    // A list of animals currently shielded from this creature.
    protected List<Animal> shieldAnimal;

    /**
     * Creates a creature with the specified name and attack value.
     * 
     * @param name        The name of the creature.
     * @param attackValue The attack value of the creature.
     */
    public Creature(String name, int attackValue) {
        this.name = name;
        this.attackValue = attackValue;
        this.charmAnimal = new HashMap<>();
        this.shieldAnimal = new ArrayList<>();
    }

    /**
     * Adds an animal to the list of animals shielded from this creature.
     *
     * @param animal The animal to shield.
     */
    public void addShieldAnimal(Animal animal) {
        shieldAnimal.add(animal);
    }

    /**
     * Adds an animal to the list of animals charming this creature.
     *
     * @param animal The animal charming this creature.
     */
    public void addCharmAnimal(Animal animal) {
        charmAnimal.put(animal, CHARM_LENGTH);
    }

    /**
     * Sets the confused status of this creature.
     *
     * @param confused New confusion status of this creature.
     */
    public void setConfused(boolean confused) {
        this.confused = confused;
    }

    /**
     * Checks if a specific animal is charming this creature.
     *
     * @param animal The animal to check.
     * @return The charmed status of this creature, with respect to the given animal.
     */
    public boolean isCharmed(Animal animal) {
        return charmAnimal.containsKey(animal) && charmAnimal.get(animal) > 0;
    }

    /**
     * Checks the confusion status of this creature.
     * 
     * @return The confusion status of this creature.
     */
    public boolean isConfused() {
        return this.confused;
    }

    /**
     * Checks if a specific animal is shielded from this creature.
     *
     * @param animal The animal to check.
     * @return The shielding status of the current animal.
     */
    public boolean isShieldAnimal(Animal animal) {
        return shieldAnimal.contains(animal);
    }

    /**
     * Updates the charm status of a specific animal, decreasing the number of turns left.
     *
     * @param animal The charmed animal.
     */
    public void updateCharmAnimal(Animal animal) {
        charmAnimal.put(animal, charmAnimal.getOrDefault(animal, 1) - 1);
    }

    /**
     * Removes the shield from a specific animal if one exists, making it vulnerable
     * to this creature again.
     *
     * @param animal The shielded animal.
     */
    public void updateShieldAnimal(Animal animal) {
        if (shieldAnimal.contains(animal)) {
            shieldAnimal.remove(animal);
        }
    }

    /**
     * Attacks the given animal, reducing its health points by this creature's attack value.
     * The attack will only be successful if the creature is not confused, the animal
     * is not charmed by the creature, and the animal is not shielded from the creature.
     *
     * @param animal The animal that this creature is attempting to attack.
     */
    public void attackAnimal(Animal animal) {
        if (!this.isConfused() && !this.isCharmed(animal) && !this.isShieldAnimal(animal)) {
            animal.attacked(this.attackValue);
            return;
        }
        // Removing and updating the shield, charm and confusion status of the creature.
        this.setConfused(false);
        this.updateShieldAnimal(animal);
        this.updateCharmAnimal(animal);
    }

    /**
     * Converts this state of this creature to a JSON object.
     *
     * @return A JsonObject representing the current state of this creature.
     */
    public JsonObject toJson() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        // Building JsonArray of animals charming this creature.
        for (Map.Entry<Animal, Integer> entry : charmAnimal.entrySet()) {
            if (entry.getValue() > 0) {
                arrayBuilder.add(Json.createObjectBuilder()
                    .add("name", entry.getKey().getName())
                    .add("turnsLeft", entry.getValue())
                    .build());
            }
        }
        return Json.createObjectBuilder()
                .add("name", name)
                .add("type", "Creature")
                .add("shortName", shortName)
                .add("description", description)
                .add("attack", attackValue)
                .add("confused", confused)
                .add("charmed", arrayBuilder.build())
                .build();
    }
}
