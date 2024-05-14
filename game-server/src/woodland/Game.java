package woodland;

import woodland.entities.Animal;
import woodland.entities.Creature;
import woodland.entities.Spell;

import woodland.entities.animals.Badger;
import woodland.entities.animals.Deer;
import woodland.entities.animals.Fox;
import woodland.entities.animals.Owl;
import woodland.entities.animals.Rabbit;

import woodland.entities.creatures.UnderAppreciatedUnicorn;
import woodland.entities.creatures.ComplicatedCentaur;
import woodland.entities.creatures.DeceptiveDragon;
import woodland.entities.creatures.PrecociousPhoenix;
import woodland.entities.creatures.SassySphinx;

import java.util.Random;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;

/**
 * A woodland diplomacy game.
 */
public class Game {

    // Game constants.
    protected static final int ROW = 20;
    protected static final int COL = 20;
    protected static final int SPELLS = 10;

    // The game board.
    protected Square[][] board;

    // Tracks the current turn.
    protected int turn;

    // Stores the animals and creatures.
    private Animal[] animals;
    private Creature[] creatures;

    // Status information for client.
    private String status;
    private String extendedStatus;

    // Seed to ensure consistency when creating games.
    private long seed;

    /**
     * Creates a woodland diplomacy game with a given seed.
     *
     * @param seed The seed used for randomisation to ensure consistent behavior.
     */
    public Game(long seed) {
        this.seed = seed;
        newGame();
    }

