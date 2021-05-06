package Objects.NPCs;

import Game.Game;
import Objects.Doors.Gate;
import Objects.NPC;
import java.util.ArrayList;

public class NPC_Neighbor1 extends NPC {
    // Constructor:
    public NPC_Neighbor1() {
        super();
        preDialogue = new String[4];
        preDialogue[0] = "Someone stole all my cookies... I suspect it was my NEIGHBOR over there.";
        preDialogue[1] = "Could you do me a favor and play an annoying sound at him?";
        preDialogue[2] = "Not something TOO GRINDY though, but not too SMOOTH either...";
        preDialogue[3] = "Try and experiment with different pitches, to find his sweet spot!";
        postDialogue = new String[1];
        postDialogue[0] = "HAHAHA NICE! Serves him right! Here, I've opened the gate for you!";
        // Set animation:
        animStart = SPR_NPC_2_MAD_START;
        // Set audio influence values:
        waveInfluence = WAVE_NONE;
        freqInfluence = FREQ_NONE;
    }

    public void update() {
        if(!affected) {
            // Get a list of every "neighbor 2" in the room (there should only be one):
            ArrayList<NPC_Neighbor2> list = Game.listRoomObjects(NPC_Neighbor2.class);
            // Check if neighbor 2 has been affected:
            if (list.get(0).affected) {
                affected = true;
                animStart = SPR_NPC_2_HAPPY_START;
                // Find and open the gate:
                Game.listRoomObjects(Gate.class).get(0).openDoor();
                // Give coin:
                Game.giveCoin(pos.copy());
            }
        }
        // Run default NPC code:
        super.update();
    }
}
