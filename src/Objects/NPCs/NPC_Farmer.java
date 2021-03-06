package Objects.NPCs;

import Game.Game;
import Objects.Enemies.Slime;
import Objects.Doors.Gate_V;
import Objects.NPC;

import java.util.ArrayList;

public class NPC_Farmer extends NPC {

    public NPC_Farmer() {
        super();
        coinID = 2;
        preDialogue = new String[3];
        preDialogue[0] = "These darn slimes are stompin' all over me crops! And they are too fast fer' me to catch!";
        preDialogue[1] = "If only me voice was LOW and SMOOTH...";
        preDialogue[2] = "... I could yell repeatedly at the slimes, they'd resonate with the tone and DISSOLVE!";
        postDialogue = new String[1];
        postDialogue[0] = "Wow! Me ears are gone and me crops are safe! As a thanks, I've open'd the gate for ya!";
        // Set animation:
        animStart = SPR_NPC_3_MAD_START;
        // Set audio influence values:
        waveInfluence = WAVE_NONE;
        freqInfluence = FREQ_NONE;
    }

    public void update() {
        if(!affected) {
            // Get a list of every slime in the room:
            ArrayList<Slime> list = Game.listRoomObjects(Slime.class);
            // Check if no slimes are left alive:
            boolean allDead = true;
            for (Slime s : list) {
                if (!s.dead) { // If the current slime is not dead:
                    allDead = false; // Not all slimes are dead.
                    break; // End the loop.
                }
            }
            if (allDead) {
                affected = true;
                animStart = SPR_NPC_3_HAPPY_START;
                // Find and open the gate:
                Game.listRoomObjects(Gate_V.class).get(0).openDoor();
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
