package Objects.Doors;

import Objects.Door;

public class Tree extends Door {

    public Tree() {
        super();
        // Default values:
        doorSprite = SPR_DOOR_TREE_START;
        animated = false;
        width = 2 * TILE;
        height = 2 * TILE;
        // Audio influence:
        waveInfluence = WAVE_SAW;
        freqInfluence = FREQ_HIGH;
        audioPersistenceMax = 60;
    }
}
