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
    public int waveInfluence, freqInfluence, audioPersistence;

    // Constructor:
    public GameObject() {
        // Default values:
        pos = new PVector(0, 0); // The position of the object (in pixels).
        width = TILE; // The width of the object (in pixels).
        height = TILE; // The height of the object (in pixels).
        solid = true; // Whether the object is solid.
        spriteID = 0; // The sprite that the object is drawn with.
        influencedByAudio = false; // Whether this object can be influenced by audio.
        waveInfluence = WAVE_NONE; // The waveform that this object can be influenced by.
        freqInfluence = FREQ_NONE; // The frequency that this object can be influenced by.
        audioPersistence = 0; // How long the object can persist through the audio influence.
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
            animTime += animSpeed; // Increment the animation timer.
            if (animTime >= animLength) animTime = 0.0f; // Reset the animation timer.
            spriteID = animStart + (int) animTime; // Set the sprite based on the animation timer.
        }
    }

    // Update graphics and draw object:
    public void display() {
        if (spriteID > 0) Game.drawSprite(spriteID, Game.getLocalCoordinates(pos));
    }
}