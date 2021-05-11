package Objects.Enemies;

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
        // Animation:
        animated = true;
        animSpeed = ANIM_IDLE_SPEED;
        animStart = spriteID;
        animLength = ANIM_IDLE_LENGTH;
        // Audio influence:
        waveInfluence = WAVE_SQUARE;
        freqInfluence = FREQ_LOW;
        audioPersistence = 3;
    }

    public void update() {
        super.update();
    }

    public void display() {
        super.display();
    }
}
