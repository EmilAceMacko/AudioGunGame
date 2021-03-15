package Objects;

import Game.GameObject;

public class DeeJay extends GameObject {

    public float aimAngle;
    public boolean controllable;
    public boolean canMove;
    public boolean shooting;
    public boolean moving;
    public float moveSpeed;

    public DeeJay() {

    }

    public void updateActions() {
        if(shooting) {
            DeeJay.fireBullet();
        }
    }

    public void updateGraphics() {

    }
}
