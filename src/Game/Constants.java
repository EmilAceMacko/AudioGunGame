package Game;

import processing.core.PVector;

public interface Constants {
    // ---------------------------- Variables:
    // Debug:
    boolean DEBUG = true;

    // Waveform types:
    int WAVE_NONE = 0;
    int WAVE_SINE = 1;
    int WAVE_SQUARE = 2;
    int WAVE_SAW = 3;

    // Frequency types:
    int FREQ_NONE = 0;
    int FREQ_LOW = 1;
    int FREQ_MID = 2;
    int FREQ_HIGH = 3;

    // Screen variables:
    int SCALE = 3; // How much the pixel graphics are upscaled.
    int WINDOW_WIDTH = 768; // In pixels.
    int WINDOW_HEIGHT = 720; // In pixels.

    // World variables:
    int ROOM_WIDTH = WINDOW_WIDTH; // In pixels.
    int ROOM_HEIGHT = WINDOW_HEIGHT; // In pixels.
    int WORLD_WIDTH = 16; // In rooms.
    int WORLD_HEIGHT = 16; // In rooms.
    int TILE = 16 * SCALE; // The width/height of a single tile (16 pixels times the game scale). Objects should have dimensions that are measured in whole tiles!
    int SPEED_LIMIT = TILE / 4; // The limit to how fast objects are allowed to move (for the sake of accurate collisions)
    int UP = 0;
    int LEFT = 1;
    int DOWN = 2;
    int RIGHT = 3;

    // Input variables:
    // Input buttons:
    int B_AMOUNT = 7;
    int B_UP = UP;
    int B_LEFT = LEFT;
    int B_DOWN = DOWN;
    int B_RIGHT = RIGHT;
    int B_SPACE = 4;
    int MB_LEFT = 5;
    int MB_RIGHT = 6;
    // Input states:
    int NONE = 0; // No input.
    int HOLD = 1; // Input is being held.
    int PRESS = 3; // Input has just been pressed.
    int RELEASE = 4; // Input has just been released.
}
