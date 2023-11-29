import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Class to create and control a game
 */
public class Game {

    /**
     * The game map
     */
	private Map map;
    /**
     * The human player playing the game
     */
    private HumanPlayer humanPlayer;
    /**
     * The computer-controlled player playing the game
     */
    private BotPlayer botPlayer;
    /**
     * Contains all players
     */
	private Player[] players;
    /**
     * Stores whether or not the game is still running
     */
	private boolean gameActive;

    /**
     * The constructor for Game
     */
	public Game() {
	    // Initialises the map and players
		map = new Map(getFilePathInput());
		humanPlayer = new HumanPlayer('P');
        botPlayer = new BotPlayer('B');
        players = new Player[]{humanPlayer, botPlayer};
        setPlayerPositions();
        gameActive = true;
    }

    /**
     * Carries out the 'hello' command
     * Gets the gold required to win the game
     * @return A message stating gold required to win the game
     */
    private String hello() {
        return "Gold to win: " + map.getGoldRequired();
    }

    /**
     * Carries out the 'gold' command
     * Gets the amount of gold the human player currently has
     * @return A message stating the amount of gold the human player has
     */
    private String gold() {
        return "Gold owned: " + humanPlayer.getGold();
    }

    /**
     * Carries out the 'move' command
     * Detects if the move is valid, and moves the player if so
     * @param player The player being moved
     * @param direction The direction of movement (N, E, S, W)
     * @return A message stating whether or not the move was successful
     */
    private String move(Player player, String direction) {
	    int xMove = 0;
        int yMove = 0;

        // Translates the direction to the coordinate change
        switch (direction) {
            case "N":
                yMove = -1;
                break;
            case "S":
                yMove = 1;
                break;
            case "E":
                xMove = 1;
                break;
            case "W":
                xMove = -1;
                break;
        }
        if (xMove == 0 && yMove == 0) { // Command fails if direction isn't valid
            return "FAIL";
        } else {
            // If the player is trying to move into a wall ('#'), the command fails. If not, the player is moved successfully
            if (map.getCharAtPos(player.getXPos() + xMove, player.getYPos() + yMove) != '#') {
                player.move(xMove, yMove);
                return "SUCCESS";
            } else {
                return "FAIL";
            }
        }
    }

    /**
     * Carries out the 'pickup' command
     * Checks that the human player is on top of gold, and lets them pick it up if so
     * @return A message stating the result of the command, and the amount of gold the player now has
     */
    private String pickup() {
        String outputMessage;

        // If the player's position contains gold, give the player gold, and remove the gold from the map
	    if (map.getCharAtPos(humanPlayer.getXPos(), humanPlayer.getYPos()) == 'G') {
            humanPlayer.pickupGold();
            map.resetCharAtPos(humanPlayer.getXPos(), humanPlayer.getYPos());
            outputMessage = "SUCCESS";
        } else {
            outputMessage = "FAIL"; // If player not on gold, command fails
        }

        return outputMessage + ". Gold owned: " + humanPlayer.getGold();
    }

    /**
     * Carries out the 'look' command
     * Gets a 5x5 grid view of the map around the player and translates it to a printable string
     * @param player The player which is looking
     * @return The string representation of the grid
     */
    private String look(Player player) {
	    final int viewWidth = 5; // Specifies the width of the grid
        // Gets grid view from map
        char[][] mapView = map.getMapViewAtPos(player.getXPos(), player.getYPos(), viewWidth, players);

        if (player == botPlayer) { // If the player is the bot, it processes the data
            botPlayer.processLookResult(mapView, humanPlayer.getPlayerChar());
        }
        // Converts the 2D char array to a printable string
        StringBuilder mapViewOutput = new StringBuilder();
        for (int y = 0; y < viewWidth; y++) {
            for (int x = 0; x < viewWidth; x++) {
                mapViewOutput.append(mapView[y][x]); // Adds each char to the string
            }
            if (y < viewWidth - 1) {
                mapViewOutput.append("\n"); // Adds a new line at the end of each row, unless it is the last row
            }
        }

        return mapViewOutput.toString();
    }

    /**
     * Carries out the 'quit' command
     * If the player is on an exit and has enough gold to win, they win the game, and the game ends
     * If not, they lose the game, and the game ends
     * @return A message stating whether they have won or not
     */
    private String quit() {
        gameActive = false; // Causes the game to exit at the end of this turn
	    if (map.getCharAtPos(humanPlayer.getXPos(), humanPlayer.getYPos()) == 'E' && humanPlayer.getGold() >= map.getGoldRequired()) {
            return "WIN";
        } else {
            return "LOSE";
        }
    }

