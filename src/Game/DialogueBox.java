package Game;

import processing.core.PVector;

public class DialogueBox implements Constants {
    // Dialogue box variables:
    public PVector pos;
    public boolean active, writing, open, doneWriting, doneAll;
    public String dialogue[];
    public int boxNumber;
    public float boxAnim;
    // Dialogue box constants:
    public static final float BOX_ANIM_SPEED = 1.0f / 30.0f;
    public static final int BOX_WIDTH = WINDOW_WIDTH - TILE*2;
    public static final int BOX_HEIGHT = TILE*2;


    // Constructor:
    DialogueBox() {
        // Default values:
        pos = new PVector(0, 0);
        active = false; // Whether the box is active (e.g. in use).
        writing = false; // Whether the box is currently writing text to the screen.
        open = false; // Whether or not the box should open up.
        doneWriting = false; // Whether or not the box has finished writing the current dialogue.
        doneAll = false; // Whether or not the box has finished all the dialogue and should close itself.
        boxNumber = 0; // Which dialogue string is currently being written/displayed.
        boxAnim = 0.0f; // The animation value for the box's open/close animation (0.0 - 1.0).
    }

    public void update() {
        if (active) {
            // Box open/close animation:
            if (open) {
                boxAnim += BOX_ANIM_SPEED;
                if (boxAnim >= 1.0f) boxAnim = 1.0f;
            } else { // Close:
                boxAnim -= BOX_ANIM_SPEED;
                if (boxAnim <= 0.0f) boxAnim = 0.0f;
            }

            // Box dialogue writing:
            if(writing) {

            } else { // Not writing:
                if(doneWriting) {
                    if(doneAll) {

                    } else {

                    }
                }

            }
        }
    }

    public void display() {
        if (active) {
            if( boxAnim > 0.0f) {
                // Draw the dialogue box:

                if (boxAnim >= 1.0f) {
                    // Draw the dialogue text:
                }
            }
        }
    }

    public void start() {
        active = true;
        writing = true;
        open = true;
    }

    public void reset() {
        active = false;
        writing = false;
        open = false;
        doneWriting = false;
        doneAll = false;
        boxNumber = 0;
        boxAnim = 0.0f;
    }
}
