package Objects.NPCs;

import Game.Game;
import Objects.Doors.Wall_H;
import Objects.NPC;

import java.util.ArrayList;

public class NPC_WallTeacher extends NPC {
    public NPC_WallTeacher() {
        super();
        coinID = 4;
        preDialogue = new String[5];
        preDialogue[0] = "Aight so hear this, them walls? They're not all what they seem...";
        preDialogue[1] = "The brittle ones, they're FAKE WALLS you see...";
        preDialogue[2] = "If only you had something that could SHAKE the walls BACK AND FORTH very FAST...";
        preDialogue[3] = "The brittle walls always SOUND SLIGHTLY DIFFERENT than the other walls.";
        preDialogue[4] = "See if you can find the fake wall in this room.";
        postDialogue = new String[1];
        postDialogue[0] = "You got it! Use this knowledge wisely, there are more fake walls with secrets behind them.";
        // Set animation:
        animStart = SPR_NPC_1_IDLE_START;
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
