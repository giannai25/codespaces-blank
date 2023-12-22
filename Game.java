import java.util.HashMap;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private  Room upstairs, stairs, babyRoom1,babyRoom2, master, masterBathroom, kitchen, diningRoom, livingRoom, bathroom, upstairsHallway,downstairs;
    private Room chosen;
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
       // Room upstairs, stairs, babyRoom1,babyRoom2,babyBathroom, master, masterBathroom, kitchen, diningRoom, livingRoom, bathroom, upstairsHallway,downstairs, upstairs;
      
        // create the rooms
        stairs = new Room("on the stairs");
     babyRoom1 = new Room("in the first baby room");
       // babyRoom2 = new Room("in the second baby room");
        master = new Room("in the master bedroom");
        masterBathroom = new Room("in the master bathroom");
        kitchen = new Room("in the kitchen");
        diningRoom = new Room("in the dining room");
        livingRoom = new Room("in the living room");
        //bathroom = new Room("in the guest bathroom");
        upstairsHallway=new Room("in the upstairs hallway");
        //downstairs=new Room("going downstairs");
        //upstairs=new Room("going upstairs");     
        
        
        // initialise room exits (north, east, south, west)
        livingRoom.setExits(stairs,kitchen,null,null);
        kitchen.setExits(null,diningRoom,null,livingRoom);
        diningRoom.setExits(null,null,null,kitchen);
        upstairsHallway.setExits(master,null,stairs,babyRoom1);
        master.setExits(masterBathroom,null,upstairsHallway,null);
        babyRoom1.setExits(null,upstairsHallway,null,null);
      //  downstairs.setExits(null,livingRoom,null,null);
        //upstairs.setExits(upstairsHallway,null,null,null);
        currentRoom = upstairsHallway;  // start game outside
        stairs.setExits(upstairsHallway,null,livingRoom,null);
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public boolean randomRoom(){
        createRooms();
        chosen= new Room("phone is in this room");
        int randomNum=(int)((Math.random()*8)+1);
        if(randomNum==1){
            chosen=babyRoom1;
        }
        if(randomNum==2){
            chosen=babyRoom2;
        }
        if(randomNum==3){
            chosen=master;
        }
        if(randomNum==4){
            chosen=masterBathroom;
        }
        if(randomNum==5){
            chosen=kitchen;
        }
        if(randomNum==6){
            chosen=diningRoom;
        }
        if(randomNum==7){
            chosen=livingRoom;
        }
        if (chosen==currentRoom){
            return true;
        }
      return false;
    }
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! (randomRoom()==true)&&!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        
        System.out.println("You found the phone and turned off the alarm in time!!\nThank you for playing.  Good bye.");
    }
    private void printLocationInfo(){
        if(currentRoom.getExit("north") != null) {
            System.out.print("north ");
        }
        if(currentRoom.getExit("east") != null) {
            System.out.print("east ");
        }
        if(currentRoom.getExit("south") != null) {
            System.out.print("south ");
        }
        if(currentRoom.getExit("west") != null) {
            System.out.print("west ");
        }
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println("You are " + currentRoom.getDescription());
        System.out.print("You can go: ");
        printLocationInfo();
        System.out.println();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println("   go quit help");
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = null;
        if(direction.equals("north")) {
            nextRoom = currentRoom.getExit("north");
        }
        if(direction.equals("east")) {
            nextRoom = currentRoom.getExit("east");
        }
        if(direction.equals("south")) {
            nextRoom = currentRoom.getExit("south");
        }
        if(direction.equals("west")) {
            nextRoom = currentRoom.getExit("west");
        }

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println("You are " + currentRoom.getDescription());
            System.out.print("Exits: ");
            printLocationInfo();
            System.out.println();
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}