package Objects;

import Game.Entity;

public class Enemy extends Entity {
    // Variables:
    public boolean dead;
    // Constructor:
    public Enemy() {
        super();
        // Default values:
        dead = false;
    }

    public void update() {
        if (!dead) {
            super.update();
        } // Else: Play death animation.
    }
    // Kill the enemy:
    public void audioThreshold() {
        if (!dead) {
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
