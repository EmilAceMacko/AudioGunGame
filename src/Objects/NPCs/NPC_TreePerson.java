package Objects.NPCs;

import Game.Game;
import Objects.Doors.Tree;
import Objects.NPC;
import java.util.ArrayList;

public class NPC_TreePerson extends NPC {
    // Constructor:
    public NPC_TreePerson() {
        super();
        coinID = 1;
        preDialogue = new String[3];
        preDialogue[0] = "When did that tree grow here? Now I can't pass through anymore.";
        preDialogue[1] = "Maybe if I had a SAW of some sort...";
        preDialogue[2] = "Then I could HIT the tree REPEATEDLY until it was chopped down.";
        postDialogue = new String[1];
        postDialogue[0] = "Wow! Thank you! Luckily your AUDIO GUN had a SAW waveform.";
        // Set animation:
        animStart = SPR_NPC_2_IDLE_START;
        // Set audio influence values:
        waveInfluence = WAVE_NONE;
        freqInfluence = FREQ_NONE;
    }

    public void update() {
        if(!affected) {
            // Get a list of every tree in the room:
            ArrayList<Tree> list = Game.listRoomObjects(Tree.class);
            // Check if the first tree is "open" (e.g. chopped down):
            if (list.get(0).open) {
                affected = true;
                animStart = SPR_NPC_2_HAPPY_START;
                // Give coin:
                Game.giveCoin(pos.copy());
                // Write to log:
                Game.writeLogCoin(coinID, false);
            }
        }
        // Run default NPC code:
        super.update();
    }
}