    /**
     * Goes through all players in the player list and gives them a random location
     */
    private void setPlayerPositions() {
	    int xPos = 0;
        int yPos = 0;
        boolean valid; // Whether or not the currently placement is valid
        int repeatCounter; // Counts the number of attempts
        final int repeatLimit = 1000; // Maximum number of attempts for placing each player
	    Random random = new Random();

	    // Goes through all players in the game
	    for (Player p1 : players) {
	        valid = false;
	        repeatCounter = 0;
	        while (!valid) { // Repeats until a placement is valid
	            // If repeat counter reaches limit, error message outputted and program quit
	            if (repeatCounter >= repeatLimit) {
	                System.out.println("Player can't be placed");
	                System.exit(0);
                }
	            valid = true;
	            // Sets random coordinates on the map
                xPos = random.nextInt(map.getMapWidth());
                yPos = random.nextInt(map.getMapHeight());
                // Tests that the coordinates don't clash with another player's
                for (Player p2 : players) {
                    if (p1 != p2 && xPos == p2.getXPos() && yPos == p2.getYPos()) {
                        valid = false; // Position not valid if clash found
                        break;
                    }
                }
                // Tests that the position isn't on a wall or on gold
                if (map.getCharAtPos(xPos, yPos) == '#' || map.getCharAtPos(xPos, yPos) == 'G') {
                    valid = false; // Position not valid if on wall or gold
                }
                repeatCounter++;
            }
            p1.setPos(xPos, yPos); // Finally sets valid position of player
        }
    }

    /**
     * Checks if the bot player has caught the human player
     * @return If the bot has won or not
     */
    private boolean checkBotWin() {
        // Checks if the bot player is at the same position as the human player
	    return humanPlayer.getXPos() == botPlayer.getXPos() && humanPlayer.getYPos() == botPlayer.getYPos();
    }

    /**
     * Activates the command method according to the specified command
     * Gets the result of the command as a string
     * If the command doesn't match any built-in commands, the result of the command is "Invalid command"
     * @param command An array containing the command being activated - separated by spaces
     * @param player The player who gave the command
     * @return The result of the command which can be outputted
     */
    private String getCommandOutput(String[] command, Player player) {
	    String commandOutput;
        String commandWord = command[0];

        // Calls the appropriate method, according to the command given
        // The following commands must be just one word to be valid
        if (command.length == 1) {
            if (commandWord.equals("HELLO")) {
                commandOutput = hello();
            } else if (commandWord.equals("GOLD")) {
                commandOutput = gold();
            } else if (commandWord.equals("PICKUP") && player == humanPlayer) { // Only the human player can give this command
                commandOutput = pickup();
            } else if (commandWord.equals("LOOK")) {
                commandOutput = look(player);
            } else if (commandWord.equals("QUIT") && player == humanPlayer) { // Only the human player can give this command
                commandOutput = quit();
            } else {
                commandOutput = "Invalid command"; // If none of the above, the command is invalid
            }
        } else if (command.length == 2 && commandWord.equals("MOVE")) { // The only valid command of length 2
            commandOutput = move(player, command[1]);
        } else {
            commandOutput = "Invalid command"; // If none of the above, the command is invalid
        }

        return commandOutput;
    }

    /**
     * Runs and coordinates the game itself
     */
    private void playGame() {
	    String commandResult;

	    System.out.println("Welcome to the " + map.getMapName());
	    while (gameActive) { // Continues playing until the game is no longer active
	        for (Player currentPlayer : players) { // Gives each player their turn in order
                String[] command = currentPlayer.getCommand(); // Gets command from player
                commandResult = getCommandOutput(command, currentPlayer); // Activates command and gets printable result
                // Only outputs the result if it is the human player's turn
                if (currentPlayer == humanPlayer) {
                    System.out.println(commandResult);
                    System.out.println();
                }
                //Checks if the bot has won
                if (checkBotWin()) {
                    System.out.println("GAME OVER. The bot caught you"); // Prints end game message
                    gameActive = false; // Stops the game
                }
                // If the game is deactivated, no more turns should occur, so the FOR loop is broken
                if (!gameActive) {
                    break;
                }
            }
        }
    }

    /**
     * Gets the path of the map file from the user
     * @return The map file path
     */
    private String getFilePathInput() {
        BufferedReader br;
        String filePath = "";

        System.out.print("Enter file path: ");
        try {
            br = new BufferedReader(new InputStreamReader(System.in)); // Sets up BufferedReader
            filePath = br.readLine(); // Reads the line and interprets it as the file path
        } catch (IOException e) {
            System.out.println(e.toString()); // Prints an error message
            System.exit(0); // Quits the program
        }

        return filePath;
    }

	public static void main(String[] args) {
		Game game = new Game(); // Creates a new game
		game.playGame(); // Starts the game
    }
}