package Objects;

import Game.GameObject;

public class NPC extends GameObject {
    public String[] preDialogue;
    public String[] postDialogue;
    public boolean affected;
    public boolean hasDialogue;

    public NPC() {
        super();
        // Default values:
        affected = false;
        hasDialogue = true;
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
