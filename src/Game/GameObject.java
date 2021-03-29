package Game;

import processing.core.PVector;

public abstract class GameObject implements Constants {
    // Standard Object Variables:
    public PVector pos;
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
    public void update() {

    }

    // Update graphics and draw object:
    public void display() {
        int xLocal = (int) (pos.x - Game.camera.roomPos.x * ROOM_WIDTH);
        int yLocal = (int) (pos.y - Game.camera.roomPos.y * ROOM_HEIGHT);
        if (spriteID > 0) Sketch.processing.image(Game.assetMgr.spriteSheet[spriteID-1], xLocal, yLocal, width, height);
    }
}