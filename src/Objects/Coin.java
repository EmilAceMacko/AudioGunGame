package Objects;

import Game.Game;
import Game.GameObject;
import processing.core.PVector;

public class Coin extends GameObject {
    // Variables:
    public PVector start, target;
    public int state;
    public float anim, bobAnim;
    public DeeJay deejay;
    // Constants:
    public static final float ANIM_APPEAR_SPEED = 1f / 40f;
    public static final float ANIM_WAIT_SPEED = 1f / 20f;
    public static final float ANIM_FLY_SPEED = 1f / 40f;
    public static final float ANIM_BOB_SPEED = 1f / 60f;
    public static final int STATE_APPEARING = 0;
    public static final int STATE_WAITING = 1;
    public static final int STATE_FLYING_TOWARDS_PLAYER = 2;
    public static final int BOB_HEIGHT = SCALE;
    // Constructor:
    public Coin(PVector _pos) {
        super();
        pos = _pos;
        // Default values:
        animated = false;
        animSpeed = 0.25f;
        animLength = SPR_GUI_COIN_LENGTH;
        spriteID = SPR_GUI_COIN_START;
        solid = false;

        start = PVector.add(pos, new PVector(0, -8 * SCALE));
        target = PVector.add(start, new PVector(0, -16 * SCALE));
        state = STATE_APPEARING;
        anim = 0f;
        bobAnim = 0f;
        deejay = Game.listGlobalObjects(DeeJay.class).get(0);
    }

    public void update() {
        if (!dead) {
            // Animate bobbing:
            bobAnim += ANIM_BOB_SPEED;
            if(bobAnim >= 1f) bobAnim = 0f;
            offset.y = (float) Math.sin(bobAnim * 2 * Math.PI) * BOB_HEIGHT;
            // Animate movement:
            switch (state) {
                case (STATE_APPEARING) -> {
                    anim += ANIM_APPEAR_SPEED;
                    pos = PVector.lerp(start, target, 1f - (float) Math.pow(1f - anim, 3));
                    if (anim >= 1f) {
                        state = STATE_WAITING;
                        anim = 0f;
                        pos = target.copy();
                    }
                }
                case (STATE_WAITING) -> {
                    anim += ANIM_WAIT_SPEED;
                    if (anim >= 1f) {
                        state = STATE_FLYING_TOWARDS_PLAYER;
                        animated = true;
                        anim = 0f;
                        start = pos.copy();
                    }
                }
                case (STATE_FLYING_TOWARDS_PLAYER) -> {
                    anim += ANIM_FLY_SPEED;
                    pos = PVector.lerp(start, deejay.pos, (1f - (float) Math.cos(anim * Math.PI)) / 2f);
                    if (anim >= 1f) {
                        Game.coin++;
                        // TODO - Play "coin get" sound!
                        dead = true;
                    }
                }
            }
        }
    }

    public void display() {
        if (!dead) {
            super.display();
        }
    }
}
