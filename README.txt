## Dungeon of Doom

Dungeon of Doom is a command-line game made using Java. It a single player game, which is 
played against a computer-controlled player. The game is played on a map made up of 
squares, where each square can contain a wall, floor, gold, or an exit. Players take it in 
turns to enter a command, allowing them to move around and interact with the map, amongst 
other things.
This continues until a player wins the game. For the human player to win, they must 
collect gold, and then quit the map once they have enough. For the computer to win, it must 
catch the player.

## How to Play

Here are the commands you may use in the game. Note that the computer only uses MOVE and 
LOOK, and is prevented from using PICKUP and QUIT.

HELLO - Shows the amount of gold needed to win the game
GOLD - Shows the amount of gold the player owns
MOVE <direction> - Moves the player in the direction specified (N, E, S or W)
PICKUP - Picks up a gold if the player is standing on one
LOOK - Shows a 5x5 grid of the map surrounding the player
QUIT - Quits the dungeon if the player is on an exit square with enough gold, if not, the 
       player loses

## Map File

To start the game, you must enter a file name. The easiest way to do this is enter the path 
name relative to the Java/class files. For example, if you the map file is alongside the 
Java/class files in a folder, just enter the filename (with .txt on the end), and the file 
will be selected. Once this is done, you may play the game.

The map file must be in a specific format, or the program will close when reading the file. 
This is the format:

line 1: "name " followed by the name of the map
line 2: "win " followed by the amount of gold needed to win the game
all following lines: a series of characters (listed below) where every line has the same
		     number of characters

These are the valid map characters:

'.' - Floor
'#' - Wall
'G' - Gold
'E' - Exit

Note that the game will close if there isn't enough room to place the players on the map. 
Players cannot be placed on walls or on gold.

## Implementation

My code is split into 5 classes:

Game - Coordinates the game itself, and carries out the commands using calls to other class 
       methods. In each turn, it gets the player's commmand, decodes it and selects the 
       appropriate method to carry out the command. If it's the human player's turn, it then 
       prints the result of this command.

Map - In charge of reading the map file, and storing the map layout, along with the gold 
      required to win and the map name. It's also in charge of returning the grid view when 
      the LOOK command is performed.

Player - A abstract class which acts as a general player. It includes an abstract method 
	 'getCommand()' which is implemented differently in both its subclasses. It keeps 
	 track of its coordinates on the map.

HumanPlayer - A subclass of Player which also stores a quanitity of gold, and implements
	      'getCommand()' to get console input from the player using BufferedReader.

BotPlayer - Also a subclass of Player which implements 'getCommand()' using an algorithm. 
	    This algorithm looks for and chases down the human player using the LOOK and 
	    MOVE commands.