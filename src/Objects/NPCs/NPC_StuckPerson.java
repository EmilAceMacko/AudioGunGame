package Objects.NPCs;

import Game.Game;
import Objects.Doors.Wall_H;
import Objects.NPC;

public class NPC_StuckPerson extends NPC {
    public NPC_StuckPerson() {
        super();
        coinID = 5;
        preDialogue = new String[1];
        preDialogue[0] = "HELP!! GET ME OUT OF HERE PLEASE!!";
        postDialogue = new String[2];
        postDialogue[0] = "THANK YOU SO MUCH I'VE BEEN STUCK HERE FOR DAYS!!";
        postDialogue[1] = "(Though the thrill of being free now has me stuck here dancing for a bit longer...)";
        // Set animation:
        animStart = SPR_NPC_1_MAD_START;
        // Set audio influence values:
        waveInfluence = WAVE_NONE;
        freqInfluence = FREQ_NONE;
    }

    public void update() {
        if (!affected) {
            // Get the first horizontal fake wall in the room:
            Wall_H wall = Game.listRoomObjects(Wall_H.class).get(0);
            // Check if wall has been "opened" (broken):
            if (wall.open) {
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
