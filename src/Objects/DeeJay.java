package Objects;

import Game.Entity;

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
        // Move to the right as a test:
        vel.x = moveSpeed;
        super.update();

        if (shooting) {
            //DeeJay.fireBullet();
        }
    }

    public void display() {
        // Custom display stuff here:
    }
}
