package Objects.NPCs;

import Game.Game;
import Objects.Doors.Gate_V;
import Objects.Enemies.BigSlime;
import Objects.Enemies.Slime;
import Objects.NPC;

import java.util.ArrayList;

public class NPC_SlimePerson extends NPC {
    public NPC_SlimePerson() {
        super();
        coinID = 9;
        preDialogue = new String[1];
        preDialogue[0] = "HELP ME PLEASE!! THESE SLIMES HAVE GOT ME SURROUNDED!!";
        postDialogue = new String[1];
        postDialogue[0] = "Thank yoooou for saving my life there!!";
        // Set animation:
        animStart = SPR_NPC_1_MAD_START;
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
            // Get a list of every big slime in the room:
            ArrayList<BigSlime> list2 = Game.listRoomObjects(BigSlime.class);
            // Check if no big slimes are left alive:
            for (BigSlime s : list2) {
                if (!s.dead) { // If the current big slime is not dead:
                    allDead = false; // Not all big slimes are dead.
                    break; // End the loop.
                }
            }
            if (allDead) {
                affected = true;
                animStart = SPR_NPC_1_HAPPY_START;
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
