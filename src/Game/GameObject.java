package Game;

import processing.core.PVector;

public abstract class GameObject implements Constants {
    // Standard Object Variables:
    public PVector pos;
    public int width, height;
    public boolean solid;
    public int spriteID;
    public boolean animated;
    public int animStart;
    public int animLength;
    public float animTime;
    public float animSpeed;
    // Audio Variables:
    public boolean influencedByAudio;
    public int waveInfluence, freqInfluence, audioPatience;

    // Constructor:
    public GameObject() {
        // Default values:
        pos = new PVector(0, 0);
        width = TILE;
        height = TILE;
        solid = true;
        spriteID = 0;
        influencedByAudio = false;
        waveInfluence = WAVE_NONE;
        freqInfluence = FREQ_NONE;
        audioPatience = 0;
        animated = false; // Whether this object is animated.
        animStart = spriteID; // The starting frame of the animation.
        animLength = 1; // How many frames there are in the animation.
        animTime = 0.0f; // The animation timer for this object.
        animSpeed = 0.5f; // The speed of the animation (1 == 60fps, 0.5 == 30fps, 0.25 == 15fps, etc.)
    }

    // Update object code:
    public void update() {
    }

    // Animate the object:
    public void animate() {
        if(animated) {
            animTime += animSpeed;
            if (animTime >= animLength) animTime = 0.0f;
            spriteID = animStart + (int) animTime;
        }
    }

    // Update graphics and draw object:
    public void display() {
        if (spriteID > 0) Game.drawSprite(spriteID, Game.getLocalCoordinates(pos));
    }
}