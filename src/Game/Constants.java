package Game;

public interface Constants {
    // ---------------------------- Variables:
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
    int WINDOW_WIDTH = 768;
    int WINDOW_HEIGHT = 720;

    // World variables:
    int ROOM_WIDTH = WINDOW_WIDTH;
    int ROOM_HEIGHT = WINDOW_HEIGHT;
    int WORLD_WIDTH = 16; // Width of the world in rooms.
    int WORLD_HEIGHT = 16; // Height of the world in rooms.
    int TILE = 16 * SCALE; // The width/height of a single tile (16 pixels times the game scale). Objects should have dimensions that are measured in whole tiles!
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
    int NONE = 0;
    int HOLD = 1;
    int PRESS = 3;
    int RELEASE = 4;

    // ---------------------------- Functions:

    // Collision check:
    static boolean collision(GameObject self, GameObject other) {
        return collision(self, 0, 0, other);
    }
    static boolean collision(GameObject self, float xOff, float yOff, GameObject other) {
        return (self.pos.x + xOff < other.pos.x + other.width &&
                self.pos.x + xOff + self.width > other.pos.x &&
                self.pos.y + yOff < other.pos.y + other.height &&
                self.pos.y + yOff + self.height > other.pos.y);
    }

    // Direction conversion:
    static int xDir(int _dir) {
        return switch (_dir) {
            case (LEFT) -> -1;
            case (RIGHT) -> 1;
            default -> 0;
        };
    }
    static int yDir(int _dir) {
        return switch (_dir) {
            case (UP) -> -1;
            case (DOWN) -> 1;
            default -> 0;
        };
    }
}
