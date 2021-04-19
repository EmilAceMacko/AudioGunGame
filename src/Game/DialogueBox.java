package Game;

import processing.core.PVector;
import java.util.ArrayList;

public class DialogueBox implements Constants {
    // Dialogue box variables:
    public PVector pos;
    public boolean active, writing, open, doneWriting, doneAll;
    public String[] dialogue;
    public int[] charWidth;
    public int boxNumber, charIndex, xOff, line;
    public float boxAnim;
    public ArrayList<BoxChar> chars;
    // Dialogue box constants:
    public static final float BOX_ANIM_SPEED = 1.0f / 10.0f; // How fast the box opens and closes.
    public static final int BOX_WIDTH = WINDOW_WIDTH - TILE*2; // the total width of the dialogue box.
    public static final int BOX_HEIGHT = 48 * SCALE; // The total height of the dialogue box.
    public static final int PADDING_TOP = 8 * SCALE; // How much space is between the top (and bottom) of the box and the text.
    public static final int PADDING_LEFT = 16 * SCALE; // How much space is between the left (and right) of the box and the text.
    public static final int CHAR_HEIGHT = 8 * SCALE; // The height of the characters
    public static final int LINE_WIDTH = BOX_WIDTH - PADDING_LEFT * 2; // How many characters can fit on one line.
    public static final int MAX_LINES = 3; // How many lines can fit in the dialogue box.
    public static final int CHAR_SEP = 2 * SCALE; // How much space to put between the characters.
    public static final int LINE_SEP = 4 * SCALE; // How much space to put between the lines.
    public static final char START_CHAR = ' '; // The first char in the char sprite list.

    // Constructor:
    DialogueBox() {
        // Default values:
        pos = new PVector(TILE, TILE); // The position of the dialogue box, from its upper left corner.
        active = false; // Whether the box is active (e.g. in use).
        writing = false; // Whether the box is currently writing text to the screen.
        open = false; // Whether or not the box should open up.
        doneWriting = false; // Whether or not the box has finished writing the current dialogue.
        doneAll = false; // Whether or not the box has finished all the dialogue and should close itself.
        boxNumber = 0; // Which dialogue string is currently being written/displayed.
        charIndex = 0; // The index of the newest written character in the current dialogue string.
        boxAnim = 0.0f; // The animation value for the box's open/close animation (0.0 - 1.0).
        chars = new ArrayList<>(); // The list of characters that have been written to the dialogue box.
        xOff = 0; // The horizontal position of the currently written characters (in pixels).
        line = 0; // The line that is currently being written to.

        charWidth = new int[SPR_FONT_LENGTH]; // The width of each char in pixels (with scaling).
        // Load all char widths:
        for (int i = 0; i < charWidth.length; i++) {
            charWidth[i] = Game.assetMgr.spriteSheet[SPR_FONT_START + i].width * SCALE;
        }
    }

