package woodland.server;

import woodland.Game;
import woodland.Square;
import woodland.entities.Spell;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Manages the game state including initalising, resetting, handling actions,
 * and representing the game as a JSON string.
 */
public class GameStateManager {

    // Woodland supremacy game.
    private Game game;

    /**
     * Creates a game state manager with a new game initialised with a given seed.
     *
     * @param seed The seed to initialise the random number generator.
     */
    public GameStateManager(long seed) {
        this.game = new Game(seed);
    }

    /**
     * Resets the game and returns the JSON representation of the new game.
     *
     * @return The JSON string representation of the new game.
     */
    public String reset() {
        game.newGame();
        return gameState();
    }

    /**
     * Creates a JSON string representing the current state of the game. Includes the
     * board layout and game information.
     *
     * @return The JSON string representation of the game.
     */
    public String gameState() {
        return Json.createObjectBuilder()
            .add("board", game.toJson())
            .add("gameOver", game.gameOver())
            .add("currentAnimalTurn", game.currentAnimal().getName())
            .add("nextAnimalTurn", game.nextAnimal().getName())
            .add("status", game.getStatus())
            .add("currentAnimalTurnType", game.turnType())
            .add("extendedStatus", game.getExtendedStatus())
            .build().toString();
    }

    /**
     * Handles a player actions and updates the game accordingly. The action
     * is specified in the form of a JSON object detailing a move or spell.
     *
     * @param action The JSON object containing the action details.
     * @return The JSON string representation of the updated game.
     */
    public String handleAction(JsonObject action) {
        String actionType = action.getString("action");
        if (actionType.equals("move")) {
            handleMove(action);
        }
        else if (actionType.equals("spell")) {
            handleSpell(action);
        }
        return gameState();
    }

    /**
     * Handles a move action where an animal moves to a new position.
     *
     * @param action The JSON object containing the move details.
     */
    private void handleMove(JsonObject action) {
        Square square = game.getSquare(action.getString("animal"));
        int newRow = action.getJsonObject("toSquare").getInt("row");
        int newCol = action.getJsonObject("toSquare").getInt("col");
        game.moveAnimal(square.getAnimal(), square.getRow(), square.getCol(), newRow, newCol);
    }

    /**
     * Handles a spell action where an animal casts a spell.
     *
     * @param action The JSON object containing the spell details.
     */
    private void handleSpell(JsonObject action) {
        Square square = game.getSquare(action.getString("animal"));
        Spell spell = Spell.valueOf(action.getString("spell").toUpperCase());
        game.castSpell(square.getAnimal(), spell);
    }
}
