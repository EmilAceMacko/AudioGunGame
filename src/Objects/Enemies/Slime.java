package Objects.Enemies;

import Game.Game;
import Objects.Enemy;
import processing.core.PVector;

public class Slime extends Enemy {
    /*public boolean patrolling = true;
    private int move = 1;
    private int timeleft = 0;
    private int timeright = 0;
    private int timeMax = 180;
    private int pause = 0;*/
    public PVector roamAreaPos, roamSpeed;
    public int roamAreaWidth;
    public int roamAreaHeight;

    public Slime() {
        super();
        // Default values:
        spriteID = SPR_ENEMY_SLIME_START;
        roamAreaPos = new PVector(0, 0);
        roamSpeed = new PVector(0, 0);
        roamAreaWidth = 0;
        roamAreaHeight = 0;
        // Animation:
        animated = true;
        animSpeed = 0.25f;
        animStart = spriteID;
        animLength = 6;
        // Audio influence:
        waveInfluence = WAVE_SINE;
        freqInfluence = FREQ_LOW;
        audioPersistenceMax = 60;
    }

    public void update() {
        if(!dead) {
            //Patrol();
            roam();
            super.update();
            animate();
        } else { // Is dead:
            // Play death animation:
            if (animTime < animLength)
            {
                spriteID = animStart + 6 + (int) animTime; // Set the sprite based on the animation timer.
                animTime += animSpeed; // Increment the animation timer.
            }
        }
    }

    public void roam() {
        //Game.console("Slime Area: " + roamAreaPos.x + ", " + roamAreaPos.y + " - " + (roamAreaPos.x + roamAreaWidth) + ", " + (roamAreaPos.y + roamAreaHeight));
        // Check for wall collisions horizontally (if moving horizontally):
        if(collisionSide.x != 0 && roamSpeed.x != 0) {
            roamSpeed.x *= -1; // Flip roam speed X.
        }
        // Check for wall collisions vertically (if moving vertically):
        if(collisionSide.y != 0 && roamSpeed.y != 0) {
            roamSpeed.y *= -1; // Flip roam speed Y.
        }
        // Check for roam-area collisions horizontally (if moving horizontally):
        if((pos.x < roamAreaPos.x && roamSpeed.x < 0) || (pos.x + width > roamAreaPos.x + roamAreaWidth && roamSpeed.x > 0)) {
            roamSpeed.x *= -1; // Flip roam speed X.
        }
        // Check for roam-area collisions Vertically (if moving vertically):
        if((pos.y < roamAreaPos.y && roamSpeed.y < 0) || (pos.y + height > roamAreaPos.y + roamAreaHeight && roamSpeed.y > 0)) {
            roamSpeed.y *= -1; // Flip roam speed Y.
        }
        // Set velocity:
        vel.x = roamSpeed.x;
        vel.y = roamSpeed.y;
    }

    /*public void Patrol() {
        //This code makes the troll move forward and backward patrolling 'vel.x'
        if (patrolling) {
            // The Troll patrols if "move == 1 or 3". If it is 2 or 0, it will stand still for a second....
            if (move == 1) {
                timeleft +=1;
                vel.x = -1.5f;
                if (timeleft >= timeMax) {
                    move = 2;
                    timeleft = 0;
                }
            }
            if (move == 2 || move == 0) {
                vel.x = 0;
                pause += 1;
                if (pause == 60){
                    if (move == 2) {
                        move = 3;
                        pause= 0;
                    }
                    if (move == 0) {
                        move = 1;
                        pause = 0;
                    }
                }
            }
            if (move == 3) {
                timeright +=1;
                vel.x = 1.5f;
                if (timeright >= timeMax) {
                    move = 0;
                    timeright = 0;
                }
            }
        } else {
            vel.x = 0;
        }
    }*/

    public void display() {
        super.display();
    }

}

