package Objects.Enemies;

import Game.Game;

public class BigSlime extends Slime {
    // Constants:
    public static final int ANIM_IDLE_START = 0;
    public static final int ANIM_IDLE_LENGTH = 5;
    public static final float ANIM_IDLE_SPEED = 0.2f;
    public static final int ANIM_DEAD_START = 5;
    public static final int ANIM_DEAD_LENGTH = 7;
    public static final float ANIM_DEAD_SPEED = 0.25f;
    // Constructor:
    public BigSlime() {
        super();
        spriteID = SPR_ENEMY_BIGSLIME_START + ANIM_IDLE_START;
        width = 2*TILE;
        height = 2*TILE;
        slimeSound = SND_SLIME_BIG;
        slimeSoundFrame = 4;
        // Animation:
        animated = true;
        animSpeed = ANIM_IDLE_SPEED;
        animStart = spriteID;
        animLength = ANIM_IDLE_LENGTH;
        animTime = (int) (Math.random() * animLength);
        // Audio influence:
        waveInfluence = WAVE_SQUARE;
        freqInfluence = FREQ_LOW;
        audioPersistenceThreshold = SOUNDFUL ? 15 : 10;
    }

    public void audioThreshold() {
        if (!dead) Game.playSound(SND_SLIME_BIG_DEAD);
        super.audioThreshold();
    }

    public void update() {
        super.update();
    }

    public void display() {
        super.display();
    }
}
