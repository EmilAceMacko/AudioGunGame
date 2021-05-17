package Objects.Doors;

import Objects.Door;

public class Wall_H extends Door {
    public Wall_H() {
        super();
        // Default values:
        doorSprite = SOUNDFUL ? SPR_DOOR_WALL_H_START : SPR_DOOR_WALL_H_ALT_START;
        animated = false;
        width = 2 * TILE;
        height = TILE;
        doorSound = SND_BRICK_SMASH;
        // Audio influence:
        waveInfluence = WAVE_SQUARE;
        freqInfluence = FREQ_HIGH;
        audioPersistenceThreshold = SOUNDFUL ? 15 : 8;
    }
}