    /**
     * Creates a new game board and initialises game variables.
     */
    public void newGame() {
        this.turn = 0;
        this.status = "";
        this.extendedStatus = "";

        this.animals = new Animal[] {
            new Rabbit(),
            new Fox(),
            new Deer(),
            new Owl(),
            new Badger()};

        this.creatures = new Creature[] {
            new UnderAppreciatedUnicorn(),
            new ComplicatedCentaur(),
            new DeceptiveDragon(),
            new PrecociousPhoenix(),
            new SassySphinx()};

        // Initialise the game board.
        this.board = new Square[ROW][COL];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                this.board[i][j] = new Square(i, j);
            }
        }

        // Place animals, creatures, and spells on the board using the random seed.
        Random rand = new Random(this.seed);
        placeAnimals(rand);
        placeCreatures(rand);
        placeSpells(rand);
    }

    /**
     * Gets the board of this game.
     * 
     * @return The game board.
     */
    public Square[][] getBoard() {
        return this.board;
    }

    /**
     * Gets a specific square on the board of this game.
     * 
     * @param row The row of the square to fetch.
     * @param col The col of the square to fetch.
     * @return The specified square on the game board.
     */
    public Square getSquare(int row, int col) {
        return this.board[row][col];
    }

    /**
     * Gets the status of this game.
     * 
     * @return The status of this game.
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Gets extended status of this game.
     * 
     * @return The extended status of this game.
     */
    public String getExtendedStatus() {
        return this.extendedStatus;
    }

    /**
     * Updates the status and extended status messages.
     *
     * @param status The status message.
     * @param extendedStatus The extended status message.
     */
    private void setStatus(String status, String extendedStatus) {
        this.status = status;
        this.extendedStatus = extendedStatus;
    }

     /**
     * Retrieves the animal whose turn it is currently.
     *
     * @return The animal whose turn it is currently.
     */
    public Animal currentAnimal() {
        return this.animals[(this.turn / 2) % this.animals.length];
    }

    /**
     * Retrieves the animal whose turn it is next.
     *
     * @return The animal whose turn it is next.
     */
    public Animal nextAnimal() {
        return this.animals[((this.turn / 2) + 1) % this.animals.length];
    }

    /**
     * Returns the type of turn, move or spell.
     *
     * @return A string indicating the type of turn.
     */
    public String turnType() {
        return this.isMoveTurn() ? "Move" : "Spell";
    }

    /**
     * Checks if the current turn is a move turn.
     *
     * @return A boolean describing if it's currently a move turn.
     */
    private boolean isMoveTurn() {
        return (this.turn % 2 == 0);
    }

    /**
     * Applies any attacks on animals sharing a square with a creature, if there is any.
     * 
     * @param square The square to apply attacks on.
     */
    private void checkAttacks(Animal animal) {
        if (animal.getSquare().hasCreature()) {
            animal.getSquare().getCreature().attackAnimal(animal);
        }
    }

    /**
     * Moves a specific animal from one square to another on the game board, ensuring
     * the move is valid and handling any attacks, interruptions by other animas or creatures,
     * or spell pick-ups as a result of the move. This method also allows for the current
     * animals spell turn to be skipped if the next animal is to be moved instead.
     *
     * @param animal The animal to move.
     * @param oldRow The current row position of the animal.
     * @param oldCol The current column position of the animal.
     * @param newRow The row position to move the animal to.
     * @param newCol The column position to move the animal to.
     */
    public void moveAnimal(Animal animal, int oldRow, int oldCol, int newRow, int newCol) {
        // Check that game is in progress.
        if (gameOver()) {
            this.setStatus("The last move was invalid.", "Game finished.");
            return;
        }

        // Check move is within board.
        if (newRow >= ROW || newCol >= COL) {
            this.setStatus("The last move was invalid.", "Move outwith board.");
            return;
        }

        // Check that the animal being moved is either the current animal, or the next animal if skipping.
        // If skipping the current animals spell turn, check that the animal being moved is the next animal.
        if ((!this.isMoveTurn() || animal != this.currentAnimal()) && animal != this.nextAnimal()) {
            this.setStatus("The last move was invalid.", "Currently " + this.currentAnimal().getName() + "\'s turn.");
            return;
        }

        // Check that the new position is not occupied by another animal.
        if (this.getSquare(newRow, newCol).hasAnimal() && animal != this.getSquare(newRow, newCol).getAnimal()) {
            this.setStatus("The last move was invalid.", this.getSquare(newRow, newCol).getAnimal().getName() + " is already in this square.");
            return;
        }

        // Check that move is valid for a specific animal.
        if (!animal.move(oldRow, oldCol, newRow, newCol)) {
            this.setStatus("The last move was invalid.", animal.getName() + " cannot perform this move.");
            return;
        }

        // Check if move is interrupted/blocked by another animal.
        if (animal.animalInterruption(this.board, oldRow, oldCol, newRow, newCol)) {
            this.setStatus("The last move was invalid.", animal.getName() + " was blocked by another animal.");
            return;
        }

        this.setStatus("The last move was successful.", "Moved " + animal.getName() + " to " + newRow + ", " + newCol);

        // Check if animals move was interrupted by a creature.
        Square interSquare = animal.creatureInterruption(this.board, oldRow, oldCol, newRow, newCol);
        if (interSquare != null) {
            newRow = interSquare.getRow();
            newCol = interSquare.getCol();
            this.setStatus("The last move was interrupted by a creature.", "Moved " + animal.getName() + " to " + newRow + ", " + newCol);
        }

        // Move the animal.
        this.board[oldRow][oldCol].setAnimal(null);
        this.board[newRow][newCol].setAnimal(animal);

        // Pick up any spell in this new square.
        if (board[newRow][newCol].hasSpell()) {
            this.saveSpell(animal, this.board[newRow][newCol].getSpell());
        }

        // Apply attacks to current animal if skipping the current animals spell turn.
        // This is applied only if the next animal is being moved.
        if (this.nextAnimal() == animal) {
            this.checkAttacks(this.currentAnimal());
        }

        // Move to spell turn only if current or next animal has been moved on a move turn.
        this.turn += this.isMoveTurn() ? 1 : 0;
        // Move to next animal if current animal has been skipped.
        this.turn += this.nextAnimal() == animal ? 2 : 0;
    }

    /**
     * Casts a chosen spell by a specific animal, ensuring that the spell casting
     * is valid and handling any changes to creatures or animals as necessary.
     *
     * @param animal The animal casting the spell.
     * @param spell  The spell that is being cast.
     */
    public void castSpell(Animal animal, Spell spell) {
         // Check that game is in progress.
        if (gameOver()) {
            setStatus("The last spell was invalid.", "Game has finished.");
            return;
        }

        // Check that the current animal can cast a spell.
        if (this.isMoveTurn() || this.currentAnimal() != animal) {
            setStatus("The last spell was invalid.", "Currently " + this.currentAnimal().getName() + "\'s turn to cast a spell.");
            return;
        }

        // Cast the chosen spell and update the game status.
        switch (spell) {
            case DETECT:
                animal.detect(this.board);
                break;
            case HEAL:
                animal.heal();
                break;
            case SHIELD:
                animal.shield();
                break;
            case CONFUSE:
                animal.confuse(this.board);
                break;
            case CHARM:
                animal.charm(this.board);
                break;
            default:
                break;
        }
        animal.updateSpell(spell);
        this.setStatus("The last spell was successful.", animal.getName() + " cast the " + spell.getName() + " spell.");

        // Apply attacks to current animal.
        this.checkAttacks(animal);
        this.turn++;
    }

    /**
     * Retrieves the square occupied by an animal with a specified name. This proves
     * useful for locating animals on the board whose position is unknown.
     *
     * @param animalName The name of the animal whose square is to be retrieved.
     * @return The square that the specified animal occupies.
     */
     public Square getSquare(String animalName) {
        for (Animal animal : animals) {
            if (animal.getName().equals(animalName)) {
                return animal.getSquare();
            }
        }
        return null;
    }

    /**
     * Adds a spell to the specified animal's list of spells and removes the spell from the board.
     *
     * @param animal The animal who picked up the spell.
     * @param spell  The spell to be added to the animal's list of spells.
     */
    public void saveSpell(Animal animal, Spell spell) {
        animal.addSpell(spell);
    }

    /**
     * Determines if the game is over, checking if the win or loss conditions have been met,
     * and updates the games status to display this. The player has won if all animals make
     * it to the last row on the board. If an animal dies the player loses.
     *
     * @return The status describing if the game is over.
     */
    public boolean gameOver() {
        int finished = 0;
        for (Animal animal : animals) {
            if (!animal.isAlive()) {
                setStatus("You have lost the game.", animal.getName() + " died.");
                return true;
            }
            finished += animal.hasFinished() ? 1 : 0;
        }

        if (finished == animals.length) {
            setStatus("You have won the game.", "All animals have made it to the woodland.");
            return true;
        }
        return false;
    }

    /**
     * Places animals at random starting positions on the board, according to the provided
     * random number generator, which is initalised with the game's seed. The animals are
     * initialised to a random position in the last row of the board.
     *
     * @param rand The random number generator.
     */
    private void placeAnimals(Random rand) {
        int col;
        for (Animal animal : animals) {
            col = rand.nextInt(COL);
            while (board[ROW - 1][col].isOccupied()) {
                col = rand.nextInt(COL);
            }
            board[ROW - 1][col].setAnimal(animal);
        }
    }

    /**
     * Places creatures at random starting positions on the board, according to the provided
     * random number generator, which is initalised with the game's seed. The creatures are
     * initialised to a random position in anywhere on the board except the first and last rows.
     *
     * @param rand The random number generator.
     */
    private void placeCreatures(Random rand) {
        int row, col;
        for (Creature creature : creatures) {
            row = rand.nextInt(ROW - 2) + 1;
            col = rand.nextInt(COL);
            while (board[row][col].isOccupied()) {
                col = rand.nextInt(COL);
            }
            board[row][col].setCreature(creature);
        }
    }

    /**
     * Places 10 random spells at random starting positions on the board, according to the provided
     * random number generator, which is initalised with the game's seed. The spells are
     * initialised to a random position in anywhere on the board except the first and last rows.
     *
     * @param rand The random number generator.
     */
    private void placeSpells(Random rand) {
        int row, col;
        Spell spell;
        Spell[] spells = Spell.values();
        for (int i = 0; i < SPELLS; i++) {
            spell = spells[rand.nextInt(spells.length)];
            row = rand.nextInt(ROW - 2) + 1;
            col = rand.nextInt(COL);
            while (board[row][col].isOccupied()) {
                col = rand.nextInt(COL);
            }
            board[row][col].setSpell(spell);
        }
    }

    /**
     * Converts this state of this game to a JSON object.
     *
     * @return A JsonArray representing the current state of this game.
     */
    public JsonArray toJson() {
        JsonArrayBuilder outerArrayBuilder = Json.createArrayBuilder();
        for (int i = 0; i < ROW; i++) {

            JsonArrayBuilder middleArrayBuilder = Json.createArrayBuilder();
            for (int j = 0; j < COL; j++) {
                Square square = getSquare(i, j);

                JsonArrayBuilder innerArrayBuilder = Json.createArrayBuilder();
                if (square.hasAnimal()) {
                    innerArrayBuilder.add(square.getAnimal().toJson());
                }
                if (square.hasCreature() && square.isVisible()) {
                    innerArrayBuilder.add(square.getCreature().toJson());
                }
                middleArrayBuilder.add(innerArrayBuilder);
            }
            outerArrayBuilder.add(middleArrayBuilder);
        }
        return outerArrayBuilder.build();
    }
}
