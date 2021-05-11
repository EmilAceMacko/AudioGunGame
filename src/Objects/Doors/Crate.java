package Objects.Doors;

import Objects.Door;

public class Crate extends Door {
    public Crate() {
        super();
        // Default values:
        doorSprite = SPR_DOOR_GATE_H_START;
        animated = false;
        width = TILE;
        height = TILE;
        // Audio influence:
        waveInfluence = WAVE_SAW;
        freqInfluence = FREQ_HIGH;
        audioPersistence = 3;
    }
}
