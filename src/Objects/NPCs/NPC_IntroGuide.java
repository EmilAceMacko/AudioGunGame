package Objects.NPCs;

import Objects.NPC;

public class NPC_IntroGuide extends NPC {
    // Constructor:
    public NPC_IntroGuide() {
        super();
        preDialogue = new String[5];
        preDialogue[0] = "Welcome DeeJay! Press SPACE to advance dialogue!";
        preDialogue[1] = "I see you have your AUDIO GUN with you, we can use that!";
        preDialogue[2] = "As you will see, many different problems have arisen...";
        preDialogue[3] = "but they should all be something you can solve with that AUDIO GUN of yours.";
        preDialogue[4] = "Be sure to help as many people as you can with it!";
        // Set animation:
        animStart = SPR_NPC_1_IDLE_START;
        // Set audio influence values:
        waveInfluence = WAVE_NONE;
        freqInfluence = FREQ_NONE;
    }
}
