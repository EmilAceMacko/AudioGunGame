package Objects.Doors;

import Objects.Door;

public class Gate_V extends Door {
    public Gate_V() {
        super();
        // Default values:
        doorSprite = SPR_DOOR_GATE_V_START;
        animated = false;
        width = TILE;
        height = 2 * TILE;
        doorSound = SND_DOOR;
    }
}
