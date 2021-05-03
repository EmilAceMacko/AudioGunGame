package Objects;

import Game.Entity;

public class Enemy extends Entity {
    public boolean dead = false;


    public Enemy() {
        super();
        // Default values:
        dead = false;
    }

    public void update() {
        if(!dead) {
            super.update();
        } else { // Is dead:
            // Play death animation:
        }
    }
    // Kill the enemy:
    public void audioThreshold() {
        if(!dead) {
            dead = true;
            solid = false;
            animTime = 0;
            waveInfluence = WAVE_NONE;
            freqInfluence = FREQ_NONE;
        }
    }

    public void display() {
        super.display();
    }
}
