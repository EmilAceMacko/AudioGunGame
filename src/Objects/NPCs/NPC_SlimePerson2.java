package Objects.NPCs;

import Game.Game;
import Objects.Doors.Gate_V;
import Objects.Enemies.BigSlime;
import Objects.NPC;

import java.util.ArrayList;

public class NPC_SlimePerson2 extends NPC {
    public NPC_SlimePerson2() {
        super();
        coinID = 6;
        preDialogue = new String[1];
        preDialogue[0] = "Please help me! Can you get rid of these weird slime monsters on my lawn?";
        postDialogue = new String[1];
        postDialogue[0] = "No more slimy lawn for this guy! You have my eternal gratitude!";
        // Set animation:
        animStart = SPR_NPC_3_MAD_START;
        // Set audio influence values:
        waveInfluence = WAVE_NONE;
        freqInfluence = FREQ_NONE;
    }

    public void update() {
        if(!affected) {
            // Get a list of every slime in the room:
            ArrayList<BigSlime> list = Game.listRoomObjects(BigSlime.class);
            // Check if no slimes are left alive:
            boolean allDead = true;
            for (BigSlime s : list) {
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
