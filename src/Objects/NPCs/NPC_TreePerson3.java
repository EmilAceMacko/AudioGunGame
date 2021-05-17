package Objects.NPCs;

import Game.Game;
import Objects.Doors.Tree;
import Objects.NPC;

import java.util.ArrayList;

public class NPC_TreePerson3 extends NPC {
    // Constructor:
    public NPC_TreePerson3() {
        super();
        coinID = 10;
        preDialogue = new String[4];
        preDialogue[0] = "So here's a challenge for ya.";
        preDialogue[1] = "See all these trees? They're all INDESTRUCTIBLE PLASTIC.";
        preDialogue[2] = "That is, with the exception of TWO of them that are REAL TREES.";
        preDialogue[3] = "I'll give you a coin if you can find them and chop them down.";
        postDialogue = new String[1];
        postDialogue[0] = "Grrrr! You managed to spot the real ones from the fakes! I... congratulate you.";
        // Set animation:
        animStart = SPR_NPC_1_IDLE_START;
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
                animStart = SPR_NPC_1_MAD_START;
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
