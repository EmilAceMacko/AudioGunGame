package Objects.Doors;

import Objects.Door;

public class Wall_V extends Door {
    public Wall_V() {
        super();
        // Default values:
        doorSprite = SOUNDFUL ? SPR_DOOR_WALL_V_START : SPR_DOOR_WALL_V_ALT_START;
        animated = false;
        width = TILE;
        height = 2 * TILE;
        doorSound = SND_BRICK_SMASH;
        // Audio influence:
        waveInfluence = WAVE_SQUARE;
        freqInfluence = FREQ_HIGH;
        audioPersistenceThreshold = SOUNDFUL ? 15 : 8;
    }
}
