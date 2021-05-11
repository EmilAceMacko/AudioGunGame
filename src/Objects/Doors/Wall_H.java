package Objects.Doors;

import Objects.Door;

public class Wall_H extends Door {
    public Wall_H() {
        super();
        // Default values:
        doorSprite = SPR_DOOR_WALL_H_START;
        animated = false;
        width = 2 * TILE;
        height = TILE;
        // Audio influence:
        waveInfluence = WAVE_SQUARE;
        freqInfluence = FREQ_HIGH;
        audioPersistence = 3;
    }
}
