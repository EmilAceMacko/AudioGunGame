package Objects;

import Game.Entity;
import Game.Game;

public class DeeJay extends Entity {

    public float aimAngle;
    public boolean controllable;
    public boolean shooting;
    public float moveSpeed;

    public DeeJay() {
        // Default values:
        super();
        aimAngle = 0.0f;
        controllable = true;
        shooting = false;
        moveSpeed = 2.5f;
    }

    public void update() {
        // Horizontal Inputs:
        if(Game.getInput(LEFT, HOLD)) {
            vel.x = -moveSpeed; // Move left.
        }
        else if (Game.getInput (RIGHT, HOLD)) {
            vel.x = moveSpeed; // Move right.
        }
        else vel.x = 0; // Don't move.

        // Vertical Inputs:
        if(Game.getInput(B_UP, HOLD)) {
            vel.y = -moveSpeed; // Move up.
        }
        else if (Game.getInput(DOWN, HOLD)) {
            vel.y = moveSpeed; // Move down.
        }
        else vel.y = 0; // Don't move.

        super.update(); // Let Entity class handle movement.

        if (shooting) {
            //DeeJay.fireBullet();
        }
    }

    public void display() {
        // Custom display stuff here:
    }
}
