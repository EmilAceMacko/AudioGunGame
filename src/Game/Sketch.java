package Game;

import processing.core.PApplet;

public class Sketch extends PApplet implements Constants {
    // Game.Sketch Variables:
    public static PApplet processing; // The link to the Processing library for other objects to use through the Game.Sketch.

    // Entry-point:
    public static void main(String[] args) {
        PApplet.main("Game.Sketch", args);
    }

    // Processing State Functions:

    public void settings() {
        // Initial app settings:
        size(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public void setup() {
        // Game.Game setup:
        processing = this;
        background(0);
    }

    public void draw() {
        // Game.Game loop:
        Game.update(); // Update game code.
        Game.display(); // Draw game graphics.
    }
}
