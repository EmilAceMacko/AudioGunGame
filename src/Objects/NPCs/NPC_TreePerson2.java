package Objects.NPCs;

import Game.Game;
import Objects.Doors.Gate_V;
import Objects.Doors.Tree;
import Objects.Enemies.Slime;
import Objects.NPC;

import java.util.ArrayList;

public class NPC_TreePerson2 extends NPC {
    // Constructor:
    public NPC_TreePerson2() {
        super();
        coinID = 7;
        preDialogue = new String[1];
        preDialogue[0] = "Too many treeeees!! Could ya per-chance help me get rid of them all??";
        postDialogue = new String[1];
        postDialogue[0] = "Wowza thanks!! That weird-sounding gun of yours did the job in no time!";
        // Set animation:
        animStart = SPR_NPC_3_MAD_START;
        // Set audio influence values:
        waveInfluence = WAVE_NONE;
        freqInfluence = FREQ_NONE;
    }

    public void update() {
        if(!affected) {
            // Get a list of every tree in the room:
            ArrayList<Tree> list = Game.listRoomObjects(Tree.class);
            // Check if no trees are left "closed":
            boolean allOpen = true;
            for (Tree t : list) {
                if (!t.open) { // If the current tree is not "open":
                    allOpen = false; // Not all trees are "open".
                    break; // End the loop.
                }
            }
            // Check if all the trees are "open":
            if (allOpen) {
                affected = true;
                animStart = SPR_NPC_3_HAPPY_START;
                // Give coin:
                Game.giveCoin(pos.copy());
                // Write to log:
                Game.writeLogCoin(coinID, true);
            }
        }
        // Run default NPC code:
        super.update();
    }
}
