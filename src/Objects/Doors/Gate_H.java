package Objects.Doors;

import Objects.Door;

public class Gate_H extends Door {
    public Gate_H() {
        super();
        // Default values:
        doorSprite = SPR_DOOR_GATE_H_START;
        animated = false;
        width = 2 * TILE;
        height = TILE;
    }
}
