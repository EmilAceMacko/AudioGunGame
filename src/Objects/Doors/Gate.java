package Objects.Doors;

import Objects.Door;

public class Gate extends Door {
    public Gate() {
        super();
        // Default values:
        doorSprite = SPR_DOOR_GATE_START;
        animated = false;
        width = TILE;
        height = 2 * TILE;
    }
}
