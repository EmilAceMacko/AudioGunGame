package Objects.NPCs;

import Game.Game;
import Objects.Doors.Crate;
import Objects.Doors.Gate_H;
import Objects.NPC;

import java.util.ArrayList;

public class NPC_CratePerson extends NPC {
    // Constructor:
    public NPC_CratePerson() {
        super();
        coinID = 8;
        preDialogue = new String[3];
        preDialogue[0] = "I got bought a bunch of stuff the other day...";
        preDialogue[1] = "...but now I've got all these WOODEN CRATES that my things arrived in...";
        preDialogue[2] = "do you have any way of disposing of them for me?";
        postDialogue = new String[1];
        postDialogue[0] = "Ayy! Thanks to you, I've now got way more free space! Thanks!!";
        // Set animation:
        animStart = SPR_NPC_2_IDLE_START;
        // Set audio influence values:
        waveInfluence = WAVE_NONE;
        freqInfluence = FREQ_NONE;
    }

    public void update() {
        if(!affected) {
            // Get a list of every crate in the room:
            ArrayList<Crate> list = Game.listRoomObjects(Crate.class);
            // Check if no crates are left "closed":
            boolean allOpen = true;
            for (Crate c : list) {
                if (!c.open) { // If the current crate is not "open":
                    allOpen = false; // Not all crates are "open".
                    break; // End the loop.
                }
            }
            // Check if all the crates are "open":
            if (allOpen) {
                affected = true;
                animStart = SPR_NPC_2_HAPPY_START;
                // Find and open the gate:
                Game.listRoomObjects(Gate_H.class).get(0).openDoor();
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
