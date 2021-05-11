package Objects;

import Game.GameObject;

public class NPC extends GameObject {
    public String[] preDialogue, postDialogue;
    public boolean affected, hasDialogue, talking;
    public int coinID;

    public NPC() {
        super();
        // Default values:
        affected = false;
        hasDialogue = true;
        talking = false;
        coinID = 0;
        // Animation:
        animated = true;
        animStart = SPR_NPC_1_IDLE_START;
        animLength = SPR_NPC_LENGTH;
        animSpeed = 0.05f;
    }

    public void update() {
        super.update();
        animate();
    }

    public void display() {
        super.display();
    }
}
