package Game;

import processing.core.PVector;

public abstract class GameObject implements Constants {
    // Standard Object Variables:
    public PVector pos, vel;
    public int width, height;
    public boolean solid;
    public int spriteID;
    // Audio Variables:
    public boolean influencedByAudio;
    public int waveInfluence, freqInfluence, audioPatience;

    // Constructor:
    public GameObject() {
        // Default values:
        pos = new PVector(0, 0);
        vel = new PVector(0, 0);
        width = TILE;
        height = TILE;
        solid = true;
        spriteID = 0;
        influencedByAudio = false;
        waveInfluence = WAVE_NONE;
        freqInfluence = FREQ_NONE;
        audioPatience = 0;
    }

    // Update object code:
    public void updateActions() {

    }

    // Update object graphics:
    public void updateGraphics() {
        // Default object has no specific graphics code.
    }
}