    public void update() {
        if (active) {
            // Box open/close animation:
            if (open) { // Open:
                boxAnim += BOX_ANIM_SPEED;
                if (boxAnim >= 1.0f) {
                    boxAnim = 1.0f; // Cap the animation value at 1.
                }
            } else { // Close:
                boxAnim -= BOX_ANIM_SPEED;
                if (boxAnim <= 0.0f) {
                    boxAnim = 0.0f; // Cap the animation value at 0.
                    resetFull(); // Reset all box variables.
                }
            }

            // Box dialogue writing:
            if (writing && boxAnim >= 1.0f) {
                // Write the dialogue:

                // If there are more chars to be written for the current dialogue:
                if (charIndex < dialogue[boxNumber].length()) {
                    char currentChar = dialogue[boxNumber].charAt(charIndex); // Get the current character from the dialogue string.
                    // If the current character is a space, then find the length of the next word:
                    if (currentChar == ' ') {
                        int wordWidth = 0; // Start at "zero width", then add width of characters until a space is found (or there are no more characters in the dialogue string):
                        for (int i = charIndex; i + 1 < dialogue[boxNumber].length() && dialogue[boxNumber].charAt(i + 1) != ' '; i++) {
                            wordWidth += charWidth[dialogue[boxNumber].charAt(i + 1) - START_CHAR] + CHAR_SEP;
                        }
                        // Now check if there is room for the next word on the current line:
                        if (xOff + wordWidth > LINE_WIDTH) {
                            // Move to the next line:
                            if (line < MAX_LINES) {
                                line++; // Move one line down.
                                xOff = 0; // Reset the horizontal position back to the left.
                                charIndex++; // Skip the space that came before the word.
                            } else { // Otherwise, no more lines left, so stop writing:
                                writing = false;
                                doneWriting = true;
                            }
                        } else { // Otherwise, there is room for the word, so write the space that came before it:
                            xOff += charWidth[' ' - START_CHAR] + CHAR_SEP;
                            charIndex++;
                        }
                    } else { // Otherwise, current character is not a space:
                        // Write the character:
                        int charSpr = currentChar - START_CHAR; // The first character in the sprite map is a space, so subtract its index first!
                        chars.add(new BoxChar(this, PADDING_LEFT + xOff, PADDING_TOP + line * (CHAR_HEIGHT + LINE_SEP), SPR_FONT_START + charSpr));
                        xOff += charWidth[charSpr] + CHAR_SEP;
                        charIndex++;
                    }
                } else { // No more characters to write for the current dialogue:
                    writing = false;
                    doneWriting = true;
                }

            } else { // Not writing:
                if (doneWriting) {
                    if (Game.getInput(B_SPACE, PRESS)) {
                        // If this was not the final dialogue string:
                        if (boxNumber < dialogue.length - 1) {
                            // Continue to the next dialogue string:
                            reset();
                            boxNumber++;
                            writing = true;
                            doneWriting = false;
                        } else { // This was the final dialogue string:
                            open = false;
                        }
                    }
                }
                // If the dialogue box has fully closed:
                if (!open && boxAnim <= 0.0f) {
                    resetFull();
                }
            }
        }
    }

    public void display() {
        if (active && boxAnim > 0.0f) { // If box is active and not fully closed:
            // Draw the dialogue box (with open/close animation):
            Sketch.processing.fill(0); // Black filling.
            Sketch.processing.noStroke();
            Sketch.processing.rect(pos.x + (1.0f - boxAnim) * BOX_WIDTH / 2.0f, pos.y, BOX_WIDTH * boxAnim, BOX_HEIGHT);
            if (boxAnim >= 1.0f) { // If box is fully open:
                // Draw each character in the dialogue:
                for (BoxChar c : chars) c.display();
            }
        }
    }

    // Start the dialogue box (used by DeeJay to start dialogue):
    public void start() {
        resetFull();
        active = true;
        writing = true;
        open = true;
    }
    // Stop the dialogue box (used by DeeJay to stop dialogue):
    public void stop() {
        writing = false;
        open = false;
    }

    // Resets basic per-dialogue variables: (For when moving to the next dialogue string)
    public void reset() {
        charIndex = 0;
        chars.clear();
        xOff = 0;
        line = 0;
    }
    // Resets the entire dialogue system: (For when done with all dialogue strings)
    public void resetFull() {
        reset();
        active = false;
        writing = false;
        open = false;
        doneWriting = false;
        doneAll = false;
        boxNumber = 0;
        boxAnim = 0.0f;
    }

    // Nested class for dialogue characters:
    public static class BoxChar {
        public DialogueBox owner;
        public int spriteID;
        public PVector pos;
        // Constructor:
        public BoxChar(DialogueBox _owner, float _x, float _y, int _spriteID) {
            this(_owner, new PVector(_x, _y), _spriteID);
        }
        public BoxChar(DialogueBox _owner, PVector _pos, int _spriteID) {
            owner = _owner;
            pos = _pos;
            spriteID = _spriteID;
        }

        public void display() {
            Game.drawSprite(spriteID, owner.pos.x + pos.x, owner.pos.y + pos.y);
        }
    }
}
