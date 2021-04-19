package Objects.NPCs;

import Objects.NPC;

public class NPC_IntroGuide extends NPC {
    // Constructor:
    public NPC_IntroGuide() {
        super();
        preDialogue = new String[3];
        preDialogue[0] = "Hello! This is the first line.";
        preDialogue[1] = "Now the second line...";
        preDialogue[2] = "... Lastly, the third line. Goodbye!";
        // Set animation:
        animStart = SPR_NPC_1_IDLE_START;
        // Set audio influence values:
        waveInfluence = WAVE_SINE;
    }
}
