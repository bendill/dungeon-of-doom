import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class to create and manage a human player
 */
public class HumanPlayer extends Player {

    /**
     * The amount of gold the player has
     */
    private int gold;

    /**
     * The constructor for HumanPlayer
     * @param playerChar The character to represent the player on the map
     */
    public HumanPlayer(char playerChar) {
        super(playerChar);
        gold = 0;
    }

    /**
     * Gets a one-line input from the user through the console
     * @return The line entered by the user
     */
    private String getInput() {
        BufferedReader br;
        String input = "";

        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            input = br.readLine(); // Reads line from console
        } catch (IOException e) {
            System.out.println(e.toString()); // Prints error message
            System.exit(0); // Exits program
        }

        return input;
    }

    /**
     * Gets a command from the human player by getting input
     * @return An array containing each word of the command
     */
    @Override
    public String[] getCommand() {
        String command;
        String[] splitCommand;

        System.out.print("Enter a command: ");
        command = getInput().toUpperCase(); // Gets one line from the user and converts it to upper case
        splitCommand = command.split(" "); // Splits command into an array, separated by spaces

        return splitCommand;
    }

    /**
     * Gets the amount of gold the player currently has
     * @return The quantity of gold the player has
     */
    public int getGold() {
        return gold;
    }

    /**
     * Increments the amount of gold the player has by 1
     */
    public void pickupGold() {
        gold++;
    }
}