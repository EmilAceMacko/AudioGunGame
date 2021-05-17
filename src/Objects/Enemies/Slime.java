package Objects.Enemies;

import Game.Game;
import Objects.Enemy;
import processing.core.PVector;

public class Slime extends Enemy {
    // Variables:
    public PVector roamAreaPos, roamSpeed;
    public int roamAreaWidth;
    public int roamAreaHeight;
    public int slimeSound;
    public int slimeSoundFrame;
    public boolean slimeSoundPlayed;
    // Constants:
    public static final int ANIM_IDLE_START = 0;
    public static final int ANIM_IDLE_LENGTH = 6;
    public static final float ANIM_IDLE_SPEED = 0.25f;
    public static final int ANIM_DEAD_START = 6;
    public static final int ANIM_DEAD_LENGTH = 6;
    public static final float ANIM_DEAD_SPEED = 0.25f;
    // Constructor:
    public Slime() {
        super();
        // Default values:
        spriteID = SPR_ENEMY_SLIME_START + ANIM_IDLE_START;
        roamAreaPos = new PVector(0, 0);
        roamSpeed = new PVector(0, 0);
        roamAreaWidth = 0;
        roamAreaHeight = 0;
        slimeSound = SND_SLIME_SMALL;
        slimeSoundFrame = 4;
        slimeSoundPlayed = false;
        // Animation:
        animated = true;
        animSpeed = ANIM_IDLE_SPEED;
        animStart = spriteID;
        animLength = ANIM_IDLE_LENGTH;
        animTime = (int) (Math.random() * animLength);
        // Audio influence:
        waveInfluence = WAVE_SINE;
        freqInfluence = FREQ_LOW;
        audioPersistenceThreshold = SOUNDFUL ? 4 : 3;
    }

    public void update() {
        if (!dead) {
            //Patrol();
            roam();
            super.update();
            animate();
        } else { // Is dead:
            // Play death animation:
            if (animTime < ANIM_DEAD_LENGTH)
            {
                spriteID = animStart + ANIM_DEAD_START + (int) animTime; // Set the sprite based on the animation timer.
                animTime += ANIM_DEAD_SPEED; // Increment the animation timer.
            }
        }
    }

    public void audioThreshold() {
        if (!dead) Game.playSound(SND_SLIME_SMALL_DEAD);
        super.audioThreshold();
    }

    public void roam() {
        //Game.console("Slime Area: " + roamAreaPos.x + ", " + roamAreaPos.y + " - " + (roamAreaPos.x + roamAreaWidth) + ", " + (roamAreaPos.y + roamAreaHeight));
        // Check for wall collisions horizontally (if moving horizontally):
        if (collisionSide.x != 0 && roamSpeed.x != 0) {
            roamSpeed.x *= -1; // Flip roam speed X.
        }
        // Check for wall collisions vertically (if moving vertically):
        if (collisionSide.y != 0 && roamSpeed.y != 0) {
            roamSpeed.y *= -1; // Flip roam speed Y.
        }
        // Check for roam-area collisions horizontally (if moving horizontally):
        if ((pos.x < roamAreaPos.x && roamSpeed.x < 0) || (pos.x + width > roamAreaPos.x + roamAreaWidth && roamSpeed.x > 0)) {
            roamSpeed.x *= -1; // Flip roam speed X.
        }
        // Check for roam-area collisions Vertically (if moving vertically):
        if ((pos.y < roamAreaPos.y && roamSpeed.y < 0) || (pos.y + height > roamAreaPos.y + roamAreaHeight && roamSpeed.y > 0)) {
            roamSpeed.y *= -1; // Flip roam speed Y.
        }
        // Set velocity:
        vel.x = roamSpeed.x;
        vel.y = roamSpeed.y;

        // Play jumping sound:
        if ((int) animTime == slimeSoundFrame) {
            if (!slimeSoundPlayed) {
                Game.playSound(slimeSound);
                slimeSoundPlayed = true;
            }
        } else {
            slimeSoundPlayed = false;
        }
    }

    public void display() {
        super.display();
    }

}

