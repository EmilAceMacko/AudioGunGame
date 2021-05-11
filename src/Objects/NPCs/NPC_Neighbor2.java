package Objects.NPCs;

import Game.Game;
import Objects.NPC;

public class NPC_Neighbor2 extends NPC {
    // Constructor:
    public NPC_Neighbor2() {
        super();
        preDialogue = new String[1];
        preDialogue[0] = "Yum, those cookies were delicious!";
        postDialogue = new String[1];
        postDialogue[0] = "AHH THAT'S THE MOST ANNOYING SOUND I'VE EVER HEARD!! PLEASE STOP!!";
        // Set animation:
        animStart = SPR_NPC_1_HAPPY_START;
        // Set audio influence values:
        waveInfluence = WAVE_SQUARE;
        freqInfluence = FREQ_MID;
        audioPersistence = 1;
    }

    public void audioThreshold() {
        affected = true;
        animStart = SPR_NPC_1_MAD_START;
    }
}
