package Objects.Doors;

public class TreeFake extends Tree {
    public TreeFake() {
        if (!SOUNDFUL) doorSprite = SPR_DOOR_TREE_ALT_START;
        // Audio influence:
        waveInfluence = WAVE_NONE;
        freqInfluence = FREQ_NONE;
    }
}
