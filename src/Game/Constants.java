package Game;

import processing.core.PVector;

public interface Constants {
    // ---------------------------- Variables:
    // Debug / Data collection:
    boolean DEBUG = false;
    String LOG_FILENAME = "uploadme.txt";

    // Game Version:
    boolean SOUNDFUL = true;

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
    int FPS = 60; // The amount of frames per second.
    int SCALE = 3; // How much the pixel graphics are upscaled.
    int WINDOW_WIDTH = 768; // In pixels.
    int WINDOW_HEIGHT = 720; // In pixels.
    int GUI_HEIGHT = 48 * SCALE; // In pixels.
    int ROOM_WIDTH = WINDOW_WIDTH; // In pixels.
    int ROOM_HEIGHT = WINDOW_HEIGHT - GUI_HEIGHT; // In pixels.
    int WORLD_WIDTH = 16; // In rooms.
    int WORLD_HEIGHT = 16; // In rooms.
    int TILE = 16 * SCALE; // The width/height of a single tile (16 pixels times the game scale). Objects should have dimensions that are measured in whole tiles!
    int SPEED_LIMIT = TILE / 4; // The limit to how fast objects are allowed to move (for the sake of accurate collisions)
    int UP = 0;
    int LEFT = 1;
    int DOWN = 2;
    int RIGHT = 3;
    int RAY_LENGTH = WINDOW_WIDTH; // The default maximum range of the audio gun ray.

    // Input variables:
    // Input buttons:
    int B_AMOUNT = 10;
    int B_UP = UP;
    int B_LEFT = LEFT;
    int B_DOWN = DOWN;
    int B_RIGHT = RIGHT;
    int B_SPACE = 4;
    int B_1 = 5;
    int B_2 = 6;
    int B_3 = 7;
    int MB_LEFT = 8;
    int MB_RIGHT = 9;
    // Input states:
    int NONE = 0; // No input.
    int HOLD = 1; // Input is being held.
    int PRESS = 3; // Input has just been pressed.
    int RELEASE = 4; // Input has just been released.

    // ---------------------------- Sprite index markers:

    int SPR_FONT_START = 1;
    int SPR_FONT_LENGTH = 95;
    int SPR_DEEJAY_START = 96;
    int SPR_DEEJAY_LENGTH = 10;
    int SPR_ENEMY_SLIME_START = 106;
    int SPR_ENEMY_SLIME_LENGTH = 12;
    int SPR_NPC_1_IDLE_START = 118;
    int SPR_NPC_1_HAPPY_START = 120;
    int SPR_NPC_1_MAD_START = 122;
    int SPR_NPC_2_IDLE_START = 124;
    int SPR_NPC_2_HAPPY_START = 126;
    int SPR_NPC_2_MAD_START = 128;
    int SPR_NPC_3_IDLE_START = 130;
    int SPR_NPC_3_HAPPY_START = 132;
    int SPR_NPC_3_MAD_START = 134;
    int SPR_NPC_LENGTH = 2;
    int SPR_GUI_BACKGROUND = 136;
    int SPR_GUI_CHECKBOX_START = 137;
    int SPR_GUI_GAUGE_START = 139;
    int SPR_GUI_SLIDER = 141;
    int SPR_GUI_WINDOW = 142;
    int SPR_DOOR_TREE_START = 143;
    int SPR_DOOR_TREE_LENGTH = 2;
    int SPR_GUI_CURSOR_1 = 145;
    int SPR_GUI_CURSOR_2 = 146;
    int SPR_DOOR_GATE_V_START = 147;
    int SPR_DOOR_GATE_V_LENGTH = 2;
    int SPR_GUI_DIALOGUE_NEXT = 149;
    int SPR_GUI_DIALOGUE_LAST = 150;
    int SPR_NPC_4_IDLE_START = 151;
    int SPR_GUI_COIN_START = 153;
    int SPR_GUI_COIN_LENGTH = 6;
    int SPR_GUI_COIN_DIGIT_START = 159;
    int SPR_GUI_COIN_DIGIT_LENGTH = 10;
    int SPR_DOOR_WALL_H_START = 169;
    int SPR_DOOR_WALL_H_LENGTH = 2;
    int SPR_DOOR_WALL_V_START = 171;
    int SPR_DOOR_WALL_V_LENGTH = 2;
    int SPR_DOOR_GATE_H_START = 173;
    int SPR_DOOR_GATE_H_LENGTH = 2;
    int SPR_ENEMY_BIGSLIME_START = 175;
    int SPR_ENEMY_BIGSLIME_LENGTH = 12;

    // ---------------------------- Audio index markers:

    int SND_MUSIC = 0;
}
