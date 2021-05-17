package Objects.Doors;

import Objects.Door;

public class Crate extends Door {
    public Crate() {
        super();
        // Default values:
        doorSprite = SPR_DOOR_CRATE_START;
        animated = false;
        width = TILE;
        height = TILE;
        doorSound = SND_CRATE_SMASH;
        // Audio influence:
        waveInfluence = WAVE_SAW;
        freqInfluence = FREQ_HIGH;
        audioPersistenceThreshold = SOUNDFUL ? 9 : 7;
    }
}
