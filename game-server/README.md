# Practical 2 - Game Server

A program for running a game server for the game Woodland Diplomacy. This project includes a fully functioning HTTP server with multi-threading to handle multiple clients connections. The server communicates to clients through HTTP `GET`, `POST` and `OPTIONS` requests and handles game instantiation and player actions through JSON format.

## Usage
The following assumes you are in the root directory of this project. The project should be setup to include the `javax.json` third-party libraries, which are used for constructing and reading JSON strings.
```bash
mkdir bin
javac -d bin -cp "./lib/*" ./src/**/*.java
```
The game server can be started through the `GameServerMain` executable, and requires two command line arguments. The first argument is the port number that the server should run on. The second argument is the seed for the random number generator used to initialise the board such that starting board is predictable.
```bash
java -cp "./lib/*:./bin" GameServerMain 4567 1234
```
Any client can then connect to the server and play a game of Woodland diplomacy by entering the following URL.
```bash
http://localhost:4567/
```

## Testing
The project included the original stacscheck tests located under the `tests` directory. These tests are used to verify that the basic functionality of the game works correctly. Note that, in testing the more complex functionality of the game, manual testing was used. You can run all the tests by entering the following command:
```bash
stacscheck /cs/home/<username>/CS5001-p2/tests/
```
If all the tests run successfully the following should be displayed:
```bash
12 out of 12 tests passed
```

## Extra Features and Implementation Notes
- Using `out.println()` on a UNIX or Linux based operating systems appends the newline character `\n` to each line, however stacscheck tests for the `\r\n` newline character. Hence, these had to be added manually. The program now works as is on any operating system, however for future note it is possible to check for the newline character on your system via `System.lineSeparator()`.
- The decision was made to separate the server and client handling between the two files `GameServer` and `ConnectionHandler` to allow for multiple clients to connect and run separate instances of Woodland Diplomacy games at once. The `ConnectionHandler` class implements `Thread` and hence each connection handler runs on a separate thread on the server.
- The server was designed to handle client disconnection at any point in time. A `cleanup()` was implemented to close all readers, writers and sockets cleanly.
- The class `GameStateManager` handles dealing with converting the current state of the game into a JSON string. This keeps the logic for parsing JSON objects separate from the game logic, resulting in clean, easy to follow logic in both classes. `GameStateManger` also deals with handling actions (moving animals, casting spells and resetting the game) sent by the `ConnectionHandler` and executing the correct methods associated with those actions. The `Game`, `Animal` and `Create` classes all have `toJson()` methods that convert each of the aforementioned game objects into the correct JSON format required by the client.
- The methods `moveAnimal()` and `castSpell()` within `Game` deal with most of the logic to do with the flow of the game. The `turn` attribute tracks the number of individual turns made by all animals (where a move and a spell are considered one turn). Extra information provided in the `extendedStatus` variable is used to provide the client with extra information about the state of the game and why certain moves or spells failed.
- The codebase was separated into various packages and sub-packages containing related code. The tree structure for these packages is shown below.
```
├── GameServerMain.java
├── README.md
└── woodland
    ├── Game.java
    ├── Square.java
    ├── entities
    │   ├── Animal.java
    │   ├── Creature.java
    │   ├── Spell.java
    │   ├── animals
    │   │   ├── Badger.java
    │   │   ├── Deer.java
    │   │   ├── Fox.java
    │   │   ├── Owl.java
    │   │   ├── Rabbit.java
    │   │   └── attributes
    │   │       ├── Digable.java
    │   │       ├── Flyable.java
    │   │       ├── Jumpable.java
    │   │       ├── LongJumpable.java
    │   │       └── ShortJumpable.java
    │   └── creatures
    │       ├── ComplicatedCentaur.java
    │       ├── DeceptiveDragon.java
    │       ├── PrecociousPhoenix.java
    │       ├── SassySphinx.java
    │       └── UnderAppreciatedUnicorn.java
    └── server
        ├── ConnectionHandler.java
        ├── GameServer.java
        └── GameStateManager.java
```