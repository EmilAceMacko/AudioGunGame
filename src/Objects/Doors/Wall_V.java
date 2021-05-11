package Objects.Doors;

import Objects.Door;

public class Wall_V extends Door {
    public Wall_V() {
        super();
        // Default values:
        doorSprite = SPR_DOOR_WALL_V_START;
        animated = false;
        width = TILE;
        height = 2 * TILE;
        // Audio influence:
        waveInfluence = WAVE_SQUARE;
        freqInfluence = FREQ_HIGH;
        audioPersistence = 3;
    }
}
