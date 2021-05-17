package Game;

import processing.core.PVector;

public abstract class GameObject implements Constants {
    // Variables:
    public PVector pos, offset;
    public int width, height;
    public int spriteID;
    public int animStart, animLength;
    public float animTime, animSpeed;
    public boolean solid, animated, isHitByAudio, isHitByCorrectAudio, dead;
    public int waveInfluence, freqInfluence, audioPersistence, audioPersistenceThreshold;
    public float audioPersistenceHealTime, audioPersistenceHealRate;

    // Constructor:
    public GameObject() {
        // Default values:
        pos = new PVector(0, 0); // The position of the object (in pixels).
        offset = new PVector(0, 0); // The offset with which to draw the object (in pixels).
        width = TILE; // The width of the object (in pixels).
        height = TILE; // The height of the object (in pixels).
        spriteID = 0; // The sprite that the object is drawn with.
        animStart = 0; // The starting frame of the animation.
        animLength = 1; // How many frames there are in the animation.
        animTime = 0.0f; // The animation timer for this object.
        animSpeed = 0.5f; // The speed of the animation (1 == 60fps, 0.5 == 30fps, 0.25 == 15fps, etc.)
        solid = true; // Whether the object is solid.
        animated = false; // Whether this object is animated.
        isHitByAudio = false; // Whether the object is currently being hit with audio.
        isHitByCorrectAudio = false; // Whether the object is currently being hit with the right frequency and waveform.
        dead = false; // Whether the object should be removed from the game.
        waveInfluence = WAVE_NONE; // The waveform that this object can be influenced by.
        freqInfluence = FREQ_NONE; // The frequency that this object can be influenced by.
        audioPersistence = 0; // How many hits of "correct audio" the object has endured.
        audioPersistenceThreshold = 1; // How many hits of "correct audio" the object can take before hitting the threshold.
        audioPersistenceHealTime = 0.0f; // Counts by one heal rate every frame. When it hits 1.0, the audioPersistence is decremented by 1.
        audioPersistenceHealRate = 1f/45f; // How often the audioPersistence heals (in points per frame).
    }

    // Update object code:
    public void update() {
        isHitByAudio = false;
        isHitByCorrectAudio = false;
        audioHeal();
    }

    public void audioHeal() {
        if(audioPersistence > 0) {
            audioPersistenceHealTime += audioPersistenceHealRate;
            if (audioPersistenceHealTime >= 1.0f) {
                audioPersistence--;
                audioPersistenceHealTime = 0.0f;
            }
        } else {
            audioPersistenceHealTime = 0.0f;
        }
    }

    public boolean audioHit(int waveHitting, int freqHitting) {
        isHitByAudio = true;
        // If being hit by specific waveform OR no specific waveform is required AND if being hit by specific frequency OR no specific frequency is required:
        if ((waveHitting == waveInfluence || waveHitting == WAVE_NONE) && (freqHitting == freqInfluence || freqHitting == FREQ_NONE)) {
            isHitByCorrectAudio = true;
            audioPersistence++;
            if (audioPersistence >= audioPersistenceThreshold) {
                audioThreshold();
            }
        } else {
            isHitByCorrectAudio = false;
        }
        return isHitByCorrectAudio;
    }

    public void audioThreshold() {
        // Object-specific code to run when the audio threshold has been hit.
        // If an object needs to know when it's audio threshold has been reached, it will implement this function in its own class.
    }

    // Animate the object:
    public void animate() {
        if (animated) {
            animTime += animSpeed; // Increment the animation timer.
            if (animTime >= animLength) animTime = 0.0f; // Reset the animation timer.
            spriteID = animStart + (int) animTime; // Set the sprite based on the animation timer.
        }
    }

    // Update graphics and draw object:
    public void display() {
        if (spriteID > 0) Game.drawSprite(spriteID, Game.getLocalCoordinates(PVector.add(pos, offset)));
    }
}