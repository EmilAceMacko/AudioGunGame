package Objects;

import Game.GameObject;

public class NPC extends GameObject {
    public String[] preDialogue;
    public String[] postDialogue;
    public boolean affected;

    public NPC() {
        super();
        affected = false;
    }

    public void update() {
        super.update();
    }

    public void display() {
        super.display();
    }
}
