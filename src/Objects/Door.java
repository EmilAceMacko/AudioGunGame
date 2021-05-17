package Objects;

import Game.Game;
import Game.GameObject;

public class Door extends GameObject {
    // Variables:
    public boolean open;
    public int doorSprite;
    public int doorSound;
    // Constructor:
    public Door() {
        super();
        // Default values:
        open = false;
        doorSprite = 0;
        doorSound = -1;
    }

    public void update() {
        spriteID = open ? doorSprite + 1 : doorSprite;
        super.update();
    }

    public void audioThreshold() {
        if (!open) openDoor();
    }

    public void openDoor() {
        open = true;
        solid = false;
        if (doorSound >= 0) Game.playSound(doorSound);
    }
    public void closeDoor() {
        open = false;
        solid = true;
        if (doorSound >= 0) Game.playSound(doorSound);
    }

    public void display() {
        super.display();
    }
}
