package Game;

import processing.core.PVector;

public abstract class GameObject implements Constants {
    // Variables:
    public PVector pos, offset;
    public int width, height;
    public int spriteID;
    public int animStart, animLength;
    public float animTime, animSpeed;
    public boolean solid, animated, isHitByAudio, dead;
    public int waveInfluence, freqInfluence, waveHitting, freqHitting, audioPersistence, audioPersistenceMax;
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
        dead = false; // Whether the object should be removed from the game.
        waveInfluence = WAVE_NONE; // The waveform that this object can be influenced by.
        freqInfluence = FREQ_NONE; // The frequency that this object can be influenced by.
        waveHitting = WAVE_NONE; // The waveform that is currently hitting this object (if influenced by audio).
        freqHitting = FREQ_NONE; // The frequency that is currently hitting this object (if influenced by audio).
        audioPersistence = 0; // How long the object has persisted while being hit with audio.
        audioPersistenceMax = 0; // How long the object can persist while being hit with audio.
    }

    // Update object code:
    public void update() {
        audioPersist();
    }

    public void audioPersist() {
        // If currently being hit by audio:
        if (isHitByAudio) {
            // Compare to specific waveforms/frequencies:
            if ((waveHitting == waveInfluence || waveHitting == WAVE_NONE) && // If being hit by specific waveform OR no specific waveform is required:
                    (freqHitting == freqInfluence || freqHitting == FREQ_NONE)) { // If being hit by specific frequency OR no specific frequency is required:
                if (audioPersistence < audioPersistenceMax) {
                    audioPersistence++;
                    offset.x = SCALE * (Math.abs((audioPersistence % 4) - 2) - 1); // Apply shake.
                } else {
                    offset.x = 0;
                    offset.y = 0;
                    audioThreshold();
                }
            }
        } else { // Not currently being hit by audio:
            waveHitting = WAVE_NONE;
            freqHitting = FREQ_NONE;
            if (audioPersistence > 0) {
                audioPersistence = 0;
                offset.x = 0;
                offset.y = 0;
            }
        }
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