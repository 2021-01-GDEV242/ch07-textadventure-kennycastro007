import java.util.Date;
import java.util.ArrayList;

/**
 * This class is the main class of the "World of Zuul" application. "World of
 * Zuul" is a very simple, text based adventure game. Users can walk around some
 * scenery. That's all. It should really be extended to make it more
 * interesting!
 * 
 * To play this game, create an instance of this class and call the "play"
 * method.
 * 
 * This main class creates and initialises all the others: it creates all rooms,
 * creates the parser and starts the game. It also evaluates and executes the
 * commands that the parser returns.
 * 
 * @author Kenny Castro-Monroy
 * @version 03.07.2021
 */

public class Game {
    private Parser parser;
    private Room currentRoom;
    private ArrayList<Item> inventory;
    private ArrayList<Room> roomHistory;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() {
        createRooms();
        parser = new Parser();
        inventory = new ArrayList<Item>();
        roomHistory = new ArrayList<Room>();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms() {
        Room outside, theater, pub, lab, office, veranda, vestibule, dungeon, policeStation, fireStation, bathroom,
                basement, nursery, kitchen, diningRoom;

        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");

        veranda = new Room("on the veranda");
        vestibule = new Room("in a vestibule... What is a vestibule again?");
        dungeon = new Room("in a... dungeon? I thought this was a school?");
        policeStation = new Room("in a police station");
        fireStation = new Room("in the fire station");
        bathroom = new Room("in a bathroom, it really smells. Damn tacos");
        basement = new Room("in the basement");
        nursery = new Room("in the nursery");
        kitchen = new Room("in the kitchem");
        diningRoom = new Room("in the dining room");

        outside.addItem(new Item("water gun", 15));
        outside.addItem(new Item("hose", 540));
        theater.addItem(new Item("dress", 60));
        dungeon.addItem(new Item("chains", 200));

        // initialise room exits
        outside.setExit("east", theater);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.setExit("north", veranda);

        theater.setExit("west", outside);
        theater.setExit("north", kitchen);
        theater.setExit("east", policeStation);

        pub.setExit("east", outside);
        pub.setExit("west", vestibule);

        lab.setExit("north", outside);
        lab.setExit("east", office);
        lab.setExit("south", basement);

        office.setExit("west", lab);

        veranda.setExit("north", diningRoom);
        veranda.setExit("east", kitchen);
        veranda.setExit("west", policeStation);

        vestibule.setExit("east", pub);
        vestibule.setExit("north", bathroom);

        bathroom.setExit("south", vestibule);

        kitchen.setExit("south", theater);

        policeStation.setExit("west", theater);

        basement.setExit("north", lab);
        basement.setExit("south", nursery);
        basement.setExit("west", fireStation);

        nursery.setExit("north", basement);
        nursery.setExit("south", dungeon);

        dungeon.setExit("north", nursery);

        currentRoom = outside; // start game outside
    }

    /**
     * Main play routine. Loops until end of play.
     */
    public void play() {
        long maxTime = 2 * 60 * 1000; // 2 minutes worth of milliseconds
        long startTime = System.currentTimeMillis();
        printWelcome();

        // Enter the main command loop. Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        long currentTime = System.currentTimeMillis();
        while (!finished && (currentTime - startTime) < maxTime) {
            Command command = parser.getCommand();
            finished = processCommand(command);
            currentTime = System.currentTimeMillis();
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
        case UNKNOWN:
            System.out.println("I don't know what you mean...");
            break;

        case HELP:
            printHelp();
            break;

        case GO:
            goRoom(command);
            break;

        case QUIT:
            wantToQuit = quit(command);
            break;
        case LOOK:
            look();
            break;
        case EAT:
            System.out.println(
                    "You have eaten some nom-noms and are not full. But you ate a lot and are now feeling self-conscious");
            break;
        case BACK:
            goBack();
            break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information. Here we print some stupid, cryptic message
     * and a list of the command words.
     */
    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /**
     * Try to go in one direction. If there is an exit, enter the new room,
     * otherwise print an error message.
     */
    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        } else {
            roomHistory.add(new Room(currentRoom));
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /**
     * "Quit" was entered. Check the rest of the command to see whether we really
     * quit the game.
     * 
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) {
        if (command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        } else {
            return true; // signal that we want to quit
        }
    }

    /**
     * Prints a description of the current room
     */
    private void look() {
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Returns you to the previous room
     */
    private void goBack() {
        currentRoom = new Room(roomHistory.get(roomHistory.size() - 1));
        roomHistory.remove(roomHistory.size() - 1);

    }
}
