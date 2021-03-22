package Game;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class Sketch extends PApplet implements Constants {
    // Sketch Variables:
    public static PApplet processing; // The link to the Processing library for other objects to use through the Sketch.

    // Entry-point:
    public static void main(String[] args) {
        PApplet.main("Game.Sketch", args);
    }

    // Processing State Functions:

    public void settings() {
        // Initial app settings:
        size(WINDOW_WIDTH, WINDOW_HEIGHT);
        smooth(0);
    }

    public void setup() {
        // Game setup:
        processing = this; // Create a link to the processing library.
        Game.init();
    }


    public void keyPressed() {
        // Making sure that we can read Uppercase letters - Changes it to a string and then back to a char:
        char newKey = key;
        String keyLowerCase = Character.toString(newKey);
        keyLowerCase = keyLowerCase.toLowerCase();
        newKey = keyLowerCase.charAt(0);
        // For every key:
        for (int i = 0; i < Game.inputMap.length; i++) {
            // If the key is not already being held (or pressed):
            if (Game.input[i] != HOLD && newKey == Game.inputMap[i]) Game.input[i] = PRESS; // Key is pressed.
        }
    }

    public void keyReleased() {
        // Making sure that we can read Uppercase letters - Changes it to a string and then back to a char:
        char newKey = key;
        String keyLowerCase = Character.toString(newKey);
        keyLowerCase = keyLowerCase.toLowerCase();
        newKey = keyLowerCase.charAt(0);
        // For every key:
        for (int i = 0; i < Game.inputMap.length; i++) {
            if (newKey == Game.inputMap[i]) Game.input[i] = RELEASE; // Key is released.
        }
    }

    public void mousePressed() {
        if ((Game.input[MB_LEFT] & HOLD) != HOLD && mouseButton == PConstants.LEFT)
            Game.input[MB_LEFT] = PRESS; // Left mouse button is pressed.
        if ((Game.input[MB_RIGHT] & HOLD) != HOLD && mouseButton == PConstants.RIGHT)
            Game.input[MB_RIGHT] = PRESS; // Right mouse button is pressed.
    }

    public void mouseReleased() {
        if (mouseButton == PConstants.LEFT) Game.input[MB_LEFT] = RELEASE; // Left mouse button is released.
        if (mouseButton == PConstants.RIGHT) Game.input[MB_RIGHT] = RELEASE; // Right mouse button is released.
    }

    public void draw() {
        // Game loop:
        Game.update(); // Update game code.
        Game.display(); // Draw game graphics.
    }
}
