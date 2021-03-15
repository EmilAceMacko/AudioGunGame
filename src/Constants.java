public interface Constants {
    // ---------------------------- Variables:
    // Waveforms:
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
    int ROOM_WIDTH = WINDOW_WIDTH;
    int ROOM_HEIGHT = WINDOW_HEIGHT;

    // ---------------------------- Functions:
    // Collision:
    static boolean collision() {
        // TODO code AABB collision between two objects!
        return false;
    }
}
