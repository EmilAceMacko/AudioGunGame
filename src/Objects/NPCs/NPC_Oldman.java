package Objects.NPCs;

import Objects.NPC;

public class NPC_Oldman extends NPC {
    // Constructor:
    public NPC_Oldman() {
        super();
        preDialogue = new String[3];
        preDialogue[0] = "I'm afraid this is the end...";
        preDialogue[1] = "Have you been nice and helpful to the villagers?";
        preDialogue[2] = "If that's the case, there's probably nothing else to do now.";
        // Set animation:
        animStart = SPR_NPC_4_IDLE_START;
        // Set audio influence values:
        waveInfluence = WAVE_NONE;
        freqInfluence = FREQ_NONE;
    }
}
