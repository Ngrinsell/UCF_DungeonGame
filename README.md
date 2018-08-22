# Pixel Dungeon Game
A basic 2D array based dungeon game with very basic pixel art / animations.  Made as a final project for DIG 3480 - Computer as a Medium.  Created in 4 days.

Original requirements to have one level for the game but the unique aspect about this implementation is that the level is randomized everytime a user plays.

This game was created using Processing.

## Detailed Summary
Lost life is simple dungeon type game where the user uses the arrow keys to
navigate the 'hero' dungeon past enemies, that move to a random space every time
the player moves, and hazards like water or pits, to take possession of the red
gem and get back to the entrance.  Walking into a hazard automatically results in
death.  Touching an enemy loses one life bar.  When the life bar is empty the
player has died and the game is over.  

The unique aspect of this game is that the board is regenerated randomly upon 
every new instance of play.

# Technical Details

## How to Run
Ensure you have Java installed on the machine you plan to run the game.

Double click the .bat file and the game will begin!

## File Details

### Folder: data
This folder holds all the images used within the game.

### Folder: lib
This folder holds any files needed to make the application export possible.

### Folder: source
This folder holds all source files for the game.
