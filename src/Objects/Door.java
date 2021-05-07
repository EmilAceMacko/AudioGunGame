package Objects;

import Game.GameObject;

public class Door extends GameObject {
    // Variables:
    public boolean open;
    public int doorSprite;

    public Door() {
        super();
        // Default values:
        open = false;
        doorSprite = 0;
    }

    public void update() {
        spriteID = open ? doorSprite + 1 : doorSprite;
        super.update();
    }

    public void audioThreshold() {
        if(!open) openDoor();
    }

    public void openDoor() {
        open = true;
        solid = false;
        waveInfluence = WAVE_NONE;
        freqInfluence = FREQ_NONE;
    }
    public void closeDoor() {
        open = false;
        solid = true;
    }

    public void display() {
        super.display();
    }
}
