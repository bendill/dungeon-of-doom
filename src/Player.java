/**
 * Abstract class to manage a general player
 */
public abstract class Player {

    /**
     * The horizontal coordinate of the player
     */
    private int xPos;
    /**
     * The vertical coordinate of the player
     */
    private int yPos;
    /**
     * The character used to represent the player
     */
    private final char playerChar;

    /**
     * Constructor for Player
     * @param playerChar The character to represent the player on the map
     */
    public Player(char playerChar) {
        this.playerChar = playerChar;
    }

    /**
     * Abstract method to get the player's command on their turn
     * @return An array containing each word of the command
     */
    public abstract String[] getCommand();

    /**
     * Moves the player's position by an x and y value
     * @param xMove The horizontal displacement
     * @param yMove The vertical displacement
     */
    public void move(int xMove, int yMove) {
        xPos += xMove;
        yPos += yMove;
    }

    /**
     * Gets the player's representative character
     * @return The player's character
     */
    public char getPlayerChar() {
        return playerChar;
    }

    /**
     * Gets the player's horizontal position
     * @return The player's x coordinate
     */
    public int getXPos() {
        return xPos;
    }

    /**
     * Gets the player's vertical position
     * @return The player's y coordinate
     */
    public int getYPos() {
        return yPos;
    }

    /**
     * Sets the position of the player
     * @param xPos The x coordinate to move the player to
     * @param yPos The y coordinate to move the player to
     */
    public void setPos(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }
}
